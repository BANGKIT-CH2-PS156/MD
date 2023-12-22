package com.project.greenbean.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.project.greenbean.R
import com.project.greenbean.data.Result
import com.project.greenbean.databinding.ActivityUpdateProfileBinding
import com.project.greenbean.ui.ViewModelFactory
import com.project.greenbean.ui.auth.AuthActivity

class Update_Profile : AppCompatActivity() {

    private var _binding: ActivityUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpAction()
        observeUser()
    }

    private fun setUpAction() {
        binding.btnSave.setOnClickListener {
            val name = binding.edUpdateName.text.toString()
            val email = binding.emailuserUpdate.text.toString()
            val address = binding.edUpdateAddress.text.toString()
            val phone = binding.edUpdatePhone.text.toString()

            observeUpdate(name, email, address, phone)
        }

    }

    private fun observeUser() {
        viewModel.getUser().observe(this) {
            when(it) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    binding.edUpdateName.setText(it.data.data?.name)
                    binding.emailuserUpdate.text = it.data.data?.email
                    binding.edUpdateJob.setText(it.data.data?.job)
                    binding.edUpdateAddress.setText(it.data.data?.address)
                    binding.edUpdatePhone.setText(it.data.data?.phone)

                    Glide.with(this)
                        .load(it.data.data?.img?.trimEnd())
                        .into(binding.profileUpdate)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun observeUpdate(
        name: String,
        email: String,
        address: String,
        phone: String,
    ) {
        viewModel.updateProfile(name, email, address, phone).observe(this) {
            when(it) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    Log.d("Update Profile", it.error)
                }

            }
        }
    }
}