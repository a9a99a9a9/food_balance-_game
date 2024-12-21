package com.example.ricegame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.btn_login)
        val registerButton: Button = findViewById(R.id.btn_register)
        val kakaoLoginButton: ImageButton = findViewById(R.id.btn_kakao_login)
        val usernameEditText: EditText = findViewById(R.id.et_username)
        val passwordEditText: EditText = findViewById(R.id.et_password)

        // SharedPreferences 가져오기
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 저장된 사용자 정보 확인
            val savedUsername = sharedPreferences.getString("username", null)
            val savedPassword = sharedPreferences.getString("password", null)
            val nickname = sharedPreferences.getString("nickname", "사용자")

            if (username == savedUsername && password == savedPassword) {
                // 로그인 성공 시 상태 저장
                val editor = sharedPreferences.edit()
                editor.putBoolean("is_logged_in", true)
                editor.apply()

                // 메인 화면으로 이동
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("nickname", nickname)
                startActivity(intent)
                finish() // LoginActivity 종료
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 이벤트
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 카카오 로그인 버튼 클릭 이벤트
        kakaoLoginButton.setOnClickListener {
            Toast.makeText(this, "카카오 로그인 기능은 구현 중입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
