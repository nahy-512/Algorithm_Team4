package com.example.carefridge.ui.add

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.carefridge.R
import com.example.carefridge.databinding.DialogAddBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

interface AddDialogInterface {
    fun onClickYesButton()
}

class AddDialog(
    private val confirmDialogInterface: AddDialogInterface,
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    // 뷰 바인딩 정의
    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    private var amount = 150 // 초기값
    private var selectedDate: Calendar? = null

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
            this.confirmDialogInterface.onClickYesButton()
            dismiss()
        }

        // 유통 기한 선택
        binding.dialogAddExpirationDateEt.setOnClickListener {
            // 날짜 선택 다이얼로그 띄우기
            showExpirationDatePicker()
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate = Calendar.getInstance()
        selectedDate?.set(Calendar.YEAR, year)
        selectedDate?.set(Calendar.MONTH, month)
        selectedDate?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        // Calendar -> LocalDateTime 변환
        val localDateTime = selectedDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()

        updateExpirationDateText(localDateTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateExpirationDateText(localDateTime: LocalDateTime?) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = localDateTime?.format(formatter)

        // 받아온 날짜로 유통기한 텍스트 세팅
        binding.dialogAddExpirationDateEt.setText(formattedDate)
        Log.d("AddDialog/Date", "localDateTime: ${localDateTime}, formattedDate: $formattedDate")
    }
}