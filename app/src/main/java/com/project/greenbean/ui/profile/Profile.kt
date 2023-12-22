package com.project.greenbean.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.project.greenbean.R
import com.project.greenbean.data.Result
import com.project.greenbean.databinding.ActivityProfileBinding
import com.project.greenbean.ui.ViewModelFactory
import com.project.greenbean.ui.auth.AuthActivity

class Profile : AppCompatActivity() {
    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUpAction()
        observeUser()

    }

    private fun setUpAction() {
        binding.changeBtn.setOnClickListener {
            val action = Intent(this, Update_Profile::class.java)
            startActivity(action)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun observeUser() {
        viewModel.getUser().observe(this) {
            when(it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.username.text = it.data.data?.name
                    binding.emailuser.text = it.data.data?.email
                    binding.jobPetani.text = it.data.data?.job
                    binding.addressPetani.text = it.data.data?.address
                    binding.numberPetani.text = it.data.data?.phone

                    Glide.with(this)
                        .load(it.data.data?.img?.trimEnd())
                        .into(binding.ivProfile)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}