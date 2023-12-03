package com.example.carefridge.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.carefridge.R
import com.example.carefridge.databinding.ActivityMainBinding
import com.example.carefridge.ui.add.AddDialog
import com.example.carefridge.ui.home.HomeFragment
import com.example.carefridge.ui.ingredient.IngredientFragment
import com.quintable.jpower.config.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showInit()
        initBottomNav()
        showAddDialog()

        // 알고리즘 테스트
//        testMenuRecommendAlgorithm()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun testMenuRecommendAlgorithm() {
        // 메뉴 추천 알고리즘 테스트
//        MenuRecommendAlgorithm.main()
    }

    private fun showInit() {
        val transaction = manager.beginTransaction()
            .add(R.id.main_frm, HomeFragment())
        transaction.commitAllowingStateLoss()
    }

    private fun showAddDialog() {
        binding.mainFloatingAddBtn.setOnClickListener {
            val dialog = AddDialog()
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "AddDialog")
        }
    }

    private fun initBottomNav() {
        binding.mainBottomNav.itemIconTintList = null
        // 바텀네비게이션 음영 삭제
        binding.mainBottomNav.background = null
        // 자리를 비워놓은(플로팅 버튼) 아이템에 대해 비활성화
        binding.mainBottomNav.menu.getItem(1).isEnabled = false

        binding.mainBottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home -> {
                    HomeFragment().changeFragment()
                    Log.d("MainActivity", "HomeFragment")
                }

                R.id.menu_list -> {
                    IngredientFragment().changeFragment()
                    Log.d("MainActivity", "ListFragment")
                }
            }
            return@setOnItemSelectedListener true
        }

        binding.mainBottomNav.setOnItemReselectedListener {  } // 바텀네비 재클릭시 화면 재생성 방지
    }

    private fun Fragment.changeFragment() {
        manager.beginTransaction().replace(R.id.main_frm, this).commitAllowingStateLoss()
    }
}