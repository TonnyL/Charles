/*
 * Copyright (c) 2018 Li Zhao Tai Lang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.tonnyl.sample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import io.github.tonnyl.charles.Charles
import io.github.tonnyl.charles.engine.ImageEngine
import io.github.tonnyl.charles.engine.impl.GlideEngine
import io.github.tonnyl.charles.engine.impl.PicassoEngine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mTheme = R.style.Charles
    private var mImageEngine: ImageEngine = GlideEngine()
    private var mShowProgressRate = true
    private val mAdapter = UriAdapter()

    companion object {

        const val REQUEST_CODE_CHOOSE = 101

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val uris = Charles.obtainResult(data)
            val paths = Charles.obtainPathResult(data)
            mAdapter.setData(uris, paths)
            Log.d("uris", "$uris")
            Log.d("paths", "$paths")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menuImageEngine -> {
                showChooseImageEngineDialog()
            }
            R.id.menuTheme -> {
                showChooseThemeDialog()
            }
            R.id.menuProgressRate -> {
                showChooseProgressRateDialog()
            }
            R.id.menuStart -> {
                startCharles()
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CheckResult")
    private fun startCharles() {
        val rxPermission = RxPermissions(this@MainActivity)
        rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({
                    if (it) {
                        Charles.from(this@MainActivity)
                                .choose()
                                .maxSelectable(9)
                                .progressRate(mShowProgressRate)
                                .imageEngine(mImageEngine)
                                .theme(mTheme)
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .forResult(REQUEST_CODE_CHOOSE)

                        mAdapter.setData(null, null)
                    } else {
                        Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }, {

                }, {

                }, {

                })
    }

    private fun showChooseThemeDialog() {
        AlertDialog.Builder(this@MainActivity)
                .setTitle(R.string.menu_theme)
                .setItems(R.array.themes, { _, which ->
                    mTheme = when (which) {
                        0 -> {
                            R.style.Charles
                        }
                        1 -> {
                            R.style.CharlesDark
                        }
                        else -> {
                            R.style.CustomCharlesTheme
                        }
                    }
                })
                .create()
                .show()
    }

    private fun showChooseProgressRateDialog() {
        AlertDialog.Builder(this@MainActivity)
                .setTitle(R.string.menu_progress_rate)
                .setItems(R.array.progress_rate, { _, which ->
                    mShowProgressRate = when (which) {
                        0 -> {
                            true
                        }
                        else -> {
                            false
                        }
                    }
                })
                .create()
                .show()
    }

    private fun showChooseImageEngineDialog() {
        AlertDialog.Builder(this@MainActivity)
                .setTitle(R.string.menu_image_engine)
                .setItems(R.array.image_engines, { _, which ->
                    mImageEngine = when (which) {
                        0 -> {
                            GlideEngine()
                        }
                        1 -> {
                            PicassoEngine()
                        }
                        else -> {
                            GlideLoader()
                        }
                    }
                })
                .create()
                .show()
    }

}
