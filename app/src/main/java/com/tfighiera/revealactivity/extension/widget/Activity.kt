package com.tfighiera.revealactivity.extension.widget

import android.app.Activity
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue

/**
 * Activity Extension Class
 *
 * created on 08/12/2017
 *
 * @author tfi
 * @version 1.0
 */
fun Activity.getThemeColor(@ColorRes colorRef: Int) = TypedValue().also {
    theme.resolveAttribute(colorRef, it, true)
}.data

fun Activity.getColor(@ColorRes colorRes: Int, theme: Resources.Theme): Int {
    return ResourcesCompat.getColor(resources, colorRes, theme)
}