package com.example.carefridge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.example.carefridge.R
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Recipe
import com.example.carefridge.databinding.FragmentHomeBinding
import com.quintable.jpower.config.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecommendMenuDialog()
    }

    override fun onStart() {
        super.onStart()

        setBlinkAnimation()
        setInitialRecipes()
    }

    private fun setBlinkAnimation() {
        // blink 애니메이션
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        // 애니메이션 재생
        binding.homeRecommendBtn.startAnimation(anim)
    }

    private fun showRecommendMenuDialog() {
        binding.homeRecommendBtn.setOnClickListener {
            val dialog = RecommendMenuDialog()
            // 알림창이 띄워져있는 동안 배경 클릭 허용
            dialog.isCancelable = true
            dialog.show(this.requireFragmentManager(), "AddDialog")
        }
    }

    private fun setInitialRecipes() {
        val db = context?.let { FridgeDatabase.getInstance(it) }!!

        // DB의 레시피 정보를 불러옴
        val recipes = db.recipeDao().getRecipes() as ArrayList<Recipe>

        if (recipes.isNotEmpty())
            return
        else {
            // 재료 데이터가 없을 때의 처리
            Thread{
                // recipes가 비어있다면 더미데이터를 넣어줌
                db.recipeDao().apply {
                    insert(Recipe(0, "스파게티", listOf("면" to 100, "고기" to 100, "채소" to 100, "소스" to 50)))
                    insert(Recipe(0, "비빔밥", listOf("밥" to 100, "고기" to 100, "채소" to 100, "소스" to 50)))
                    insert(Recipe(0, "짜장면", listOf("면" to 150, "고기" to 50, "채소" to 50, "소스" to 30)))
                    insert(Recipe(0, "스테이크", listOf("소고기" to 200, "채소" to 50, "소스" to 30)))
                    insert(Recipe(0, "샌드위치", listOf("빵" to 100, "고기" to 50, "채소" to 80)))
                }

                // 데이터가 잘 들어왔는지 확인
                val _recipes = db.recipeDao().getRecipes()
                Log.d("DB recipes data", _recipes.toString())
            }.start()
        }
    }
}