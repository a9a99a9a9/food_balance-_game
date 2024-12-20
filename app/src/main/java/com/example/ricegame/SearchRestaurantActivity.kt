package com.example.ricegame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class SearchRestaurantActivity : AppCompatActivity() {
    private lateinit var restaurantList: MutableList<Map<String, String>>
    private lateinit var adapter: RestaurantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var resetButton: Button

    // 필터 UI
    private lateinit var locationSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var spicyCheckBox: CheckBox
    private lateinit var hotCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_restaurant)

        searchEditText = findViewById(R.id.et_search)
        searchButton = findViewById(R.id.btn_search)
        resetButton = findViewById(R.id.btn_reset)
        recyclerView = findViewById(R.id.rv_search_results)
        locationSpinner = findViewById(R.id.spinner_location)
        typeSpinner = findViewById(R.id.spinner_type)
        spicyCheckBox = findViewById(R.id.checkbox_spicy)
        hotCheckBox = findViewById(R.id.checkbox_hot)

        restaurantList = mutableListOf()
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RestaurantAdapter(restaurantList) { restaurant ->
            openLink(restaurant["url"])
        }
        recyclerView.adapter = adapter

        setupFilters()
        fetchRestaurantData() // 처음 실행 시 전체 데이터 로드

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            val location = locationSpinner.selectedItem.toString().toIntOrNull() ?: 0
            val type = typeSpinner.selectedItem.toString().toIntOrNull() ?: 0
            val spicy = if (spicyCheckBox.isChecked) 1 else 0
            val hot = if (hotCheckBox.isChecked) 1 else 0

            fetchRestaurantData(query, location, type, spicy, hot)
        }

        resetButton.setOnClickListener {
            searchEditText.text.clear()
            locationSpinner.setSelection(0)
            typeSpinner.setSelection(0)
            spicyCheckBox.isChecked = false
            hotCheckBox.isChecked = false
            fetchRestaurantData() // 초기화 후 전체 데이터 로드
        }
    }

    private fun setupFilters() {
        val locationOptions = listOf("모든 지역", "1", "2", "3", "4")
        locationSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationOptions)

        val typeOptions = listOf("모든 타입", "1", "2", "3", "4")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeOptions)
    }

    private fun fetchRestaurantData(
        query: String = "",
        location: Int = 0,
        type: Int = 0,
        spicy: Int = 0,
        hot: Int = 0
    ) {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:3000/list"
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

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val locationValue = jsonObject.getInt("location")
                        val typeValue = jsonObject.getInt("type")
                        val spicyValue = jsonObject.getInt("spicy")
                        val hotValue = jsonObject.getInt("hot")
                        val address = jsonObject.getString("address")
                        val url = jsonObject.optString("url", "")

                        if (name.contains(query, ignoreCase = true) &&
                            (location == 0 || location == locationValue) &&
                            (type == 0 || type == typeValue) &&
                            (spicy == 0 || spicy == spicyValue) &&
                            (hot == 0 || hot == hotValue)
                        ) {
                            restaurantList.add(mapOf("name" to name, "address" to address, "url" to url))
                        }
                    }

                    runOnUiThread {
                        if (restaurantList.isEmpty()) {
                            Toast.makeText(this@SearchRestaurantActivity, "결과가 없습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    private fun openLink(url: String?) {
        if (!url.isNullOrEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } else {
            Toast.makeText(this, "유효하지 않은 링크입니다", Toast.LENGTH_SHORT).show()
        }
    }
}
