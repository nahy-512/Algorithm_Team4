package com.example.carefridge.ui.ingredient

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.carefridge.R
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.DialogEditIngredientBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class EditIngredientBottomSheetDialog(var ingredient: Ingredient): BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: DialogEditIngredientBinding

    val JUMPING_RATE = 50

    private lateinit var db: FridgeDatabase

    private var selectedDate: Calendar? = null

    private var expirationDate: Long = 0L // 유통기한
    private var amount: Int = 0 // 양
    var isPrefer: Boolean = false   // 선호 여부

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogEditIngredientBinding.inflate(inflater, container, false)

        db = FridgeDatabase.getInstance(requireContext())!!

        setInit()
        initClickListener()

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("EditDialog", "onDismiss()")
    }

    private fun setInit() {
        amount = ingredient.amount
        isPrefer = ingredient.isPrefer
        expirationDate = ingredient.expirationDate

        with(binding){
            // 재료 이름
            dialogEditIngredientNameEt.setText(ingredient.name)
            // 유통기한
            dialogEditIngredientExpirationDateEt.setText(ingredient.getFormattedExpirationDate())
            // 양
            binding.dialogEditIngredientAmountTv.text = amount.toString()
            // 첫 진입 시 토글 이미지 세팅
            dialogEditIngredientPreferToggleBtn.isChecked = isPrefer
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
            // roomDB에 재료 정보 엄데이트
            updateIngredient()
        }

        // 유통기한
        binding.dialogEditIngredientExpirationDateEt.setOnClickListener {
            showExpirationDatePicker()
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

    private fun updateIngredient() {
        val name = binding.dialogEditIngredientNameEt.text.toString()

        ingredient = Ingredient(ingredient.id, name, amount, expirationDate, binding.dialogEditIngredientPreferToggleBtn.isChecked)
        Log.d("EditDialog", "ingredient: $ingredient")

        // 조건 확인
        if (name.isEmpty() || expirationDate == 0L) {
            Toast.makeText(requireContext(), "값을 모두 추가해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val thread = Thread{
            // roomDB에 추가
            db.ingredientDao().update(ingredient)
        }
        thread.start()
        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }

        Toast.makeText(requireContext(), "재료의 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()

        // 종료
        dismiss()
    }

    private fun showExpirationDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 달력 만들기
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        // 이미 선택된 날짜 설정
        if (expirationDate != 0L) {
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = expirationDate

            // 오늘 이전 날짜도 처리
            if (selectedCalendar.get(Calendar.YEAR) > 0) {
                datePickerDialog.updateDate(
                    selectedCalendar.get(Calendar.YEAR),
                    selectedCalendar.get(Calendar.MONTH),
                    selectedCalendar.get(Calendar.DAY_OF_MONTH)
                )
            }
        }
        // 다이얼로그 띄우기
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate = Calendar.getInstance()
        selectedDate?.set(Calendar.YEAR, year)
        selectedDate?.set(Calendar.MONTH, month)
        selectedDate?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        // Calendar를 Long으로 변환
        expirationDate = selectedDate?.timeInMillis!!

        // 포맷된 날짜를 얻기 위해 함수 호출
        ingredient.expirationDate = expirationDate
        val formattedDate = ingredient.getFormattedExpirationDate()

        // 유통기한 텍스트 업데이트
        updateExpirationDateText(formattedDate)
    }

    private fun updateExpirationDateText(formattedDate: String) {
        // 유통기한 텍스트 업데이트
        binding.dialogEditIngredientExpirationDateEt.setText(formattedDate)
        Log.d("EditDialog/Date", "formattedDate: $formattedDate")
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