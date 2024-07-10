package com.arrazyfathan.customnavigationfragment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.arrazyfathan.customnavigationfragment.MainViewFragmentId
import com.arrazyfathan.customnavigationfragment.MainViewModel
import com.arrazyfathan.customnavigationfragment.R

class ThreeFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val next = view.findViewById<Button>(R.id.next)
        val back = view.findViewById<Button>(R.id.back)

        next.setOnClickListener {

        }


        back.setOnClickListener {
            viewModel.setPage(MainViewFragmentId.Two, false)
        }
    }

    companion object {
        @JvmStatic fun newInstance() = ThreeFragment()
    }
}
