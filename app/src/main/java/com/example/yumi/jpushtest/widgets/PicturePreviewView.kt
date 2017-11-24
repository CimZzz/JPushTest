package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.utils.sendToast

/**
 * Created by CimZzz(王彦雄) on 2017/11/23.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class PicturePreviewView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val DIFFERENCE_RATIO = 1.0f / 1000

    var picPath : String = ""
    var bitmap : Bitmap? = null
    var ratio : Float = 0f
    var lastWidth : Int = 0
    var bitmapMatrix : Matrix = Matrix()
    val paint : Paint = Paint()

    var startX : Float = 0f
    var startY : Float = 0f

    var curX : Float = 0f
    var curY : Float = 0f

    var downX : Float = 0f
    var downY : Float = 0f

    var downX2 : Float = 0f

    var tmpX : Float = 0f
    var tmpY : Float = 0f

    var tmpRatio : Float = 0f


    var friXdis: Float = 0f

    var pointerCount = 0
    var isScaled = false


    fun setPicture(path : String) {
        if(bitmap != null) {
            bitmap!!.recycle()
            bitmap = null
            isScaled = false
        }
        picPath = path

        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        when(action) {
            MotionEvent.ACTION_DOWN->{
                downX = event.x
                downY = event.y

                tmpX = curX
                tmpY = curY
                pointerCount = 1
            }
            MotionEvent.ACTION_POINTER_DOWN-> {
                downX2 = event.getX(1)
                pointerCount = 2

                friXdis = Math.abs(downX2 - downX)
                tmpRatio = ratio
            }
            MotionEvent.ACTION_MOVE-> {
                if(pointerCount == 1) {
                    //单指触摸
                    val disX = event.x - downX
                    val disY = event.y - downY

                    bitmapMatrix.postTranslate(-tmpX,-tmpY)
                    tmpX = curX + disX
                    tmpY = curY + disY

                    bitmapMatrix.postTranslate(tmpX,tmpY)
                }
                else {
                    //多指触摸
                    if(pointerCount == -1 || event.pointerCount < 2)
                        return true

                    val curXDiffval = event.getX(1) - event.getX(0) - friXdis
                    bitmapMatrix.setScale(-tmpRatio,-tmpRatio)
                    tmpRatio = ratio + curXDiffval * DIFFERENCE_RATIO
                    bitmapMatrix.setScale(tmpRatio,tmpRatio)

                }
                invalidate()
            }
            MotionEvent.ACTION_UP-> {
                if(pointerCount == 1) {
                    //单指触摸结束
                    if(isScaled) {
                        //缩放时处理
                    }
                    else {
                        bitmapMatrix.postTranslate(-tmpX,-tmpY)
                        bitmapMatrix.postTranslate(startX,startY)
                    }
                }
                pointerCount = -1
                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawARGB(255,0,0,0)
        var tmpBitmap = bitmap



        if(tmpBitmap == null) {
            tmpBitmap = BitmapFactory.decodeFile(picPath)
            bitmap = tmpBitmap
        }

        if(lastWidth != width) {
            lastWidth = width
            bitmapMatrix.reset()
            val bitmapW = tmpBitmap!!.width
            val bitmapH = tmpBitmap.height

            val ratioW = width * 1.0f / bitmapW
            val ratioH = height * 1.0f / bitmapH
            ratio = if(ratioW > ratioH) ratioH else ratioW

            val afterW = bitmapW * ratio
            val afterH = bitmapH * ratio

            startX = (width - afterW) / 2.0f
            startY = (height - afterH) / 2.0f

            bitmapMatrix.setScale(ratio,ratio,0f,0f)
            bitmapMatrix.postTranslate(startX,startY)

            curX = startX
            curY = startY

        }

        canvas.drawBitmap(bitmap,bitmapMatrix,paint)
    }
}
