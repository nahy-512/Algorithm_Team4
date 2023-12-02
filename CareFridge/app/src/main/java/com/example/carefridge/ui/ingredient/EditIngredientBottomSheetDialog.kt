package com.example.carefridge.ui.ingredient

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.carefridge.R
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.DialogEditIngredientBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditIngredientBottomSheetDialog(val ingredient: Ingredient): BottomSheetDialogFragment() {

    private lateinit var binding: DialogEditIngredientBinding
    val JUMPING_RATE = 50

    private var amount: Int = 0 // 양
    var isPrefer: Boolean = false   // 선호 여부

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogEditIngredientBinding.inflate(inflater, container, false)

        setInit()
        initClickListener()
        switchToggle()

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("EditDialog", "onDismiss()")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setInit() {
        amount = ingredient.amount
        isPrefer = ingredient.isPrefer

        val expirationDate = ingredient.getFormattedExpirationDate()

        with(binding){
            // 재료 이름
            dialogEditIngredientNameEt.setText(ingredient.name)
            // 유통기한
            dialogEditIngredientExpirationDateEt.setText(expirationDate)
            // 양
            binding.dialogEditIngredientAmountTv.text = amount.toString()
            // 첫 진입 시 토글 이미지 세팅
            dialogEditIngredientPreferToggleBtn.isChecked = isPrefer
        }
    }


    private fun switchToggle() {
        val toggle = binding.dialogEditIngredientPreferToggleBtn
        // 토글 클릭 시 이미지 세팅
        toggle.setOnClickListener {
            toggle.isChecked = !isPrefer
            isPrefer = !isPrefer
        }
    }

    private fun initClickListener() {
        // X 버튼 눌러서 내리기
        binding.dialogEditIngredientCloseIv.setOnClickListener {
            dismiss()
        }

        // 삭제
        binding.dialogEditIngredientDeleteBtn.setOnClickListener {
            // 삭제 확인 다이얼로그 띄우기
            showDeleteAlertDialog()
        }


        // 편집
        binding.dialogEditIngredientDoneBtn.setOnClickListener {
            //TODO: roomDB 편집 진행
            dismiss()
        }

        // 양 감소
        binding.dialogEditIngredientAmountMinusIv.setOnClickListener {
            if (!canUpdateAmount()) {
                Toast.makeText(requireContext(), "최소값은 50입니다.", Toast.LENGTH_SHORT).show()
            } else {
                updateAmount(-1)
            }
        }

        // 양 증가
        binding.dialogEditIngredientAmountPlusIv.setOnClickListener {
            updateAmount(+1)
        }
    }

    private fun updateAmount(direct: Int) {
        amount += direct * JUMPING_RATE
        binding.dialogEditIngredientAmountTv.text = getString(R.string.dialog_add_ingredient_amount_et, amount)
    }

    private fun canUpdateAmount(): Boolean {
        return amount > JUMPING_RATE
    }

    private fun showDeleteAlertDialog() {
        /* Dialog를 활용하여 이어서 작성할거냐고 묻는 창 띄우기 */
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("재료를 삭제하시겠습니까?")
            .setMessage("확인 누르시면 재료가 영구적으로 삭제됩니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    Log.d("EditDialog", "삭제 버튼 클릭")
                    //TODO: roomDB 삭제 진행
                    dismiss()
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    // 특별한 동작 없음
                })
        // 다이얼로그 띄우기
        builder.show()
    }
}