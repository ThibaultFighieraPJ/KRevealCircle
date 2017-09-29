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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_circled.*

class RevealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circled)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RevealCircleAnimatorHelper
                .create(this)
                .start(root, getColorAccent(this), ResourcesCompat.getColor(resources, R.color.background_default, theme))
        setShowFragmentButton()
    }

    private fun setShowFragmentButton() {
        show_fragment_fab.setOnClickListener { view ->
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RevealFragment.newInstance(sourceView = view))
                    .commit()
            show_fragment_fab.hide()
        }
    }

    private fun getColorAccent(context: Context) = TypedValue().also {
        context.theme.resolveAttribute(R.attr.colorAccent, it, true)
    }.data

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    companion object {
        /**
         * Create a new [Intent] of [RevealActivity]
         * @param context
         */
        fun newIntent(context: Context, sourceView: View? = null) =
                Intent(context, RevealActivity::class.java).also {
                    RevealCircleAnimatorHelper.addBundleValues(it, sourceView)
                }
    }
}