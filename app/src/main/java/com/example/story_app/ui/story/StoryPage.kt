package com.example.story_app.ui.story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.story_app.R
import com.example.story_app.adapter.StoryAdapter
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.databinding.FragmentStoryBinding
import com.example.story_app.ui.WelcomePage
import com.example.story_app.ui.story.detail.DetailStoryPage
import com.example.story_app.ui.story.uploadStory.UploadStoryPage


class StoryPage : Fragment(), StoryAdapter.ToDetailCallback {
    private lateinit var binding: FragmentStoryBinding
    private lateinit var storyRv: RecyclerView
    private lateinit var arrayList: ArrayList<ListStoryItem>
    private val viewModel: StoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyRv = binding.rvStory
        arrayList = ArrayList()

        val adapter = StoryAdapter(arrayList)
        adapter.setToDetailCallback(this)

        storyRv.adapter = adapter
        storyRv.layoutManager = LinearLayoutManager(requireContext())

        val pref = AuthPreference(requireContext())
        val token = pref.getUser().token

        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.listStory.observe(requireActivity()) { listStory ->
            arrayList.clear()
            arrayList.addAll(listStory)
            storyRv.adapter?.notifyDataSetChanged()
        }

        viewModel.getListStory(token)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.logout -> {
                    pref.logout()
                    val welcomePage = WelcomePage()
                    val fragmentManager = parentFragmentManager
                    fragmentManager.beginTransaction().apply {
                        replace(
                            R.id.frame_container,
                            welcomePage,
                            WelcomePage::class.java.simpleName
                        )
                        addToBackStack(null)
                        commit()
                    }
                    true
                }

                else -> false
            }
        }

        binding.fab.setOnClickListener {
            val uploadStoryFragment = UploadStoryPage()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    uploadStoryFragment,
                    UploadStoryPage::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }


    override fun onItemClicked(
        story: ListStoryItem,
        imgStory: ImageView,
        storyTitle: TextView,
        storyDesc: TextView
    ) {
        val bundle = Bundle()
        bundle.putString(DetailStoryPage.EXTRA_ID, story.id)
        val detailFragment = DetailStoryPage()
        detailFragment.arguments = bundle

        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().apply {
            addSharedElement(imgStory, "image")
            addSharedElement(storyTitle, "title")
            addSharedElement(storyDesc, "desc")
            replace(
                R.id.frame_container,
                detailFragment,
                DetailStoryPage::class.java.simpleName,
            )
            addToBackStack(null)
            commitAllowingStateLoss()
        }

        imgStory.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.text_confirm_dialog))
            setMessage(getString(R.string.exit_confirmation_message))
            setPositiveButton(getString(R.string.text_yes)) { _, _ ->
                requireActivity().finishAffinity()
            }
            setNegativeButton(getString(R.string.text_no)) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

}