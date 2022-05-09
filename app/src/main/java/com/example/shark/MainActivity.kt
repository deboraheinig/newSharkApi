package com.example.shark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val sharkApi = RetrofitHelper.getInstance().create(SharkService::class.java)
    // Views
    private lateinit var tvGames: TextView
    private lateinit var tvResults: TextView
    private lateinit var etSearch: TextInputEditText
    private lateinit var fabSearch: FloatingActionButton
    private lateinit var rvGames: RecyclerView
    private lateinit var etMin: TextInputEditText
    private lateinit var etMax: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initViews()

        rvGames.layoutManager = LinearLayoutManager(this)

        etSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                getDeals(etSearch.text.toString(), etMin.text.toString().toDoubleOrNull(), etMax.text.toString().toDoubleOrNull())
                return@OnKeyListener true
            }
            false
        })

        fabSearch.setOnClickListener {
            tvResults.visibility = View.VISIBLE
            tvGames.text = etSearch.text.toString()
            getDeals(etSearch.text.toString(), etMin.text.toString().toDoubleOrNull(), etMax.text.toString().toDoubleOrNull())

        }
        getDeals(lowerPrice = 0.0, upperPrice = 0.0)
    }

    private fun initViews() {
        rvGames = findViewById(R.id.recyclerView)
        etSearch = findViewById(R.id.textEdit)
        tvGames = findViewById(R.id.joguinhos)
        tvResults = findViewById(R.id.resultados)
        fabSearch = findViewById(R.id.button)
        etMin = findViewById(R.id.valorMin)
        etMax = findViewById(R.id.valorMax)
    }

    private fun getDeals(title: String? = null, lowerPrice: Double? = null, upperPrice: Double? = null) {
        GlobalScope.launch(Dispatchers.IO) {
            val result  = sharkApi.getDeals(title, lowerPrice, upperPrice)

            runOnUiThread {
                rvGames.adapter = SharkAdapter(result)
                Log.d("http", result.toString())
            }
        }
    }
}

// lob u >3<
