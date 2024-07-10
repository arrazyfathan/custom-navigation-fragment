package com.arrazyfathan.customnavigationfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseActivityMultipleFragment<VB : ViewBinding, T> : BaseActivity() {

    abstract var currentId: T
    abstract val fragment: (T) -> Fragment

    protected lateinit var binding: VB

    private val popAnim: Pair<Int, Int> = Pair(R.anim.pull_in_left, R.anim.push_out_right)
    private val pushAnim: Pair<Int, Int> = Pair(R.anim.jak_pull_in_right, R.anim.push_out_left)
    val topAnim: Pair<Int, Int> = Pair(R.anim.jak_pull_in_top, R.anim.push_out_bottom)

    private val animation: (Boolean) -> Pair<Int, Int> = { push -> if (push) pushAnim else popAnim }

    private var activeFragmentSimpleName = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setBinding(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun setBinding(layoutInflater: LayoutInflater): VB

    protected fun setFragment(pair: Pair<T, Boolean>) {
        val activeFragment = fragment(pair.first)
        activeFragmentSimpleName.value = activeFragment.javaClass.getSimpleName()
        Log.d("activeFragment", activeFragmentSimpleName.value)
        val anim = animation(pair.second)
        currentId = pair.first
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(anim.first, anim.second)
            .replace(
                R.id.container_fragment, activeFragment, activeFragment.javaClass.getSimpleName())
            .commitAllowingStateLoss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    protected fun showFragment(pair: Pair<T, Boolean>, animationOpt: Pair<Int, Int>? = null) {
        val activeFragment = fragment(pair.first)
        currentId = pair.first
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager
                .findFragmentByTag(activeFragment.javaClass.getSimpleName())
                ?.let {
                    hideAndShowFragment(
                        this, activeFragment, animationOpt ?: animation(pair.second))
                }
                ?: fun() {
                    add(
                        R.id.container_fragment,
                        activeFragment,
                        activeFragment.javaClass.getSimpleName())
                    hideAndShowFragment(
                        this, activeFragment, animationOpt ?: animation(pair.second))
                }()
        }
    }

    protected fun showFragmentWithoutAnimation(pair: Pair<T, Boolean>) {
        val activeFragment = fragment(pair.first)
        currentId = pair.first
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager
                .findFragmentByTag(activeFragment.javaClass.getSimpleName())
                ?.let { hideAndShowFragment(this, activeFragment) }
                ?: fun() {
                    add(
                        R.id.container_fragment,
                        activeFragment,
                        activeFragment.javaClass.getSimpleName())
                    hideAndShowFragment(this, activeFragment)
                }()
        }
    }

    private fun hideAndShowFragment(
        fragmentTransaction: FragmentTransaction,
        activeFragment: Fragment,
        animOpt: Pair<Int, Int>? = null
    ) =
        with(fragmentTransaction) {
            supportFragmentManager
                .findFragmentByTag(activeFragmentSimpleName.value)
                ?.let {
                    animOpt?.let { anim -> setCustomAnimations(anim.first, anim.second) }
                    hide(it)
                }
                .also {
                    activeFragmentSimpleName.value = activeFragment.javaClass.getSimpleName()
                    animOpt?.let { anim -> setCustomAnimations(anim.first, anim.second) }
                    show(activeFragment)
                    commitAllowingStateLoss()
                }
        }
}
