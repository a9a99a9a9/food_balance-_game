package com.example.ricegame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.BalanceGameActivity
import com.example.ricegame.NearbyRecommendationActivity
import com.example.ricegame.R
import com.example.ricegame.RandomRecommendationActivity
import com.example.ricegame.SearchRestaurantActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // 밸런스 게임 버튼
        val btnBalanceGame: Button = findViewById(R.id.btn_balance_game)
        btnBalanceGame.setOnClickListener {
            // 밸런스 게임 화면으로 이동
            val intent = Intent(this, BalanceGameActivity::class.java)
            startActivity(intent)
        }

        // 주변 밥집 추천 버튼
        val btnNearbyRecommendation: Button = findViewById(R.id.btn_nearby_recommendation)
        btnNearbyRecommendation.setOnClickListener {
            // 주변 밥집 추천 화면으로 이동
            val intent = Intent(this, NearbyRecommendationActivity::class.java)
            startActivity(intent)
        }

        // 랜덤 밥집 추천 버튼
        val btnRandomRecommendation: Button = findViewById(R.id.btn_random_recommendation)
        btnRandomRecommendation.setOnClickListener {
            // 랜덤 밥집 추천 화면으로 이동
            val intent = Intent(this, RandomRecommendationActivity::class.java)
            startActivity(intent)
        }

        // 식당 검색 버튼
        val btnSearchRestaurant: Button = findViewById(R.id.btn_search_restaurant)
        btnSearchRestaurant.setOnClickListener {
            // 식당 검색 화면으로 이동
            val intent = Intent(this, SearchRestaurantActivity::class.java)
            startActivity(intent)
        }
    }
}
