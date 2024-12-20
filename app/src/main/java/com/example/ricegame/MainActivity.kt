package com.example.ricegame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 밸런스 게임 버튼
        val btnBalanceGame: Button = findViewById(R.id.btn_balance_game)
        btnBalanceGame.setOnClickListener {
            val intent = Intent(this, BalanceGameActivity::class.java)
            startActivity(intent)
        }

        // 주변 밥집 추천 버튼
        val btnNearbyRecommendation: Button = findViewById(R.id.btn_nearby_recommendation)
        btnNearbyRecommendation.setOnClickListener {
            val intent = Intent(this, NearbyRecommendationActivity::class.java)
            startActivity(intent)
        }

        // 랜덤 밥집 추천 버튼
        val btnRandomRecommendation: Button = findViewById(R.id.btn_random_recommendation)
        btnRandomRecommendation.setOnClickListener {
            val intent = Intent(this, RandomRecommendationActivity::class.java)
            startActivity(intent)
        }

        // 식당 검색 버튼
        val btnSearchRestaurant: Button = findViewById(R.id.btn_search_restaurant)
        btnSearchRestaurant.setOnClickListener {
            val intent = Intent(this, SearchRestaurantActivity::class.java)
            startActivity(intent)
        }

        // 즐겨찾기 버튼
        val btnFavorites: Button = findViewById(R.id.btn_favorites)
        btnFavorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
}
