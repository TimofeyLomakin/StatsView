package com.example.statsview.ui

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.statsview.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<StatsView>(R.id.stats)
        view.data = listOf(
            500F,
            500F,
            500F,
            500F
        )

        val animator = ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 1200
            interpolator = android.view.animation.LinearInterpolator()
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                view.updateAnimationProgress(progress)
            }
        }

        animator.start()
    }
}