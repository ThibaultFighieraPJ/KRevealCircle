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

import android.content.ClipData
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab.setOnClickListener { view ->
            startActivity(RevealActivity.newIntent(this, sourceView = view))
        }
        setDragDrop(fab)
    }

    private fun setDragDrop(fab: FloatingActionButton) {
        fab.setOnLongClickListener { view ->
            ViewCompat.startDragAndDrop(view, ClipData.newPlainText("", ""), View.DragShadowBuilder(view), null, 0)
        }

        fab.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_ENTERED, DragEvent.ACTION_DRAG_STARTED -> view.visibility = View.INVISIBLE
                DragEvent.ACTION_DRAG_ENDED -> view.visibility = View.VISIBLE
            }
            true
        }
        main_activity.setOnDragListener { _, event ->
            if (event.action == DragEvent.ACTION_DROP) {
                setNewFabPosition(event.x.toInt(), event.y.toInt())
            }
            true
        }
    }

    private fun setNewFabPosition(x: Int, y: Int) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.NO_GRAVITY
        params.leftMargin = x - fab.width / 2
        params.topMargin = y - fab.height / 2
        fab.layoutParams = params
    }
}
