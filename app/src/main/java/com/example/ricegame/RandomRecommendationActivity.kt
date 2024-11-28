package com.example.ricegame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.R

class RandomRecommendationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_recommendation) // 랜덤 추천 화면 연결
    }
}
