package com.example.ricegame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // SharedPreferences 초기화
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // 로그인 상태 확인
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val nickname = sharedPreferences.getString("nickname", "사용자")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
            finish()
        }

        // UI 요소 초기화
        val loginButton: Button = findViewById(R.id.btn_login)
        val registerButton: Button = findViewById(R.id.btn_register)
        val kakaoLoginButton: ImageButton = findViewById(R.id.btn_kakao_login)
        val usernameEditText: EditText = findViewById(R.id.et_username)
        val passwordEditText: EditText = findViewById(R.id.et_password)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 간단한 로그인 검증 로직 (예제)
                if (username == "test" && password == "1234") {
                    // 로그인 상태 저장
                    sharedPreferences.edit().apply {
                        putBoolean("isLoggedIn", true)
                        putString("nickname", "테스트 사용자")
                        apply()
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("nickname", "테스트 사용자")
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 회원가입 버튼 클릭 이벤트
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 카카오 로그인 버튼 클릭 이벤트
        kakaoLoginButton.setOnClickListener {
            // 카카오 로그인 로직을 구현해야 합니다.
            Toast.makeText(this, "카카오 로그인 기능은 구현 중입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
