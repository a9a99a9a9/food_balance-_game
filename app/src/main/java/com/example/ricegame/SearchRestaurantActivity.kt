package com.example.ricegame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ricegame.R

class SearchRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_restaurant) // 식당 검색 화면 연결
    }
}
