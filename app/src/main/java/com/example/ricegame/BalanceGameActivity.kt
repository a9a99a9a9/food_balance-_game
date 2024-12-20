package com.example.ricegame

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ricegame.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

class BalanceGameActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsLayout: GridLayout

    // 질문과 선택지 데이터
    private val questions = listOf(
        "식당의 위치는?" to listOf("구정문", "신정문", "사대부고"),
        "식사 종류는?" to listOf("한식", "양식", "중식", "일식", "아무거나"),
        "매운거? 안매운거?" to listOf("매운거", "안매운거", "아무거나"),
        "뜨거운거? 차가운거?" to listOf("뜨거운거", "차가운거", "아무거나"),
        "면류?" to listOf("O", "X", "아무거나"),
        "가벼운 간식? 든든한 한끼?" to listOf("간식", "식사", "아무거나"),
        "국물있는 음식? 없는 음식?" to listOf("국물 있는 음식", "국물 없는 음식", "아무거나")
    )

    private var currentQuestionIndex = 0
    private val selectedOptions = mutableListOf<Int>() // 선택된 항목을 저장할 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_game)

        questionTextView = findViewById(R.id.questionTextView)
        optionsLayout = findViewById(R.id.optionsLayout)

        showQuestion()
    }

    private fun showQuestion() {
        val (question, options) = questions[currentQuestionIndex]
        questionTextView.text = question

        optionsLayout.removeAllViews()
        options.forEachIndexed { index, option ->
            val button = android.widget.Button(this).apply {
                text = option
                textSize = 18f
                setBackgroundColor(ContextCompat.getColor(this@BalanceGameActivity, R.color.deep_blue))
                setTextColor(ContextCompat.getColor(this@BalanceGameActivity, android.R.color.white))
                setOnClickListener { onOptionSelected(index) }
            }

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(12, 12, 12, 12)
            }

            button.layoutParams = params
            optionsLayout.addView(button)
        }
    }

    private fun onOptionSelected(optionIndex: Int) {
        selectedOptions.add(optionIndex + 1)

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion()
        } else {
            questionTextView.text = "추천 결과를 불러오는 중..."
            optionsLayout.removeAllViews()
            fetchRestaurantData(selectedOptions)
        }
    }

    private fun fetchRestaurantData(selectedOptions: List<Int>) {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:3000/list"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    questionTextView.text = "네트워크 오류가 발생했습니다."
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.let { responseBody ->
                    try {
                        val jsonArray = JSONArray(responseBody.string())
                        val restaurantList = mutableListOf<Restaurant>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val restaurant = Restaurant(
                                id = jsonObject.getInt("id"),
                                name = jsonObject.getString("name"),
                                location = jsonObject.getInt("location"),
                                type = jsonObject.getInt("type"),
                                spicy = jsonObject.getInt("spicy"),
                                hot = jsonObject.getInt("hot"),
                                noodle = jsonObject.getInt("noodle"),
                                main = jsonObject.getInt("main"),
                                broth = jsonObject.getInt("broth"),
                                address = jsonObject.optString("address", null)
                            )
                            restaurantList.add(restaurant)
                        }

                        val sortedRestaurants = sortRestaurantsByScore(restaurantList, selectedOptions)

                        runOnUiThread {
                            showRecommendations(sortedRestaurants)
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            questionTextView.text = "데이터를 처리하는 중 오류가 발생했습니다."
                        }
                    }
                }
            }
        })
    }

    private fun sortRestaurantsByScore(
        restaurants: List<Restaurant>,
        selectedOptions: List<Int>
    ): List<Restaurant> {
        return restaurants.sortedByDescending { restaurant ->
            var score = 0
            if (selectedOptions[0] != 0 && restaurant.location == selectedOptions[0]) score++
            if (selectedOptions[1] != 0 && restaurant.type == selectedOptions[1]) score++
            if (selectedOptions[2] != 0 && restaurant.spicy == selectedOptions[2]) score++
            if (selectedOptions[3] != 0 && restaurant.hot == selectedOptions[3]) score++
            if (selectedOptions[4] != 0 && restaurant.noodle == selectedOptions[4]) score++
            if (selectedOptions[5] != 0 && restaurant.main == selectedOptions[5]) score++
            if (selectedOptions[6] != 0 && restaurant.broth == selectedOptions[6]) score++
            score
        }
    }

    private fun showRecommendations(recommendedRestaurants: List<Restaurant>) {
        questionTextView.text = "추천 식당 목록:"
        optionsLayout.removeAllViews()

        if (recommendedRestaurants.isEmpty()) {
            questionTextView.text = "조건에 맞는 식당이 없습니다."
            return
        }

        recommendedRestaurants.forEach { restaurant ->
            val textView = TextView(this).apply {
                text = "${restaurant.name} (${restaurant.address})"
                textSize = 18f
                setTextColor(ContextCompat.getColor(this@BalanceGameActivity, android.R.color.black))
            }
            optionsLayout.addView(textView)
        }
    }

    data class Restaurant(
        val id: Int,
        val name: String,
        val location: Int,
        val type: Int,
        val spicy: Int,
        val hot: Int,
        val noodle: Int,
        val main: Int,
        val broth: Int,
        val address: String?
    )
}
