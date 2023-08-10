package com.example.androidtesting.ui.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.androidtesting.R
import com.example.androidtesting.util.collectWhenOwnerStarted
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {

    val viewModel by viewModels<AddShoppingItemViewModel>()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(
            SET_IMAGE
        ) { _, result ->
            result.getString(IMAGE_URL)?.let { imageUrl ->
                viewModel.onEvent(AddShoppingItemUiEvent.SetImageUrl(imageUrl))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.add_shopping_item)

        view.findViewById<View>(R.id.btnAddShoppingItem).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.etShoppingItemName).text.toString()
            val amount = view.findViewById<EditText>(R.id.etShoppingItemAmount).text.toString()
            val price = view.findViewById<EditText>(R.id.etShoppingItemPrice).text.toString()
            closeKeyboard()
            viewModel.onEvent(AddShoppingItemUiEvent.AddItem(name, amount, price))
        }

        view.findViewById<View>(R.id.ivShoppingImage).setOnClickListener {
            openImagePicker()
        }

        viewModel.uiState.collectWhenOwnerStarted(
            viewLifecycleOwner
        ) {
            it.errorMessage?.let { errorMessage ->
                clearAllError()
                showError(errorMessage)
            }
            it.shoppingItem?.let {
                clearAllError()
                Snackbar.make(
                    requireView(),
                    "add item successful",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    show()
                }
                findNavController().popBackStack()
            }
        }

        viewModel.imageUrlState.collectWhenOwnerStarted(
            viewLifecycleOwner
        ) {
            it?.let {
                requestManager.load(it).into(view.findViewById(R.id.ivShoppingImage))
            }
        }
    }

    private fun closeKeyboard() {
        val view = this.requireActivity().currentFocus
        view?.let {
            val manager: InputMethodManager? = this.requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager?
            manager?.hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
    fun openImagePicker() {
        findNavController().navigate(
            R.id.action_addShoppingItemFragment_to_imagePickFragment
        )
    }

    fun clearAllError() {
        requireView().rootView.findViewById<TextInputLayout>(R.id.itemNameInputLayout).error = null
        requireView().rootView.findViewById<TextInputLayout>(R.id.priceInputLayout).error = null
        requireView().rootView.findViewById<TextInputLayout>(R.id.amountInputLayout).error = null
    }

    private fun showError(error: String) {
        when (error) {
            AddShoppingItemViewModel.INVALID_IMAGE_URL -> {
                Snackbar.make(
                    requireView(),
                    "Please add the image, click YES to add",
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction("YES") {
                        openImagePicker()
                    }
                    show()
                }
            }

            AddShoppingItemViewModel.INVALID_NAME -> {
                val inputLayout =
                    requireView().rootView.findViewById<TextInputLayout>(R.id.itemNameInputLayout)
                inputLayout.error = "Invalid name"

            }

            AddShoppingItemViewModel.INVALID_PRICE -> {
                val inputLayout =
                    requireView().rootView.findViewById<TextInputLayout>(R.id.priceInputLayout)
                inputLayout.error = "Invalid price"
            }

            AddShoppingItemViewModel.INVALID_AMOUNT -> {
                val inputLayout =
                    requireView().rootView.findViewById<TextInputLayout>(R.id.amountInputLayout)
                inputLayout.error = "Invalid amount"
            }
        }
    }

    companion object {
        const val SET_IMAGE = "set-image"
        const val IMAGE_URL = "image-url"
    }


}