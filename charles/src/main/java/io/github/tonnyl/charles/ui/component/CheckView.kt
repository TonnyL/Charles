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

package io.github.tonnyl.charles.ui.component

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable

import io.github.tonnyl.charles.R

class CheckView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle), Checkable {

    private var checkDrawable: Drawable? = null

    private var bitmapPaint: Paint? = null
    private var bitmapEraser: Paint? = null
    private var checkEraser: Paint? = null
    private var borderPaint: Paint? = null

    private var drawBitmap: Bitmap? = null
    private var checkBitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var checkCanvas: Canvas? = null

    private var progress: Float = 0.toFloat()
        set(value) {
            if (this.progress == value) {
                return
            }
            field = value
            invalidate()
        }

    private var checkAnim: ObjectAnimator? = null

    private var attachedToWindow: Boolean = false
    private var isChecked: Boolean = false

    // default size
    private var size = 24
    // inside bitmap's color
    private var bitmapColor = Color.parseColor("#3F51B5")
    // border's color
    private var borderColor = Color.parseColor("#FFFFFF")

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CheckView)
        size = ta.getDimensionPixelSize(R.styleable.CheckView_size, dp(size.toFloat()))
        bitmapColor = ta.getColor(R.styleable.CheckView_color_background, bitmapColor)
        borderColor = ta.getColor(R.styleable.CheckView_color_border, borderColor)

        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bitmapEraser = Paint(Paint.ANTI_ALIAS_FLAG)
        bitmapEraser?.color = 0
        bitmapEraser?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        checkEraser = Paint(Paint.ANTI_ALIAS_FLAG)
        checkEraser?.color = 0
        checkEraser?.style = Paint.Style.STROKE
        checkEraser?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint?.style = Paint.Style.STROKE
        borderPaint?.strokeWidth = dp(2f).toFloat()
        checkDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_checked, null)

        // tint the check view icon
        val attr = context.obtainStyledAttributes(intArrayOf(R.attr.media_checkView_iconColor))
        val color = attr.getColor(0, 0)
        attr.recycle()

        checkDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        visibility = View.VISIBLE
        ta.recycle()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE && drawBitmap == null) {
            drawBitmap = Bitmap.createBitmap(dp(size.toFloat()), dp(size.toFloat()), Bitmap.Config.ARGB_8888)
            bitmapCanvas = Canvas(drawBitmap)
            checkBitmap = Bitmap.createBitmap(dp(size.toFloat()), dp(size.toFloat()), Bitmap.Config.ARGB_8888)
            checkCanvas = Canvas(checkBitmap)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.getMode(Math.min(widthMeasureSpec, heightMeasureSpec)))
        super.onMeasure(newSpec, newSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if (visibility != View.VISIBLE) {
            return
        }
        checkEraser?.strokeWidth = size.toFloat()

        drawBitmap?.eraseColor(0)
        var rad = (measuredWidth / 2).toFloat()

        val bitmapProgress = if (this.progress >= 0.5f) 1.0f else this.progress / 0.5f
        val checkProgress = if (this.progress < 0.5f) 0.0f else (this.progress - 0.5f) / 0.5f

        val p = if (isChecked) this.progress else 1.0f - this.progress

        if (p < BOUNCE_VALUE) {
            rad -= dp(2f) * p
        } else if (p < BOUNCE_VALUE * 2) {
            rad -= dp(2f) - dp(2f) * p
        }

        borderPaint?.color = borderColor
        canvas.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), rad - dp(1f), borderPaint)

        bitmapPaint?.color = bitmapColor

        bitmapCanvas?.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), rad, bitmapPaint)
        bitmapCanvas?.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), rad * (1 - bitmapProgress), bitmapEraser)
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)

        checkBitmap?.eraseColor(0)
        val w = checkDrawable?.intrinsicWidth
        val h = checkDrawable?.intrinsicHeight
        if (w != null && h != null) {
            val x = (measuredWidth - w) / 2
            val y = (measuredHeight - h) / 2
            checkDrawable?.setBounds(x, y, x + w, y + h)
        }

        checkDrawable?.draw(checkCanvas)
        checkCanvas?.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), rad * (1 - checkProgress), checkEraser)

        canvas.drawBitmap(checkBitmap, 0f, 0f, null)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachedToWindow = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        attachedToWindow = false
    }

    fun setSize(size: Int) {
        this.size = size
    }

    fun setCheckedColor(value: Int) {
        bitmapColor = value
    }

    fun setBorderColor(value: Int) {
        borderColor = value
        borderPaint?.color = borderColor
    }

    private fun cancelAnim() {
        if (checkAnim != null) {
            checkAnim?.cancel()
        }
    }

    private fun addAnim(isChecked: Boolean) {
        checkAnim = ObjectAnimator.ofFloat(this, "progress", if (isChecked) 1.0f else 0.0f)
        checkAnim?.duration = 300
        checkAnim?.start()
    }

    fun setChecked(checked: Boolean, animated: Boolean) {
        if (checked == isChecked) {
            return
        }
        isChecked = checked

        if (attachedToWindow && animated) {
            addAnim(checked)
        } else {
            cancelAnim()
            progress = if (checked) 1.0f else 0.0f
        }
    }


    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(b: Boolean) {
        setChecked(b, true)
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    fun dp(value: Float): Int {
        if (value == 0f) {
            return 0
        }
        val density = context.resources.displayMetrics.density
        return Math.ceil((density * value).toDouble()).toInt()
    }

    companion object {
        private const val BOUNCE_VALUE = 0.2f
    }

}