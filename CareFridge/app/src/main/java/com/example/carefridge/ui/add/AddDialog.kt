package com.example.carefridge.ui.add

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.carefridge.R
import com.example.carefridge.data.FridgeDatabase
import com.example.carefridge.data.entities.Ingredient
import com.example.carefridge.databinding.DialogAddBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AddDialog: DialogFragment(), DatePickerDialog.OnDateSetListener {

    // 뷰 바인딩 정의
    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!
    val JUMPING_RATE = 50

    private lateinit var db: FridgeDatabase

    private var expirationDate: Long = 0L
    private var amount = 150 // 초기값
    var isPrefer: Boolean = false   // 선호 여부
    private var selectedDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddBinding.inflate(inflater, container, false)
        val view = binding.root

        db = FridgeDatabase.getInstance(requireContext())!!

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
        // 유통기한 힌트 세팅 -> 오늘
        binding.dialogAddExpirationDateEt.hint = getFormattedExpirationDate(System.currentTimeMillis())
    }

    private fun initClickListener() {
        // 취소 버튼 클릭
        binding.dialogNoBtn.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 클릭
        binding.dialogYesBtn.setOnClickListener {
            // roomDB에 재료 추가
            insertIngredient()
        }

        // 유통 기한 선택
        binding.dialogAddExpirationDateEt.setOnClickListener {
            // 날짜 선택 다이얼로그 띄우기
            showExpirationDatePicker()
        }

        binding.dialogAddAmountMinusIv.setOnClickListener {
            if (!canUpdateAmount()) {
                Toast.makeText(requireContext(), "최소값은 50입니다.", Toast.LENGTH_SHORT).show()
            } else {
                updateAmount(-1)
            }
        }

        binding.dialogAddAmountPlusIv.setOnClickListener {
            updateAmount(+1)
        }

    }

    private fun insertIngredient() {
        val name = binding.dialogAddNameEt.text.toString()

        val ingredient = Ingredient(0, name, amount, expirationDate, binding.dialogAddPreferToggleBtn.isChecked)
        Log.d("AddDialog", "ingredient: $ingredient")

        // 조건 확인
        if (name.isEmpty() || expirationDate == 0L) {
            Toast.makeText(requireContext(), "값을 모두 추가해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        Thread{
            // roomDB에 추가
            db.ingredientDao().insert(ingredient)
        }.start()

        Toast.makeText(requireContext(), "재료가 리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show()

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
        // 오늘 이전 날짜는 선택 불가능하게
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
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
        val formattedDate = getFormattedExpirationDate(expirationDate)

        // 유통기한 텍스트 업데이트
        updateExpirationDateText(formattedDate)
    }

    private fun updateExpirationDateText(formattedDate: String) {
        // 유통기한 텍스트 업데이트
        binding.dialogAddExpirationDateEt.setText(formattedDate)
        Log.d("AddDialog/Date", "formattedDate: $formattedDate")
    }

    fun getFormattedExpirationDate(longDate: Long): String {
        val instant = Instant.ofEpochMilli(longDate)
        val expirationDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return expirationDate.format(formatter)
    }

    private fun updateAmount(direct: Int) {
        amount += direct * JUMPING_RATE
        binding.dialogAddAmountTv.text = getString(R.string.dialog_add_ingredient_amount_et, amount)
    }

    private fun canUpdateAmount(): Boolean {
        return amount > JUMPING_RATE
    }
}