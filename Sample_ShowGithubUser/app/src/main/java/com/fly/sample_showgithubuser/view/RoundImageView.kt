package com.fly.sample_showgithubuser.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.Shader.TileMode
import android.os.Build
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.graphics.Bitmap
import android.graphics.Paint.DITHER_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.R.attr.path
import android.annotation.SuppressLint
import android.graphics.RectF
import android.util.Log
import com.fly.sample_showgithubuser.R


@SuppressLint("AppCompatCustomView")
class RoundImageView : ImageView {

    companion object{
        /**
         * 圓形模式
         */
        const val MODE_CIRCLE = 1
        /**
         * 普通模式
         */
        const val MODE_NONE = 0
        /**
         * 圓角模式
         */
        const val MODE_ROUND = 2
        /**
         * 上圓角模式
         */
        const val MODE_TOP_ROUND = 3
    }

    private lateinit var mPaint: Paint

    /*  使用的模式*/
    private var currMode = 0

    /**
     * 圓角半徑
     */
    private var currRound = convertDpToPx(10F)

    constructor(context: Context) : super(context) {
        initViews()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        obtainStyledAttrs(context, attrs, defStyleAttr)
        initViews()
    }

    private fun obtainStyledAttrs(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0)
        currMode = if (a.hasValue(R.styleable.RoundImageView_type)) a.getInt(
            R.styleable.RoundImageView_type, MODE_NONE
        ) else MODE_NONE
        currRound = if (a.hasValue(R.styleable.RoundImageView_radius)) a.getDimensionPixelSize(
            R.styleable.RoundImageView_radius,
            currRound
        ) else currRound
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 當模式為圓形模式的時候,強制讓寬高一致
         */
        if (currMode === MODE_CIRCLE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            val result = Math.min(measuredHeight, measuredWidth)
            setMeasuredDimension(result, result)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        val mDrawable = drawable
        val mDrawMatrix = imageMatrix
        if (mDrawable == null) {
            return
        }

        if (mDrawable.intrinsicWidth == 0 || mDrawable.intrinsicHeight == 0) {
            return
        }

        if (mDrawMatrix == null && paddingTop == 0 && paddingLeft == 0) {
            mDrawable.draw(canvas)
        } else {
            val saveCount = canvas.getSaveCount()
            canvas.save()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (cropToPadding) {
                    val scrollX = scrollX
                    val scrollY = scrollY
                    canvas.clipRect(
                        scrollX + paddingLeft, scrollY + paddingTop,
                        scrollX + right - left - paddingRight,
                        scrollY + bottom - top - paddingBottom
                    )
                }
            }
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            if (currMode == MODE_CIRCLE) {//當為圓形模式的時候
                val bitmap = convertDrawableToBitmap(mDrawable)
                bitmap?.let {
                    mPaint?.setShader(BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
                    canvas.drawCircle((width-paddingLeft-paddingRight) / 2F, (height-paddingTop-paddingBottom) / 2F, (width-paddingLeft-paddingRight) / 2F, mPaint)
                }
            } else if (currMode == MODE_ROUND) {//當為圓角模式的時候
                val bitmap = convertDrawableToBitmap(mDrawable)
                bitmap?.let {
                    mPaint?.setShader(BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
                    canvas.drawRoundRect(
                        RectF(
                            paddingLeft.toFloat(), paddingTop.toFloat(),
                            (width - paddingRight).toFloat(),
                            (height - paddingBottom).toFloat()
                        ),
                        currRound.toFloat(), currRound.toFloat(), mPaint
                    )
                }
            } else if (currMode == MODE_TOP_ROUND) {
                val bitmap = convertDrawableToBitmap(mDrawable)
                bitmap?.let {
                    mPaint?.setShader(BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
                    val path = Path()
                    //圆角弧度
                    val rids = floatArrayOf(
                        currRound.toFloat(),
                        currRound.toFloat(),
                        currRound.toFloat(),
                        currRound.toFloat(),
                        0.0f,
                        0.0f,
                        0.0f,
                        0.0f
                    )
                    path.addRoundRect(
                        RectF(
                            paddingLeft.toFloat(), paddingTop.toFloat(),
                            (width - paddingRight).toFloat(),
                            (height - paddingBottom).toFloat()
                        ), rids, Path.Direction.CW
                    )
                    canvas.drawPath(path, mPaint)
                }
            } else {
                if (mDrawMatrix != null) {
                    canvas.concat(mDrawMatrix)
                }
                mDrawable.draw(canvas)
            }
            canvas.restoreToCount(saveCount)
        }
    }

    fun setCurrRound(dp: Float){
        currRound = convertDpToPx(dp)
    }

    fun setType(mode: Int){
        currMode = mode
    }

    /**
     * drawable轉換成bitmap
     */
    private fun convertDrawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val matrix = imageMatrix
        if (matrix != null) {
            canvas.concat(matrix)
        }
        drawable.draw(canvas)
        return bitmap
    }

    private fun convertDpToPx(value: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()
    }

    private fun initViews() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    }
}