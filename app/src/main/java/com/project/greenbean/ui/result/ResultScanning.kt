package com.project.greenbean.ui.result

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.project.greenbean.data.response.PredictResponse
import com.project.greenbean.databinding.ActivityResultScanningBinding

class ResultScanning : AppCompatActivity() {

    private lateinit var binding: ActivityResultScanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("predict", PredictResponse::class.java)
        } else {
            intent.getParcelableExtra("predict")
        }

        data?.let {
            with(binding) {
                result.text = it.data?.info
                Glide.with(this@ResultScanning).load(it.data?.image).into(imgResult)
            }
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}