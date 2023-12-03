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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.carefridge.R
import com.example.carefridge.algorithm.MenuRecommendAlgorithm
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.data.entities.Recipe
import com.example.carefridge.databinding.DialogRecommendMenuBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
    private val delay = 150L // 0.15초
    private val lifecycleScope = CoroutineScope(Dispatchers.Default)

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

        // 다시 추천 받기
        binding.dialogRecommendMenuRetryIv.setOnClickListener {
            // 메뉴 추천 재진행
            retryRecommendMenu()
        }

        // 만들어 먹기
        binding.dialogRecommendMenuTryBtn.setOnClickListener {
            // 메뉴를 선정했으므로, 추천 여부 상태를 이전으로 되돌림
            db.recipeDao().resetRecommendStatus()
            //TODO: 재료 그램수 업데이트
            dismiss()
        }
    }

    private fun getRecommendMenu() {
        recommendMenu = MenuRecommendAlgorithm.main(getIngredients(), recipes, getUserPreferIngredients())
        Log.d("RecommendDialog", recommendMenu.toString())
        if (recommendMenu != null) {
            // 메뉴 세팅
            recommendMenuId = findRecipeIdByName(recommendMenu)
        } else {
            setRecommendDoneView()
        }
    }

    private fun retryRecommendMenu() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (recipes.isNotEmpty()) {
                // db에서 추천 여부 비활성화
                db.recipeDao().updateIsRecommendTargetById(recommendMenuId!!, false)

                // 비활성화가 완료된 후에 레시피 다시 조회 (이전에 추천한 메뉴를 제외하고 다시 추천)
                withContext(Dispatchers.Main) {
                    getRecipes()
                    // 추천 목록을 다시 가져옴
                    getRecommendMenu()
                    // 결과 나오는 동안 애니메이션
                    changeMenuImg()
                }
            }
        }
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
            binding.dialogRecommendMenuIv.setImageResource(menuImgList.getResourceId(it.id - 1, -1))
            binding.dialogRecommendMenuNameTv.text = it.name
        }
        handler.postDelayed({
            setRecommendDoneView()
        }, 1000)
        //        handler.removeCallbacksAndMessages(null)

//        // db에서 추천 여부 비활성화
//        setRecommendMenuInactivate(recommendMenuId!!)
    }

    private fun setRecommendDoneView() {
        with (binding) {
            dialogRecommendMenuTitleTv.text = getString(R.string.dialog_recommend_menu_title)
            dialogRecommendMenuLoadingTv.visibility = View.GONE
            // 버튼 보여주기
            dialogRecommendMenuTryBtn.visibility = View.VISIBLE
            dialogRecommendMenuRetryIv.visibility = View.VISIBLE
        }
    }

    private fun getIngredients(): List<Ingredient> {
        return db.ingredientDao().getIngredients()
    }

    private fun getRecipes() {
        // 추천할 목록
        recipes = db.recipeDao().getRecommendTargetRecipes(true) as ArrayList<Recipe>
        if (recipes.isEmpty()) {
            Toast.makeText(requireContext(), "추천할 수 있는 메뉴가 없습니다!\n추천 메뉴를 초기화합니다.", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch(Dispatchers.IO) {
                db.recipeDao().resetRecommendStatus()
                withContext(Dispatchers.Main) {
                    recipes = db.recipeDao().getRecommendTargetRecipes(true) as ArrayList<Recipe>
                    binding.dialogRecommendMenuRetryIv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getUserPreferIngredients(): List<String> {
        // 사용자가 선호하는 재료 목록을 가져옴
        val preferIngredients = db.ingredientDao().getUserPreferIngredients(true)
        // Ingredient 객체에서 name 필드만 추출하여 List로 반환
        return preferIngredients.map { it.name }
    }

    private fun changeMenuImg() {
        binding.dialogRecommendMenuTitleTv.text = "메뉴 추천중"
        binding.dialogRecommendMenuLoadingTv.visibility = View.VISIBLE
        binding.dialogRecommendMenuTryBtn.visibility = View.GONE
        binding.dialogRecommendMenuRetryIv.visibility = View.GONE

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

        var cnt = 0

        menuIv.startAnimation(fadeOutAnimation)
        menuTv.startAnimation(fadeOutAnimation)

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // 추천중 ... 업데이트
                setLoadingString((++cnt) % 3)

//                Log.d("ImgTest", "index: ${currentMenuIndex}, menu: ${menuNameList[currentMenuIndex]}")
                currentMenuIndex = (currentMenuIndex + 1) % menuNameList.size
                // 이미지 교체
                menuIv.setImageResource(menuImgList.getResourceId(currentMenuIndex, -1))
                menuIv.startAnimation(fadeInAnimation)
                // 텍스트 교체
                menuTv.text = menuNameList[currentMenuIndex]
                menuIv.startAnimation(fadeInAnimation)

                // 추천 결과가 있으면 애니메이션을 멈추고 결과를 업데이트
                if (recommendMenu != null) {
                    handler.postDelayed({
                        setRecommendationResult()
                    }, 1000) // 메뉴를 보여주기까지 딜레이 주기
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun setLoadingString(cnt: Int) { // 로딩 상태 업데이트
        var loadingString = ""
        for (i: Int in 0 until cnt) {
            loadingString += ". "
        }
        binding.dialogRecommendMenuLoadingTv.text = loadingString
    }
}