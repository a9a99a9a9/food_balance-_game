package com.example.ricegame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nicknameTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharedPreferences 가져오기
        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = preferences.getBoolean("is_logged_in", false)

        // 로그인이 되어 있지 않으면 로그인 화면으로 이동
        if (!isLoggedIn) {
            navigateToLogin()
            return
        }

        // 닉네임 표시 및 로그아웃 버튼 초기화
        nicknameTextView = findViewById(R.id.tv_nickname)
        logoutButton = findViewById(R.id.btn_logout)

        // 닉네임 가져오기
        val nickname = preferences.getString("nickname", "사용자") ?: "사용자"
        nicknameTextView.text = "안녕하세요, $nickname 님!"

        // 로그아웃 버튼 클릭 이벤트 설정
        logoutButton.setOnClickListener {
            val editor = preferences.edit()
            editor.putBoolean("is_logged_in", false)
            editor.remove("nickname") // 닉네임 제거
            editor.apply()

            navigateToLogin()
        }

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

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
