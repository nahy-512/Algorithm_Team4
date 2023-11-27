package com.example.carefridge.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.carefridge.R
import com.example.carefridge.databinding.ActivityMainBinding
import com.example.carefridge.ui.home.HomeFragment
import com.example.carefridge.ui.list.ListFragment
import com.quintable.jpower.config.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        showInit()
        initBottomNav()
    }

    private fun showInit() {
        val transaction = manager.beginTransaction()
            .add(R.id.main_frm, HomeFragment())
        transaction.commitAllowingStateLoss()
    }

    private fun initBottomNav() {
        binding.mainBottomNav.itemIconTintList = null

        binding.mainBottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home -> {
                    HomeFragment().changeFragment()
                }

                R.id.menu_list -> {
                    ListFragment().changeFragment()
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