package com.project.greenbean.ui.auth

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.project.greenbean.R
import com.project.greenbean.data.Result
import com.project.greenbean.databinding.ActivityRegisterBinding
import com.project.greenbean.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val viewModel by viewModels<AuthViewModel>() { ViewModelFactory.getInstance(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edRegisterPassword2.text.toString()

            register(email, password, confirmPassword)
        }
    }

    private fun register(
        email: String,
        password: String,
        confirmPassword: String
    ) {


        viewModel.register(email, password, confirmPassword).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.btnRegister.isEnabled = false
                    binding.linearProgressIndicator.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.btnRegister.isEnabled = true
                    binding.linearProgressIndicator.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Register")
                        setMessage(result.data.message.toString())
                        setPositiveButton(getString(R.string.continue_login)) { _, _ ->
                            val intent = Intent(this@RegisterActivity, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    binding.btnRegister.isEnabled = true
                    binding.linearProgressIndicator.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}