/*
 *
 *  Copyright (C) 2017 Thibault Fighiera
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tfighiera.revealactivity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.tfighiera.revealactivity.RevealCircleAnimatorHelper.Companion.addBundleValues

/**
 * Helper allowing [Activity] display from a source view with a Circular Animation.
 * It can also crossfade source view color to targated color
 * @see [addBundleValues] for parameters
 * @author tfighiera
 * @version 1.0
 */
class RevealCircleAnimatorHelper {
    private val CIRCULAR_SPEED = 400L
    private var mSourceX: Float = -1f
    private var mSourceY: Float = -1f

    constructor(activity: Activity) {
        if (extractBundleValues(activity.intent.extras)) {
            activity.overridePendingTransition(0, 0)
            activity.intent.removeExtra(EXTRAS)
        }
    }

    constructor(fragment: Fragment, container: ViewGroup? = null) {
        if (extractBundleValues(fragment.arguments)) {
            fragment.arguments.remove(EXTRAS)
            container?.let {
                if (container.x < mSourceX) mSourceX -= container.x else mSourceX = 0f
                if (container.y < mSourceY) mSourceY -= container.y else mSourceY = 0f
            }
        }
    }

    private fun extractBundleValues(extras: Bundle?): Boolean {
        val bundle = extras?.getBundle(EXTRAS)
        if (bundle != null) {
            mSourceX = bundle.getFloat(SOURCE_X)
            mSourceY = bundle.getFloat(SOURCE_Y)
            return true
        }
        return false
    }

    /**
     * Start circle animation
     * @param fromColor the source color desired for circular background
     * @param targetColor the final color to apply to [rootLayout]
     */
    fun start(rootLayout: View, @ColorInt fromColor: Int? = null, @ColorInt targetColor: Int? = null) {
        if (mSourceX >= 0 && mSourceY >= 0) {
            rootLayout.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(rootLayout: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int) {
                    startCircularAnimation(rootLayout, fromColor, targetColor)
                    rootLayout?.removeOnLayoutChangeListener(this)
                }
            })
        }
    }

    private fun startCircularAnimation(rootLayout: View?, @ColorInt fromColor: Int?, @ColorInt targetColor: Int?) {
        if (rootLayout == null) {
            return
        }
        val circularReveal = getCircularAnimator(rootLayout, mSourceX.toInt(), mSourceY.toInt(), CIRCULAR_SPEED)
        val animator = AnimatorSet()
        if (fromColor != null && targetColor != null) {
            val fadeAnimator = getColorCrossFadeAnimator(rootLayout, fromColor, targetColor, CIRCULAR_SPEED)
            animator.play(circularReveal).before(fadeAnimator)
        } else {
            animator.play(circularReveal)
        }
        animator.start()
    }

    private fun getCircularAnimator(targetView: View, sourceX: Int, sourceY: Int, speed: Long): Animator {
        val finalRadius = Math.hypot(targetView.width.toDouble(), targetView.height.toDouble()).toFloat()
        return ViewAnimationUtils.createCircularReveal(targetView, sourceX, sourceY, 0f, finalRadius).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = speed
        }
    }

    private fun getColorCrossFadeAnimator(targetView: View, @ColorInt fromColor: Int, @ColorInt targetColor: Int, speed: Long): ValueAnimator {
        val colorAnimation = ValueAnimator.ofArgb(fromColor, targetColor)
        colorAnimation.interpolator = AccelerateDecelerateInterpolator()
        colorAnimation.duration = speed
        // Set new color to background smoothly
        colorAnimation.addUpdateListener { animation ->
            targetView.setBackgroundColor(animation.animatedValue as Int)
        }
        // if canceled or ended set final color
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                targetView.setBackgroundColor(targetColor)
            }

            override fun onAnimationCancel(animation: Animator) {
                targetView.setBackgroundColor(targetColor)
            }
        })
        targetView.setBackgroundColor(fromColor)
        return colorAnimation
    }

    companion object {
        private const val SOURCE_X = "source_x"
        private const val SOURCE_Y = "source_y"
        private const val EXTRAS = "reveal-circle-extra"

        /**
         * Add useful values to activity's Intent which will be extracted when new activity will start
         * @see RevealCircleAnimatorHelper.start
         * @param intent    The source intent
         * @param sourceView  The source view triggering new intent display
         */
        fun addBundleValues(intent: Intent, sourceView: View?) {
            if (sourceView != null) {
                intent.putExtra(EXTRAS, createBundle(sourceView))
            }
        }

        /**
         * Add useful values to bundle which will be extracted when new fragment will start
         * @see RevealCircleAnimatorHelper.start
         * @param arguments    The main bundle containing all others
         * @param sourceView  The source view triggering new intent display
         */
        fun addBundleValues(arguments: Bundle, sourceView: View?) {
            if (sourceView != null) {
                return arguments.putBundle(EXTRAS, createBundle(sourceView))
            }
        }

        private fun createBundle(sourceView: View) = Bundle().apply {
            putFloat(SOURCE_X, sourceView.x + sourceView.width / 2)
            putFloat(SOURCE_Y, sourceView.y + sourceView.height / 2)
        }

        /**
         * Create new [RevealCircleAnimatorHelper] from Fragment
         */
        fun create(fragment: Fragment, container: ViewGroup? = null) = RevealCircleAnimatorHelper(fragment, container)

        /**
         * Create new [RevealCircleAnimatorHelper] from Activity
         */
        fun create(activity: Activity) = RevealCircleAnimatorHelper(activity)
    }
}
