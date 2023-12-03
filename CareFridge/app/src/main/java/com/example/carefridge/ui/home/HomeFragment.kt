package com.example.carefridge.ui.home

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.example.carefridge.R
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
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(this.requireFragmentManager(), "AddDialog")
        }
    }
}