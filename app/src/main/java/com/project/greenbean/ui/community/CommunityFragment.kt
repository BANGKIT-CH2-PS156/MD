package com.project.greenbean.ui.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.greenbean.adapter.PostAdapter
import com.project.greenbean.data.Result
import com.project.greenbean.databinding.FragmentCommunityBinding
import com.project.greenbean.ui.ViewModelFactory

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CommunityViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePosting()

        binding.fabAddPost.setOnClickListener {
            val action = Intent(requireContext(), Upload_Story::class.java)
            startActivity(action)
        }
    }

    private fun observePosting() {
        viewModel.getAllPosting().observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val adapter = PostAdapter()
                    adapter.differ.submitList(it.data.posting)
                    binding.rvCommunity.adapter = adapter
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("observePosting", it.error)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        observePosting()
    }
}