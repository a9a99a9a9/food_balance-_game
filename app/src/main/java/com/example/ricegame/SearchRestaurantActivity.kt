package com.example.ricegame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class SearchRestaurantActivity : AppCompatActivity() {
    private lateinit var restaurantList: MutableList<Restaurant>
    private lateinit var adapter: RestaurantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_restaurant)

        recyclerView = findViewById(R.id.rv_search_results)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchEditText = findViewById(R.id.et_search)
        searchButton = findViewById(R.id.btn_search)

        restaurantList = mutableListOf()

        // RestaurantAdapter 초기화 및 클릭 이벤트 설정
        adapter = RestaurantAdapter(restaurantList) { restaurant ->
            showRestaurantModal(restaurant) // 클릭 시 모달 표시
        }
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                fetchRestaurantData(query)
            } else {
                Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRestaurantData(query: String) {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:3000/list" // 전체 데이터를 가져오는 API URL

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SearchRestaurantActivity, "연결 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonArray = JSONArray(responseBody.string())
                    restaurantList.clear()

                    // 입력된 키워드와 일치하는 데이터 필터링
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val address = jsonObject.getString("address")

                        if (name.contains(query, ignoreCase = true)) { // 대소문자 무시
                            restaurantList.add(Restaurant(name, address))
                        }
                    }

                    runOnUiThread {
                        if (restaurantList.isEmpty()) {
                            Toast.makeText(this@SearchRestaurantActivity, "결과가 없습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            adapter.notifyDataSetChanged() // RecyclerView 업데이트
                        }
                    }
                }
            }
        })
    }

    private fun showRestaurantModal(restaurant: Restaurant) {
        // BottomSheetDialog 생성
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_restaurant, null)
        dialog.setContentView(view)

        // 모달 내 뷰 초기화
        val nameTextView = view.findViewById<TextView>(R.id.tv_restaurant_name)
        val addressTextView = view.findViewById<TextView>(R.id.tv_restaurant_address)
        val openLinkButton = view.findViewById<Button>(R.id.btn_open_link)

        // 음식점 정보 설정
        nameTextView.text = restaurant.name
        addressTextView.text = restaurant.address

        // 링크 열기 버튼 클릭 이벤트
        openLinkButton.setOnClickListener {
            val url = restaurant.address // 주소를 URL로 사용
            if (url.startsWith("http://") || url.startsWith("https://")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent) // 브라우저로 이동
            } else {
                Toast.makeText(this, "유효하지 않은 링크입니다: $url", Toast.LENGTH_SHORT).show()
            }
        }

        // 모달 표시
        dialog.show()
    }
}