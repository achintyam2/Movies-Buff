package com.pt.soch.newsapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


inline fun <reified T: ViewModel> AppCompatActivity.getViewModel(): T {
    return ViewModelProvider(this).get(T::class.java)
}

inline fun <reified T: ViewModel> Fragment.getViewModel(): T {
    return ViewModelProvider(this).get(T::class.java)
}

inline fun <reified T: ViewModel> Fragment.getActivityViewModel(): T {
    return ViewModelProvider(requireActivity()).get(T::class.java)
}


fun View.hideSoftKeyBoard() {
    if (this.windowToken != null) {
        this.clearFocus()
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun View.showToast(msg: String) {
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}

fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(this.context).inflate(layout, this, false)
}

fun View.gone() {
    if (this.visibility == View.VISIBLE) this.visibility = View.GONE
}

fun View.invisible() {
    if (this.visibility == View.VISIBLE) this.visibility = View.INVISIBLE
}

fun View.visible() {
    if (this.visibility == View.GONE || this.visibility == View.INVISIBLE) this.visibility = View.VISIBLE
}



fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


fun FragmentActivity.launchActivity(
    className: Class<*>,
    bundle: Bundle? = null,
    removeAllPrevActivities: Boolean = false
) {
    val activityLaunchIntent = Intent(this, className)
    bundle?.let { activityLaunchIntent.putExtras(it) }
    startActivity(activityLaunchIntent)
    if (removeAllPrevActivities) {
        finishAffinity()
    }

}

fun Fragment.launchActivity(
    className: Class<*>,
    bundle: Bundle? = null
) {
    val activityLaunchIntent = Intent(activity, className)
    bundle?.let { activityLaunchIntent.putExtras(it) }
    startActivity(activityLaunchIntent)
}

fun FragmentActivity.launchActivityForResult(
    className: Class<*>,
    bundle: Bundle? = null,
    requestCode : Int
) {
    val activityLaunchIntent = Intent(this, className)
    bundle?.let { activityLaunchIntent.putExtras(it) }
    startActivityForResult(activityLaunchIntent,requestCode)

}





