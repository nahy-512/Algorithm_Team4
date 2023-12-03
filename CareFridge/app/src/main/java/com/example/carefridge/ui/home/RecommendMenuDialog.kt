package com.example.carefridge.ui.home

import android.content.res.TypedArray
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.carefridge.R
import com.example.carefridge.databinding.DialogRecommendMenuBinding


class RecommendMenuDialog : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogRecommendMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuImgList: TypedArray
    private lateinit var menuNameList: Array<String>

    private var currentmenuIndex = 0
    private val handler = Handler()
    private val delay = 200L // 0.3초

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRecommendMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        setInit()
        initClickListener()

        return view
    }

    override fun onStart() {
        super.onStart()

        changeMenuImg()
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