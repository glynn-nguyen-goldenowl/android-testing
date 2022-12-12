package com.example.androidtesting.ui.shopping

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidtesting.R

class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.fabAddShoppingItem).setOnClickListener {
            addShoppingItem()
        }
    }
    private fun addShoppingItem(){
        findNavController().navigate(
            R.id.action_shoppingFragment_to_addShoppingItemFragment
        )
    }
}