package com.example.androidtesting.ui.shopping

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtesting.R
import com.example.androidtesting.util.CurrencyFormat
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

        requireActivity().title = getString(R.string.shopping_item_list)

        view.findViewById<View>(R.id.fabAddShoppingItem).setOnClickListener {
            addShoppingItem()
        }

        setupRecyclerView(view.findViewById(R.id.rvShoppingItems))

        viewModel.uiState.collectWhenOwnerStarted(
            viewLifecycleOwner
        ) {
            val totalPriceView = view.findViewById<TextView>(R.id.tvShoppingTotalPrice)
            totalPriceView.text = getString(R.string.total_price_formatted, CurrencyFormat.format(it.totalPrice))
            view.findViewById<ProgressBar>(R.id.progressBar).isVisible = it.loading
            totalPriceView.isVisible = it.shoppingItemList.isNotEmpty()
            view.findViewById<TextView>(R.id.lbEmptyItem).isVisible = it.shoppingItemList.isEmpty()
            shoppingItemAdapter.shoppingItems = it.shoppingItemList
        }

        shoppingItemAdapter.decrementAmountClicked = { item ->
            viewModel.onEvent(ShoppingUiEvent.DecrementAmountItem(item))
        }

        shoppingItemAdapter.incrementAmountClicked = { item ->
            viewModel.onEvent(ShoppingUiEvent.IncrementAmountItem(item))
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
            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
            this.addItemDecoration(dividerItemDecoration)
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}