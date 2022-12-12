package com.example.androidtesting.ui.picker

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtesting.R
import com.example.androidtesting.ui.add.AddShoppingItemFragment
import com.example.androidtesting.util.collectWhenOwnerStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {

    val viewModel by viewModels<ImagePickViewModel>()
    @Inject
    lateinit var imageAdapter: ImageListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<EditText>(R.id.etSearch).addTextChangedListener { it ->
            val index = it?.toString()
            index?.let { query ->
                viewModel.onEvent(ImageListUiEvent.Search(query))
            }
        }

        viewModel.uiState.collectWhenOwnerStarted(
            viewLifecycleOwner
        ) {
            view.findViewById<View>(R.id.progressBar).isVisible = it.isLoading
            imageAdapter.images = it.imageUrls
            it.errorMessage?.let { error ->
                Toast.makeText(this.requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        val recyclerView : RecyclerView = view.findViewById(R.id.rvImages)
        recyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }

        imageAdapter.setOnItemClickListener { imageUrl ->
            setFragmentResult(
                AddShoppingItemFragment.SET_IMAGE, bundleOf(
                    AddShoppingItemFragment.IMAGE_URL to imageUrl
                )
            )
            findNavController().popBackStack()
        }

    }
    companion object{
        const val GRID_SPAN_COUNT = 4
    }
}