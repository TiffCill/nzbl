package com.wyl.nzbl.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class SecondLoadingView : View {
    constructor(context: Context, attributeSet: AttributeSet,def:Int):super(context,attributeSet,def)
    private val mCircleColor:Int = Color.BLUE
    private val mSquareColor:Int = Color.GREEN
    private val mTriangleColor:Int = Color.RED

}