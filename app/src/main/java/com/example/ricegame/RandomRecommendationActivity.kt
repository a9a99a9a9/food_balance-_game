package com.example.ricegame
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telecom.Call
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.databinding.ActivityRandomRecommendationBinding
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import kotlin.random.Random

data class Restaurant(val name: String, val address: String)

class RandomRecommendationActivity : AppCompatActivity() {
    private var location: Int = 0
    private var type: Int = 0
    private var spicy: Int = 0
    private var hot: Int = 0
    private var noodle: Int = 0
    private var main: Int = 0
    private var broth: Int = 0
    private val restaurantList = mutableListOf<Restaurant>()
    private lateinit var adapter: RestaurantAdapter

    private lateinit var binding: ActivityRandomRecommendationBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isSpinning = false
    private var restaurants = listOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9", "10") // 임시 값
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_recommendation)

        binding = ActivityRandomRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchRestaurantData()

        binding.startButton.setOnClickListener { startSpinning() }
        binding.stopButton.setOnClickListener { stopSpinning() }
    }

    private fun fetchRestaurantData() {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:3000/list" +
                "?location=$location&type=$type&spicy=$spicy&hot=$hot&noodle=$noodle&main=$main&broth=$broth"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@RandomRecommendationActivity, "Connection error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                response.body?.let { responseBody ->
                    try {
                        // JSON 데이터를 파싱
                        val jsonArray = JSONArray(responseBody.string())
                        val tempRestaurants = mutableListOf<String>() // 임시 리스트

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val name = jsonObject.getString("name") // "name" 키의 값을 가져옴
                            tempRestaurants.add(name) // 리스트에 추가
                        }

                        // restaurants 리스트를 업데이트 (UI 스레드에서 동작 필요)
                        runOnUiThread {
                            restaurants = tempRestaurants // 업데이트
                            if (restaurants.isNotEmpty()) {
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
        if (restaurants.isEmpty()) {
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

                currentIndex = Random.nextInt(restaurants.size)
                binding.RestaurantName.text = restaurants[currentIndex]

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
                    binding.RestaurantName.text = restaurants[currentIndex]
                    binding.startButton.visibility = Button.VISIBLE
                    binding.stopButton.visibility = Button.GONE
                    return
                }

                currentIndex = Random.nextInt(restaurants.size)
                binding.RestaurantName.text = restaurants[currentIndex]

                delay += 20
                handler.postDelayed(this, delay)
            }
        })
    }
}
