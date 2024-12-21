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

        val usernameEditText: EditText = findViewById(R.id.et_register_id)
        val passwordEditText: EditText = findViewById(R.id.et_register_password)
        val confirmPasswordEditText: EditText = findViewById(R.id.et_register_confirm_password) // 비밀번호 확인 추가
        val nicknameEditText: EditText = findViewById(R.id.et_register_nickname)
        val registerButton: Button = findViewById(R.id.btn_register_complete)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            val nickname = nicknameEditText.text.toString().trim()

            when {
                username.isEmpty() -> {
                    Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                nickname.isEmpty() -> {
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 사용자 정보를 저장하는 로직 (SharedPreferences 또는 서버와 통신)
                    Toast.makeText(this, "회원가입 성공: $nickname", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}
