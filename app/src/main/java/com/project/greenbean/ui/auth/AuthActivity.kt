package com.project.greenbean.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.project.greenbean.R
import com.project.greenbean.data.Result
import com.project.greenbean.databinding.ActivityAuthBinding
import com.project.greenbean.ui.MainActivity
import com.project.greenbean.ui.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAuthBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        getSession()
    }

    private fun getSession() {
        viewModel.getSession().observe(this) {
            Log.d("AuthActivity", it)
            if (it != "") {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                setUpAction()
            }
        }
    }

    private fun setUpAction() {

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            observeLogin(email, password)
        }
    }

    private fun observeLogin(email: String, password: String) {
        viewModel.login(email, password).observe(this) {
            when(it) {
                is Result.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.buttonLogin.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    AlertDialog.Builder(this@AuthActivity).apply {
                        setTitle("Login Success")
                        setMessage("Welcome to CoffeGit!")
                        create()
                        show()
                    }
                    saveUser(it.data)
                }
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    binding.buttonLogin.isEnabled = true
                    Log.d("LoginFragment", it.error)
                    android.app.AlertDialog.Builder(this).apply {
                        setTitle("Login")
                        setMessage(it.error)
                        setPositiveButton("OK") { _, _ ->
                            clearForm()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.edLoginEmail.text?.clear()
        binding.edLoginPassword.text?.clear()
    }

    private fun saveUser(token: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(1000)
                viewModel.saveSession(token)
                onDestroy()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getSession()
    }
}