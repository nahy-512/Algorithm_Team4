package com.example.carefridge.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.carefridge.databinding.DialogRecommendMenuBinding


class RecommendMenuDialog : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogRecommendMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRecommendMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        initClickListener()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

}