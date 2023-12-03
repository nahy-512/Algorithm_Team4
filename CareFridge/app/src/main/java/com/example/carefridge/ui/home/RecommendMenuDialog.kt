package com.example.carefridge.ui.home

import android.content.res.TypedArray
import android.os.Build
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
import androidx.annotation.RequiresApi
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

    private var currentmenuIndex = 0
    private val handler = Handler()
    private val delay = 200L // 0.2초

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

        setInitialRecipes()
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
        val recommendMenu = MenuRecommendAlgorithm.main(getIngredients(), recipes, getUserPreferIngredients())
        //TODO: 메뉴 세팅

    }

    private fun setInitialRecipes() {
        // DB의 재료 정보를 불러옴
        recipes = db.recipeDao().getRecipes() as ArrayList<Recipe>

        if (recipes.isNotEmpty())
            return
        else {
            // 재료 데이터가 없을 때의 처리
            Thread{
                // recipes가 비어있다면 더미데이터를 넣어줌
                db.recipeDao().apply {
                    insert(Recipe("스파게티", listOf("밥" to 100, "고기" to 100, "채소" to 100, "소스" to 50)))
                    insert(Recipe("비빔밥", listOf("밥" to 100, "고기" to 100, "채소" to 100, "소스" to 50)))
                    insert(Recipe("짜장면", listOf("면" to 150, "고기" to 50, "채소" to 50, "소스" to 30)))
                    insert(Recipe("스테이크", listOf("소고기" to 200, "채소" to 50, "소스" to 30)))
                    insert(Recipe("샌드위치", listOf("빵" to 100, "고기" to 50, "채소" to 80)))
                }
                // 추가했다면 다시 데이터를 ingredients에 넣어줌
                recipes = db.recipeDao().getRecipes() as ArrayList<Recipe>

                // 데이터가 잘 들어왔는지 확인
                val _recipes = db.recipeDao().getRecipesLiveData()
                Log.d("DB recipes data", _recipes.toString())
            }.start()
        }
    }

    private fun getIngredients(): List<Ingredient> {
        return db.ingredientDao().getIngredients()
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
                currentmenuIndex = (currentmenuIndex + 1) % menuNameList.size
                // 이미지 교체
                menuIv.setImageResource(menuImgList.getResourceId(currentmenuIndex, -1))
                menuIv.startAnimation(fadeInAnimation)
                // 텍스트 교체
                menuTv.text = menuNameList[currentmenuIndex]
                menuIv.startAnimation(fadeInAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}