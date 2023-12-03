package com.example.carefridge.ui.home

import android.content.res.TypedArray
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.carefridge.R
import com.example.carefridge.algorithm.MenuRecommendAlgorithm
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.data.entities.Recipe
import com.example.carefridge.databinding.DialogRecommendMenuBinding


class RecommendMenuDialog : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogRecommendMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FridgeDatabase

    private lateinit var menuImgList: TypedArray
    private lateinit var menuNameList: Array<String>

    private var recipes = arrayListOf<Recipe>()

    private var currentMenuIndex = 0
    private val handler = Handler()
    private val delay = 200L // 0.2초

    // 추천 결과
    private var recommendMenu: String? = null
    private var recommendMenuId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRecommendMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        db = FridgeDatabase.getInstance(requireContext())!!

        setInit()
        initClickListener()

        return view
    }

    override fun onStart() {
        super.onStart()

        // DB에서 레시피 조회하기
        getRecipes()
        // 조회 결과가 나올 때까지 이미지 바꾸기
        changeMenuImg()
        // 메뉴 추천 진행
        getRecommendMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Fragment가 종료될 때 handler의 작업을 제거
        handler.removeCallbacksAndMessages(null)
    }

    private fun setInit() {
        menuImgList = requireContext().resources.obtainTypedArray(R.array.menu_img_arr)
        menuNameList = requireContext().resources.getStringArray(R.array.menu_name_arr)
    }

    private fun initClickListener() {

        // 닫기
        binding.dialogRecommendMenuCloseIv.setOnClickListener {
            dismiss()
        }

        // 다시 추천 받기
        binding.dialogRecommendMenuAgainBtn.setOnClickListener {
            //TODO: 메뉴 추천 재진행
            dismiss()
        }

        // 만들어 먹기
        binding.dialogRecommendMenuTryBtn.setOnClickListener {
            //TODO: 추천 받은 메뉴 비활성화
            dismiss()
        }
    }

    private fun getRecommendMenu() {
        recommendMenu = MenuRecommendAlgorithm.main(getIngredients(), recipes, getUserPreferIngredients())
        // 메뉴 세팅
        recommendMenuId = findRecipeIdByName(recommendMenu)
    }

    private fun findRecipeIdByName(menuName: String?): Int? {
        return recipes.find { it.name == menuName }?.id
    }

    private fun setRecommendationResult() {
        // 추천 결과를 받은 후에는 애니메이션을 멈추고, 추천된 메뉴의 이미지와 이름으로 업데이트
        handler.removeCallbacksAndMessages(null)

        // 추천 결과가 없으면 처리 종료
        if (recommendMenuId == null) return

        val recommendedRecipe = db.recipeDao().getRecipeById(recommendMenuId!!)
        recommendedRecipe?.let {
            // 이미지와 이름을 업데이트
            binding.dialogRecommendMenuIv.setImageResource(menuImgList.getResourceId(currentMenuIndex, -1))
            binding.dialogRecommendMenuNameTv.text = it.name
        }
    }

    private fun getIngredients(): List<Ingredient> {
        return db.ingredientDao().getIngredients()
    }

    private fun getRecipes() {
        recipes = db.recipeDao().getRecipes() as ArrayList<Recipe>
    }

    private fun getUserPreferIngredients(): List<String> {
        // 사용자가 선호하는 재료 목록을 가져옴
        val preferIngredients = db.ingredientDao().getUserPreferIngredients(true)
        // Ingredient 객체에서 name 필드만 추출하여 List로 반환
        return preferIngredients.map { it.name }
    }

    private fun changeMenuImg() {
        // 일정 간격으로 이미지를 교체하는 Runnable을 실행
        handler.postDelayed(object : Runnable {
            override fun run() {
                changeMenuWithAnimation(binding.dialogRecommendMenuIv, binding.dialogRecommendMenuNameTv)
                handler.postDelayed(this, delay)
            }
        }, delay)
    }


    private fun changeMenuWithAnimation(menuIv: ImageView, menuTv: TextView) {
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        menuIv.startAnimation(fadeOutAnimation)
        menuTv.startAnimation(fadeOutAnimation)

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                currentMenuIndex = (currentMenuIndex + 1) % menuNameList.size
                // 이미지 교체
                menuIv.setImageResource(menuImgList.getResourceId(currentMenuIndex, -1))
                menuIv.startAnimation(fadeInAnimation)
                // 텍스트 교체
                menuTv.text = menuNameList[currentMenuIndex]
                menuIv.startAnimation(fadeInAnimation)

                // 추천 결과가 있으면 애니메이션을 멈추고 결과를 업데이트
                if (recommendMenu != null) {
                    setRecommendationResult()
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}