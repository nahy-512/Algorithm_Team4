package com.example.carefridge.ui.ingredient.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carefridge.R
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.ItemIngredientBinding

class IngredientRVAdapter(
    val context: Context,
    private val ingredientList: ArrayList<Ingredient>
):  RecyclerView.Adapter<IngredientRVAdapter.ViewHolder>(){

    private lateinit var mItemClickListener: MyItemClickListener

    interface MyItemClickListener {
        fun onItemClick(ingredient: Ingredient, position: Int)
    }

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun updateAlbums(newList: List<Ingredient>) {
        ingredientList.clear()
        ingredientList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemIngredientBinding = ItemIngredientBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientList[position])
        holder.apply {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(ingredientList[position], position)
            }
        }
    }

    override fun getItemCount(): Int = ingredientList.size

    inner class ViewHolder(private val binding: ItemIngredientBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Ingredient) {
            binding.itemIngredientNameTv.text = item.name
            binding.itemIngredientAmountTv.text = context.getString(R.string.item_ingredient_amount_tv, item.amount)
//            binding.itemIngredientAmountTv.text = item.amount.toString() + "g"
        }
    }

}