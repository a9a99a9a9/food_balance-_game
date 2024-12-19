package com.example.ricegame

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

data class Restaurant(val name: String, val address: String)

class BalanceGameActivity : AppCompatActivity() {
    private var location: Int = 0
    private var type: Int = 0
    private var spicy: Int = 0
    private var hot: Int = 0
    private var noodle: Int = 0
    private var main: Int = 0
    private var broth: Int = 0

    private val restaurantList = mutableListOf<Restaurant>()
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_game)

        // 추가 로직 작성
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RestaurantAdapter(restaurantList)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btn_location1).setOnClickListener { location = 1 }
        findViewById<Button>(R.id.btn_location2).setOnClickListener { location = 2 }
        findViewById<Button>(R.id.btn_location3).setOnClickListener { location = 3 }

        findViewById<Button>(R.id.btn_type1).setOnClickListener { type = 1 }
        findViewById<Button>(R.id.btn_type2).setOnClickListener { type = 2 }
        findViewById<Button>(R.id.btn_type3).setOnClickListener { type = 3 }
        findViewById<Button>(R.id.btn_type4).setOnClickListener { type = 4 }

        findViewById<Button>(R.id.btn_spicy1).setOnClickListener { spicy = 1 }
        findViewById<Button>(R.id.btn_spicy2).setOnClickListener { spicy = 2 }

        findViewById<Button>(R.id.btn_hot1).setOnClickListener { hot = 1 }
        findViewById<Button>(R.id.btn_hot2).setOnClickListener { hot = 2 }

        findViewById<Button>(R.id.btn_noodle1).setOnClickListener { noodle = 1 }
        findViewById<Button>(R.id.btn_noodle2).setOnClickListener { noodle = 2 }

        findViewById<Button>(R.id.btn_main1).setOnClickListener { main = 1 }
        findViewById<Button>(R.id.btn_main2).setOnClickListener { main = 2 }

        findViewById<Button>(R.id.btn_broth1).setOnClickListener { broth = 1 }
        findViewById<Button>(R.id.btn_broth2).setOnClickListener { broth = 2 }

        findViewById<Button>(R.id.btn_submit).setOnClickListener {
            fetchRestaurantData()
        }
    }

    private fun fetchRestaurantData() {
        val client = OkHttpClient()
        val url = "http://10.0.2.2:3000/list" +
                "?location=$location&type=$type&spicy=$spicy&hot=$hot&noodle=$noodle&main=$main&broth=$broth"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@BalanceGameActivity, "Connect erro!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonArray = JSONArray(responseBody.string())
                    restaurantList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val address = jsonObject.getString("address")
                        restaurantList.add(Restaurant(name, address))
                    }

                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }
}
