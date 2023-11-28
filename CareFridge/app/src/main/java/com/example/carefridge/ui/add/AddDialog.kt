package com.example.carefridge.ui.add

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.carefridge.R
import com.example.carefridge.databinding.DialogAddBinding

interface AddDialogInterface {
    fun onClickYesButton()
}

class AddDialog(
    confirmDialogInterface: AddDialogInterface,
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: AddDialogInterface? = null

    private var amount = 150 // 초기값

    init {
        this.confirmDialogInterface = confirmDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddBinding.inflate(inflater, container, false)
        val view = binding.root

        setInit()
        initClickListener()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setInit() {
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 버튼이 없는 다이얼로그는 id를 -1로 넘겨줌
        if (id == -1) {
            // 취소 버튼을 보이지 않게 처리
            binding.dialogNoBtn.visibility = View.GONE
        }

        // 초기 양 세팅
        binding.dialogAddAmountTv.text = getString(R.string.dialog_add_ingredient_amount_et, amount)
    }

    private fun initClickListener() {
        // 취소 버튼 클릭
        binding.dialogNoBtn.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 클릭
        binding.dialogYesBtn.setOnClickListener {
            this.confirmDialogInterface?.onClickYesButton()
            dismiss()
        }

        binding.dialogAddExpirationDateEt.setOnClickListener {
            Log.d("AddDialog", "유통 기한 et 클릭")
            //TODO: 유통기한 날짜 선택하기
        }
    }
}