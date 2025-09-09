package com.example.statsview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.statsview.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<StatsView>(R.id.stats).data = listOf(
            500F,
            500F,
            500F,
            500F
        )
    }
}