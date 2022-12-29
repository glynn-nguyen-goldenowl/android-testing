package com.example.androidtesting.ui.shopping

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtesting.R
import com.example.androidtesting.util.collectWhenOwnerStarted
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    val viewModel by viewModels<ShoppingViewModel>()

    @VisibleForTesting
    private var snackBar : Snackbar? = null

    @Inject
    lateinit var shoppingItemAdapter: ShoppingItemAdapter

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel.onEvent(ShoppingUiEvent.DeleteItem(item))
            snackBar = Snackbar.make(requireView(), "Successfully deleted item", 2000).apply {
                setAction("Undo") {
                    viewModel.onEvent(ShoppingUiEvent.InsertItem(item))
                }
                show()
            }
        }
    }

    @VisibleForTesting
    fun performSnackBarAction(){
        val actionView = snackBar?.view?.findViewById<View>(com.google.android.material.R.id.snackbar_action)
        actionView?.performClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.fabAddShoppingItem).setOnClickListener {
            addShoppingItem()
        }
        view.findViewById<Button>(R.id.consumeError).setOnClickListener {
            viewModel.onEvent(ShoppingUiEvent.ConsumeError("Error"))
        }
        setupRecyclerView(view.findViewById(R.id.rvShoppingItems))

        viewModel.uiState.collectWhenOwnerStarted(
            viewLifecycleOwner
        ) {
            view.findViewById<TextView>(R.id.tvShoppingTotalPrice).text = "Total Price: " +   it.totalPrice.toString()
            view.findViewById<ProgressBar>(R.id.progressBar).isVisible = it.loading
            view.findViewById<TextView>(R.id.errorMessage).text = it.errorMessage ?: ""
            view.findViewById<Button>(R.id.consumeError).isVisible = (it.errorMessage != null)
            shoppingItemAdapter.shoppingItems = it.shoppingItemList
        }
    }
    private fun addShoppingItem(){
        findNavController().navigate(
            R.id.action_shoppingFragment_to_addShoppingItemFragment
        )
    }

    private fun setupRecyclerView(rvShoppingItems: RecyclerView) {
        rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}