package com.project.greenbean.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.project.greenbean.R
import com.project.greenbean.data.Result
import com.project.greenbean.databinding.FragmentHomeBinding
import com.project.greenbean.di.Injection
import com.project.greenbean.ui.ViewModelFactory
import com.project.greenbean.ui.profile.Profile

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> { ViewModelFactory.getInstance(requireActivity())}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
        Log.d("HomeFragment", "onCreate: Halo")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment3", "onCreate: Halo")

        observeUser()
        setUpAction()
    }

    private fun setUpAction() {
        binding.imgProfile.setOnClickListener {
            val action = Intent(requireContext(), Profile::class.java)
            startActivity(action)
        }
    }

    private fun observeUser() {
        viewModel.getUser().observe(viewLifecycleOwner) {user ->
            when(user) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val userImg = user.data.data?.img?.trimEnd()
                    Log.d("HomeFragment", userImg.toString())
                    Glide.with(requireContext())
                        .load(userImg)
                        .into(binding.imgProfile)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), user.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeUser()
        setUpAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}