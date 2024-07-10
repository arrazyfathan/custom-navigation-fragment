package com.arrazyfathan.customnavigationfragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
}
