package com.arrazyfathan.customnavigationfragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.arrazyfathan.customnavigationfragment.databinding.ActivityMainBinding
import com.arrazyfathan.customnavigationfragment.fragment.OneFragment
import com.arrazyfathan.customnavigationfragment.fragment.ThreeFragment
import com.arrazyfathan.customnavigationfragment.fragment.TwoFragment

class MainActivity : BaseActivityMultipleFragment<ActivityMainBinding, MainViewFragmentId>() {

    private val viewModel by viewModels<MainViewModel>()

    // set initial fragment
    override var currentId: MainViewFragmentId = MainViewFragmentId.One

    // define all fragment
    override val fragment: (MainViewFragmentId) -> Fragment = { fragmentId ->
        when (fragmentId) {
            MainViewFragmentId.One -> OneFragment.newInstance()
            MainViewFragmentId.Two -> TwoFragment.newInstance()
            MainViewFragmentId.Three -> ThreeFragment.newInstance()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleBackPress()
        observe()

    }

    private fun observe() {
        viewModel.fragmentFlow.observe(this) { pair ->
            setFragment(pair)
        }
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (currentId) {
                        MainViewFragmentId.Two -> {
                            viewModel.setPage(MainViewFragmentId.One, false)
                        }
                        MainViewFragmentId.Three -> {
                            viewModel.setPage(MainViewFragmentId.Two, false)
                        }

                        MainViewFragmentId.One -> {
                            finish()
                        }
                    }
                }
            },
        )
    }
}
