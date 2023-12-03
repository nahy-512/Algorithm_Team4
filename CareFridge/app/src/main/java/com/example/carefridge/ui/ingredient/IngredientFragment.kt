package com.example.carefridge.ui.ingredient

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.carefridge.R
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.FragmentIngredientBinding
import com.example.carefridge.ui.ingredient.adapter.IngredientRVAdapter
import com.quintable.jpower.config.BaseFragment
import java.time.LocalDateTime
import java.time.ZoneId

class IngredientFragment : BaseFragment<FragmentIngredientBinding>(FragmentIngredientBinding::bind, R.layout.fragment_ingredient) {

    private lateinit var db: FridgeDatabase

    private var ingredients = arrayListOf<Ingredient>()

    private var isItemDecorationAdded = false // 간격을 초기에만 설정해주기 위함

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FridgeDatabase.getInstance(requireContext())!!
    }

    override fun onStart() {
        super.onStart()

        inputDummyIngredients()
        initIngredientRV()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inputDummyIngredients() {
        // DB의 재료 정보를 불러옴
        ingredients = db.ingredientDao().getIngredients() as ArrayList<Ingredient>

        if (ingredients.isNotEmpty())
            return
        else {
            // 재료 데이터가 없을 때의 처리
            Thread{
                // ingredients가 비어있다면 더미데이터를 넣어줌
                db.ingredientDao().apply {
                    insert(Ingredient("고기", 500, getExpirationDateAfterDays(5), false))
                    insert(Ingredient("채소", 400, getExpirationDateAfterDays(3)))
                    insert(Ingredient("밥", 500, getExpirationDateAfterDays(10), true))
                    insert(Ingredient("면", 500, getExpirationDateAfterDays(11), isPrefer = false))
                    insert(Ingredient("빵", 400, getExpirationDateAfterDays(2), isPrefer = false))
                    insert(Ingredient("소고기", 200, getExpirationDateAfterDays(3)))
                }
                // 추가했다면 다시 데이터를 ingredients에 넣어줌
                ingredients = db.ingredientDao().getIngredients() as ArrayList<Ingredient>

                // 데이터가 잘 들어왔는지 확인
                val _ingredients = db.ingredientDao().getIngredientsLiveData()
                Log.d("DB ingredients data", _ingredients.toString())
            }.start()
        }
    }

    private fun initIngredientRV() {

        // 어댑터와 데이터 리스트 연결
        val adapter = IngredientRVAdapter(requireContext(), ingredients)
        // 리사이클러뷰에 어댑터 연결
        binding.ingredientRv.adapter = adapter
        // 레이아웃 매니저 설정
        if (!isItemDecorationAdded) {
            binding.ingredientRv.run {
                val spanCount = 2
                val space = 20 // 20dp로 간격 지정
                addItemDecoration(GridSpaceItemDecoration(spanCount, space))
            }
            isItemDecorationAdded = true // 변수 업데이트
        }

        db.ingredientDao().getIngredientsLiveData().observe(viewLifecycleOwner, Observer { ingredients ->
            if (ingredients.isNotEmpty()) {
                adapter.updateAlbums(ingredients)
            }
        })

        adapter.setMyItemClickListener(object : IngredientRVAdapter.MyItemClickListener {
            override fun onItemClick(ingredient: Ingredient, position: Int) {
                Log.d("INGREDIENT-FRAG", "재료 아이템 클릭!")
                Log.e("SET-INGREDIENT", "$ingredient , $position")

                // 편집 바텀시트 띄우기
                showEditBottomSheetDialog(ingredient)
            }
        })
    }

    private fun showEditBottomSheetDialog(ingredient: Ingredient){
        Log.d("ShowDialog", "success")
        val dialog = EditIngredientBottomSheetDialog(ingredient)

        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExpirationDateAfterDays(daysToAdd: Long): Long {
        val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val expirationDateTime = currentDateTime.plusDays(daysToAdd)
        // 현재 날짜를 기준으로 인자로 들어온 daysToAdd를 더한 유통기한을 반환
        return expirationDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}