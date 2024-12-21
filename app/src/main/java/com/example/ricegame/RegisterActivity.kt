package com.example.ricegame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameEditText: EditText = findViewById(R.id.et_register_id) // 변경
        val passwordEditText: EditText = findViewById(R.id.et_register_password)
        val nicknameEditText: EditText = findViewById(R.id.et_register_nickname)
        val registerButton: Button = findViewById(R.id.btn_register_complete) // 변경

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val nickname = nicknameEditText.text.toString()

            if (username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 사용자 정보를 저장하는 로직 (SharedPreferences 또는 서버와 통신)
                Toast.makeText(this, "회원가입 성공: $nickname", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
