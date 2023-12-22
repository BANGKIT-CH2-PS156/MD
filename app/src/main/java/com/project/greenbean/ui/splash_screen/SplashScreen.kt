package com.project.greenbean.ui.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.project.greenbean.databinding.ActivitySplashScreenBinding
import com.project.greenbean.ui.auth.AuthActivity

class SplashScreen : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.splashScreen.alpha = 0f
        binding.splashScreen.animate().apply {
            duration = 1500
            alpha(1f)
        }.withEndAction {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}