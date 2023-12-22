package com.project.greenbean.ui.verifikasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.greenbean.data.Result
import com.project.greenbean.data.response.PredictResponse
import com.project.greenbean.data.response.Prediction
import com.project.greenbean.databinding.ActivityVerifikasiBinding
import com.project.greenbean.ui.ViewModelFactory
import com.project.greenbean.ui.result.ResultScanning
import com.project.greenbean.util.getImageUri
import com.project.greenbean.util.uriToFile

class Verifikasi : Fragment() {

    private var _binding: ActivityVerifikasiBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels <VerifikasiViewModel>  {
        ViewModelFactory.getInstance(requireContext())
    }

    private var currentImageUri: Uri? = null
    var predictedData = PredictResponse()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityVerifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            startCamera()
        }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.checkbtn.setOnClickListener {
            predictImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
            Toast.makeText(requireContext(), "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            Toast.makeText(requireContext(), "Try Again / No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun predictImage() {
        currentImageUri?.let { uri ->
            val prediction = uriToFile(uri, requireContext())

            viewModel.predictImage(prediction).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        // Handle loading state if needed
                    }
                    is Result.Success -> {
                        val predictedData = result.data
                        Toast.makeText(requireContext(), "Prediction successful: $predictedData", Toast.LENGTH_SHORT).show()
                        navigateToResultScanningActivity(predictedData)
                    }
                    is Result.Error -> {
                        val errorMessage = result.error
                        Toast.makeText(requireContext(), "Prediction failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun navigateToResultScanningActivity(predictedData: PredictResponse) {
        val intent = Intent(requireContext(), ResultScanning::class.java).also {
            it.putExtra("predict", predictedData)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

