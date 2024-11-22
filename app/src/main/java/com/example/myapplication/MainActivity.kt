package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 시스템 바 여백 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 버튼 이벤트 설정
        findViewById<Button>(R.id.btnBalanceGame).setOnClickListener {
            startActivity(Intent(this, BalanceGameActivity::class.java))
        }

        findViewById<Button>(R.id.btnNearbyRestaurants).setOnClickListener {
            startActivity(Intent(this, NearbyRestaurantsActivity::class.java))
        }

        findViewById<Button>(R.id.btnRandomRestaurant).setOnClickListener {
            startActivity(Intent(this, RandomRestaurantActivity::class.java))
        }

        findViewById<Button>(R.id.btnSearchRestaurant).setOnClickListener {
            startActivity(Intent(this, SearchRestaurantActivity::class.java))
        }
    }
}
