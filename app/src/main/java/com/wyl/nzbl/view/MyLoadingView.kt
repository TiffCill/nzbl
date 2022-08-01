package com.wyl.nzbl.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wyl.nzbl.R

class MyLoadingView @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private var maxRadius = 120       //最大半径
    private var minRadius = 30       //最小半径
    private var leftRadius = 60      //左边小球当前半径
    private var midRadius = 60       //中间小球当前半径
    private var rightRadius = 60     //右边小球当前半径
    private var myDuration = 1500    //动画时间
    private var color = Color.BLUE   //颜色
    private var internal = 400       //小球间距

    private val leftRadiosAnimator = ValueAnimator()
    private val midRadiosAnimator = ValueAnimator()
    private val rightRadiosAnimator = ValueAnimator()
    private lateinit var  mPaint : Paint
    init {
        initParams()
    }

    private fun initParams(){
        val a = this.context.obtainStyledAttributes(attributeSet, R.styleable.LoadingView)
        //获取自定义属性
        a.apply {
            maxRadius = getInt(R.styleable.LoadingView_max_radius,30)
            if (maxRadius > 200) maxRadius = 200
            minRadius = getInt(R.styleable.LoadingView_min_radius,5)
            if (minRadius < 1 || minRadius == maxRadius) minRadius = maxRadius - 10
            leftRadius = minRadius
            midRadius = minRadius
            rightRadius = minRadius
            myDuration = getInt(R.styleable.LoadingView_anim_duration,500)
            color = getColor(R.styleable.LoadingView_ball_color,Color.BLUE)
            internal = getInt(R.styleable.LoadingView_internal,100)
        }
        a.recycle()//回收typedArray

        setAnim()

        mPaint = Paint()
        mPaint.color = color
        mPaint.style = Paint.Style.FILL
    }

    private fun setAnim() {
        //设置左边小球动画
        leftRadiosAnimator.run {
            setIntValues(minRadius,maxRadius)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = myDuration.toLong()
            startDelay = duration * 1 / 3
            addUpdateListener {
                leftRadius = it.animatedValue as Int
            }
        }
        //设置中间小球动画
        midRadiosAnimator.run {
            setIntValues(minRadius,maxRadius)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = myDuration.toLong()
            startDelay = duration * 2 / 3
            addUpdateListener {
                midRadius = it.animatedValue as Int
            }
        }
        //设置右边小球动画
        rightRadiosAnimator.run {
            setIntValues(minRadius, maxRadius)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = myDuration.toLong()
            startDelay = duration * 3 / 3
            addUpdateListener {
                rightRadius = it.animatedValue as Int
            }
        }

        leftRadiosAnimator.start()
        midRadiosAnimator.start()
        rightRadiosAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val  widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)  //宽度
        val  heightSpecMode = MeasureSpec.getMode(heightMeasureSpec) //高度模式
        val  heightSpecSize = MeasureSpec.getSize(heightMeasureSpec) //高度
        //处理高度为wrap_content时的情况，宽度如果为wrap_content时默认为屏幕宽度
        if (heightSpecMode == MeasureSpec.AT_MOST){
            //设置整个控件的大小
            setMeasuredDimension(widthSpecSize,maxRadius * 2)
        }else{
            setMeasuredDimension(widthMeasureSpec,heightSpecSize)
        }
    }

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        val pivotX = width / 2  //中心小球横坐标
        val pivotY = height / 2 //中心小球纵坐标

        canvas?.apply {
            //开始绘制
            drawCircle((pivotX + internal ).toFloat(),pivotY.toFloat(),leftRadius.toFloat(),mPaint)
            drawCircle(pivotX.toFloat(),pivotY.toFloat(),midRadius.toFloat(),mPaint)
            drawCircle((pivotX - internal ).toFloat(),pivotY.toFloat(),rightRadius.toFloat(),mPaint)
        }
        invalidate()

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        leftRadiosAnimator.end()
        midRadiosAnimator.end()
        rightRadiosAnimator.end()
    }
}