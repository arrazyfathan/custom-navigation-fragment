package com.arrazyfathan.customnavigationfragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun ComponentActivity.registerStartForActivityResult(onResult: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), onResult)
}

fun AppCompatActivity.addFragment(
    container: ViewGroup,
    fragment: Fragment,
    allowStateLoss: Boolean = false
) {
    supportFragmentManager.commitTransaction(allowStateLoss) { add(container.id, fragment) }
}

fun <T : Fragment> AppCompatActivity.addFragment(
    container: ViewGroup,
    fragmentClass: Class<T>,
    params: Bundle? = null,
    tag: String? = null,
    allowStateLoss: Boolean = false
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        add(container.id, fragmentClass, params, tag)
    }
}

fun AppCompatActivity.replaceFragment(
    container: ViewGroup,
    fragment: Fragment,
    tag: String? = null,
    allowStateLoss: Boolean = false
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        replace(container.id, fragment, tag)
    }
}

fun <T : Fragment> AppCompatActivity.replaceFragment(
    container: ViewGroup,
    fragmentClass: Class<T>,
    params: Bundle? = null,
    tag: String? = null,
    allowStateLoss: Boolean = false,
    useCustomAnimation: Boolean = false
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        if (useCustomAnimation) {
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        }
        replace(container.id, fragmentClass, params, tag)
    }
}

fun AppCompatActivity.addFragmentToBackstack(
    container: ViewGroup,
    fragment: Fragment,
    tag: String? = null,
    allowStateLoss: Boolean = false
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        replace(container.id, fragment).addToBackStack(tag)
    }
}

fun <T : Fragment> AppCompatActivity.addFragmentToBackstack(
    container: ViewGroup,
    fragmentClass: Class<T>,
    params: Bundle? = null,
    tag: String? = null,
    allowStateLoss: Boolean = false,
    option: ((FragmentTransaction) -> Unit)? = null
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        option?.invoke(this)
        replace(container.id, fragmentClass, params, tag).addToBackStack(tag)
    }
}

fun AppCompatActivity.popBackstack() {
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.resetBackstack() {
    repeat(supportFragmentManager.backStackEntryCount) {
        supportFragmentManager.popBackStack()
    }
}

fun AppCompatActivity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

/**
 * The current activity must be the root of a task to call onBackPressed, otherwise finish activities with the same task affinity.
 */
fun AppCompatActivity.validateBackPressed(onBackPressed: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && supportFragmentManager.backStackEntryCount == 0) {
        if (isTaskRoot) {
            onBackPressed()
        } else {
            Log.e("","Application is potentially corrupted by an unknown activity")
            finishAffinity()
        }
    } else {
        onBackPressed()
    }
}

fun Activity.restart() {
    finish()
    startActivity(intent)
}

fun Activity.keepScreenOn() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun Activity.endKeepScreenOn() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

inline fun FragmentManager.commitTransactionNow(func: FragmentTransaction.() -> FragmentTransaction) {
    // Could throw and make the app crash
    // e.g sharedActionViewModel.observe()
    tryOrNull("Failed to commitTransactionNow") {
        beginTransaction().func().commitNow()
    }
}

inline fun FragmentManager.commitTransaction(allowStateLoss: Boolean = false, func: FragmentTransaction.() -> FragmentTransaction) {
    val transaction = beginTransaction().func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

inline fun <A> tryOrNull(message: String? = null, operation: () -> A): A? {
    return try {
        operation()
    } catch (any: Throwable) {
        if (message != null) {
            Log.e("$any",message)
        }
        null
    }
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard(andRequestFocus: Boolean = false) {
    if (andRequestFocus) {
        requestFocus()
    }
    val imm = context?.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.setHorizontalPadding(padding: Int) {
    setPadding(
        padding,
        paddingTop,
        padding,
        paddingBottom
    )
}

