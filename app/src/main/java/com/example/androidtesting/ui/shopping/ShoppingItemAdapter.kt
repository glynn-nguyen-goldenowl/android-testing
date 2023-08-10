package com.example.androidtesting.ui.shopping


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidtesting.R
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.util.CurrencyFormat
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glideRequestManager: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    var incrementAmountClicked: ((ShoppingItem) -> Unit)? = null
    var decrementAmountClicked: ((ShoppingItem) -> Unit)? = null

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.itemView.apply {
            glideRequestManager.load(shoppingItem.imageUrl).into(this.findViewById(R.id.ivShoppingImage))

            this.findViewById<TextView>(R.id.tvName).text = shoppingItem.name

            val amountText = "${shoppingItem.amount}"
            this.findViewById<TextView>(R.id.tvShoppingItemAmount).text = amountText

            this.findViewById<TextView>(R.id.tvShoppingItemPrice).text = CurrencyFormat.format(shoppingItem.price)

            val incrementCountButton = this.findViewById<AppCompatImageButton>(R.id.btnIncrementCount)
            incrementCountButton.setOnClickListener {
                incrementAmountClicked?.invoke(shoppingItem)
            }

            val decrementCountButton = this.findViewById<AppCompatImageButton>(R.id.btnDecrementCount)
            decrementCountButton.isEnabled = shoppingItem.amount > 0
            decrementCountButton.setOnClickListener {
                decrementAmountClicked?.invoke(shoppingItem)
            }


        }
    }
}