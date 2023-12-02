package com.example.carefridge.ui.ingredient

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.carefridge.R
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.FragmentIngredientBinding
import com.example.carefridge.ui.ingredient.adapter.IngredientRVAdapter
import com.quintable.jpower.config.BaseFragment

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
                    insert(Ingredient("고기", 500))
                    insert(Ingredient("채소", 400))
                    insert(Ingredient("밥", 500))
                    insert(Ingredient("면", 500, isPrefer = true))
                    insert(Ingredient("빵", 400, isPrefer = true))
                    insert(Ingredient("소고기", 200))
                }
                // 추가했다면 다시 데이터를 ingredients에 넣어줌
                ingredients = db.ingredientDao().getIngredients() as ArrayList<Ingredient>

                // 데이터가 잘 들어왔는지 확인
                val _albums = db.ingredientDao().getIngredientsLiveData()
                Log.d("DB data", _albums.toString())
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
}