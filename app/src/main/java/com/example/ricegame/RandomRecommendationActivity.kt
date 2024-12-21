package com.example.ricegame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.databinding.ActivityRandomRecommendationBinding
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import kotlin.random.Random

data class Restaurant(val name: String, val address: String, val url: String)

class RandomRecommendationActivity : AppCompatActivity() {
    private var location: Int = 0
    private var type: Int = 0
    private var spicy: Int = 0
    private var hot: Int = 0
    private var noodle: Int = 0
    private var main: Int = 0
    private var broth: Int = 0

    private lateinit var binding: ActivityRandomRecommendationBinding
    private val handler = Handler(Looper.getMainLooper())

    private var isSpinning = false
    private var currentIndex = 0

    private val restaurantList = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchRestaurantData()

        binding.startButton.setOnClickListener { startSpinning() }
        binding.stopButton.setOnClickListener { stopSpinning() }

        // 식당 이름 클릭 시 URL로 이동
        binding.RestaurantName.setOnClickListener {
            val selectedRestaurant = restaurantList.getOrNull(currentIndex)
            if (selectedRestaurant != null && selectedRestaurant.url.isNotBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRestaurant.url))
                startActivity(intent)
            } else {
                Toast.makeText(this, "유효하지 않은 링크입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRestaurantData() {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:3000/list" +
                "?location=$location&type=$type&spicy=$spicy&hot=$hot&noodle=$noodle&main=$main&broth=$broth"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // 오류 로그 출력
                runOnUiThread {
                    Toast.makeText(this@RandomRecommendationActivity, "Connection error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    try {
                        val jsonArray = JSONArray(responseBody.string())
                        val tempRestaurants = mutableListOf<Restaurant>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val name = jsonObject.getString("name")
                            val address = jsonObject.getString("address")
                            val url = jsonObject.optString("url", "") // URL 필드 처리
                            tempRestaurants.add(Restaurant(name, address, url))
                        }

                        runOnUiThread {
                            restaurantList.clear()
                            restaurantList.addAll(tempRestaurants)

                            if (restaurantList.isNotEmpty()) {
                                Toast.makeText(this@RandomRecommendationActivity, "Data loaded!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@RandomRecommendationActivity, "No restaurants found.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@RandomRecommendationActivity, "Data parsing error!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun startSpinning() {
        if (isSpinning || restaurantList.isEmpty()) {
            binding.RestaurantName.text = "No data available"
            return
        }

        isSpinning = true
        binding.startButton.visibility = Button.GONE
        binding.stopButton.visibility = Button.VISIBLE
        binding.stopButton.isEnabled = true
        binding.stopButton.setBackgroundResource(R.drawable.stop_button_bg)

        handler.post(object : Runnable {
            override fun run() {
                if (!isSpinning) return

                currentIndex = Random.nextInt(restaurantList.size)
                binding.RestaurantName.text = restaurantList[currentIndex].name

                handler.postDelayed(this, 50)
            }
        })
    }

    private fun stopSpinning() {
        isSpinning = false
        binding.stopButton.isEnabled = false
        binding.stopButton.setBackgroundResource(R.drawable.clicked_stop_button)

        var delay = 50L
        handler.post(object : Runnable {
            override fun run() {
                if (delay > 300) {
                    binding.RestaurantName.text = restaurantList[currentIndex].name
                    binding.startButton.visibility = Button.VISIBLE
                    binding.stopButton.visibility = Button.GONE
                    return
                }

                currentIndex = Random.nextInt(restaurantList.size)
                binding.RestaurantName.text = restaurantList[currentIndex].name

                delay += 20
                handler.postDelayed(this, delay)
            }
        })
    }
}
