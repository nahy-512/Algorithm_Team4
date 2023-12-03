package com.example.carefridge.ui.ingredient.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.carefridge.R
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.ItemIngredientBinding
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.abs

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

        @SuppressLint("StringFormatMatches")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item : Ingredient) {
            binding.itemIngredientNameTv.text = item.name
            binding.itemIngredientAmountTv.text = context.getString(R.string.item_ingredient_amount_tv, item.amount)
            binding.itemIngredientExpireDateTv.text = item.getFormattedExpirationDate()

            val dDay = calculateDDay(item.expirationDate)

            if (dDay == 0) { // 디데이
                binding.itemIngredientDDayTv.text = context.getString(R.string.item_ingredient_dDay)
                binding.itemIngredientDDayTv.setTextColor(ContextCompat.getColor(context, R.color.blue))
            } else if (dDay < 0) { // 유통기한 지남
                binding.itemIngredientDDayTv.text = context.getString(R.string.item_ingredient_dDay_tv, "+", abs(dDay)) // 양수로 일수 변환
                binding.itemIngredientDDayTv.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            else {
                binding.itemIngredientDDayTv.text = context.getString(R.string.item_ingredient_dDay_tv, "-", dDay)
                binding.itemIngredientDDayTv.setTextColor(Color.parseColor("#8C8C8C"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateDDay(expirationDateTimeMillis: Long): Int {
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        // 현재 시간
        val currentDateTime = LocalDateTime.now(koreaZoneId)
        // 유통기한 시간
        val expirationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(expirationDateTimeMillis), koreaZoneId)
        // 날짜 기준으로 디데이 계산
        val duration = Duration.between(currentDateTime, expirationDateTime)
        // 일(day)로 변환
        val dDay = duration.toDays().toInt()

        return dDay
    }
}