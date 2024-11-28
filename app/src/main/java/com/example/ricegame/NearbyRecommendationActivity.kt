package com.example.ricegame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.R

class NearbyRecommendationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_recommendation) // 가까운 식당 추천 레이아웃 연결
    }
}
