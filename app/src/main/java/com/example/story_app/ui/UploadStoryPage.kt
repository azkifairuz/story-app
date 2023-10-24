package com.example.story_app.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.story_app.R
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.local.uriToFile
import com.example.story_app.databinding.FragmentUploadStoryPageBinding
import com.example.story_app.ui.CameraActivity.Companion.CAMERAX_RESULT
import com.example.story_app.viewmodel.AuthViewmodel
import com.example.story_app.viewmodel.UploadStoryViewmodel
import java.io.File

class UploadStoryPage : Fragment() {
    private lateinit var binding: FragmentUploadStoryPageBinding
    private var currentImageUri: Uri? = null
    private val viewModel: UploadStoryViewmodel by viewModels()
    private var imageVal: File? = null
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG)
                    .show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadStoryPageBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = AuthPreference(requireContext())
        val token = pref.getUser().token
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraXButton.setOnClickListener { startCameraX() }

        viewModel.isLoading.observe(requireActivity()){isLoading->
            showLoading(isLoading)
        }

        viewModel.responseBody.observe(requireActivity()){response->
            if (response.error) {
                Toast.makeText(requireContext(), getString(R.string.request_time_out), Toast.LENGTH_SHORT).show()
            } else {
                requireActivity().setResult(Activity.RESULT_OK)
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                val storyFragment = StoryPage()
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(
                        R.id.frame_container,
                        storyFragment,
                        StoryPage::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }

            }
        }
        binding.uploadButton.setOnClickListener {
           uploadImage(token)
        }


    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCameraX() {
        val cameraIntent = Intent(requireContext(), CameraActivity::class.java)
        cameraActivityResultLauncher.launch(cameraIntent)
    }

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
          if (result.resultCode == CAMERAX_RESULT) {
              currentImageUri =
                  result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
              showImage()
          }
        }

    private fun showImage() {
        currentImageUri?.let {
            imageVal = uriToFile(it,requireContext())
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun uploadImage(token:String) {
        viewModel.postStory(
            token,
            imageVal as File,
            binding.etDesc.editText?.text.toString(),
        )
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}