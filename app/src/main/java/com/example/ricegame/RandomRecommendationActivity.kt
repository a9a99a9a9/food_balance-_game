package com.example.ricegame
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telecom.Call
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.databinding.ActivityRandomRecommendationBinding
import kotlin.random.Random

class RandomRecommendationActivity : AppCompatActivity() {

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

        binding.startButton.setOnClickListener { startSpinning() }
        binding.stopButton.setOnClickListener { stopSpinning() }

        fetchRestaurantData()
    }

    private fun fetchRestaurantData() {
        // 식당 이름 서버에서 불러오는 기능 추가예정
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
