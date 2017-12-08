package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.logV
import java.util.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/23.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class PicturePreviewView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val DIFFERENCE_RATIO = 1.0f / 400
    private val IGNORE_MOVE_RANGE = 100
    private val MAX_SIZE = 4000f
    private val MIN_SIZE = 600f

    var picPath : String = ""
    var listener : OnLongPressListener? = null


    var bitmap : Bitmap? = null
    var ratio : Float = 0f
    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var bitmapMatrix : Matrix = Matrix()
    val paint : Paint = Paint()

    var overRange : Boolean = false
    var trigger : Boolean = false
    var timer : Timer? = null
    var timerTask : TimerTask? = null

    var minimumRatio : Float = 0f
    var maxmumRatio : Float = 0f
    var originRatio : Float = 0f


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
    var xRatio : Float = 0f
//    var originX : Float = 0f


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

    private fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                logV("TimerDo")
                trigger = true

                if(listener != null)
                    post({ listener!!.onLongPress() })

                closeTimer()
            }
        }
        timer!!.schedule(timerTask,2000)
    }

    private fun closeTimer() {
        if(timerTask != null)
            timerTask!!.cancel()
        if(timer != null)
            timer!!.cancel()
        timerTask = null
        timer = null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked

        if(trigger) {
            if(action == MotionEvent.ACTION_UP)
                trigger = false

            return true
        }

        when(action) {
            MotionEvent.ACTION_DOWN->{
                downX = event.x
                downY = event.y

                tmpX = curX
                tmpY = curY
                pointerCount = 1
                overRange = false
                startTimer()
            }
            MotionEvent.ACTION_POINTER_DOWN-> {
                if(pointerCount != 2) {
                    val centerX = (event.getX(1) + event.getX(0))/2.0f
                    xRatio = (centerX - curX) / (screenWidth - curX)
                }

                downX2 = event.getX(1)
                pointerCount = 2

                friXdis = Math.abs(downX2 - downX)
                closeTimer()
            }
            MotionEvent.ACTION_MOVE-> {
                if(pointerCount == 1) {
                    //单指触摸
                    val disX = event.x - downX
                    val disY = event.y - downY

                    if(!overRange) {
                        val nowRange = Math.sqrt((disX * disX+disY * disY).toDouble()).toFloat()
                        if(nowRange > IGNORE_MOVE_RANGE) {
                            closeTimer()
                            overRange = true
                        }
                        else return true
                    }

                    bitmapMatrix.postTranslate(-tmpX,-tmpY)
                    tmpX = curX + disX
                    tmpY = curY + disY

                    bitmapMatrix.postTranslate(tmpX,tmpY)
                }
                else {
                    //多指触摸
                    if(pointerCount == -1 || event.pointerCount < 2)
                        return true


                    val curXDiffval = Math.abs(event.getX(1) - event.getX(0)) - friXdis
                    val lastRatio = tmpRatio
                    tmpRatio = ratio + curXDiffval * DIFFERENCE_RATIO

                    if(tmpRatio > maxmumRatio)
                        tmpRatio = maxmumRatio
                    else if(tmpRatio < minimumRatio)
                        tmpRatio = minimumRatio

                    val afterW = bitmap!!.width * tmpRatio
                    val afterH = bitmap!!.height * tmpRatio

                    bitmapMatrix.setScale(-lastRatio,-lastRatio)
                    bitmapMatrix.setScale(tmpRatio,tmpRatio)
                    if(tmpRatio <= originRatio) {
                        bitmapMatrix.postTranslate((width - afterW) / 2.0f,(height - afterH) / 2.0f)
                    } else {
                        tmpX = startX - (bitmap!!.width * (tmpRatio - originRatio)) * xRatio
                        tmpY = (height - afterH) / 2.0f
//                        logV("center:$centerX,diffWid:$widthDiffVal,ratio:$xRatio")
                        bitmapMatrix.postTranslate( tmpX,tmpY)
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP->{
                if(event.pointerCount > 2)
                    return true

                curX = tmpX
                curY = tmpY
                ratio = tmpRatio
            }
            MotionEvent.ACTION_UP-> {
                if(pointerCount == 1) {
                    //单指触摸结束
                    if(isScaled) {
                        //缩放时处理
                        val curW = bitmap!!.width * ratio
                        val curH = bitmap!!.height * ratio
                        val maxX : Float
                        val maxY : Float
                        val minX : Float
                        val minY : Float
                        if(curW < screenWidth) {
                            maxX = (screenWidth - curW) / 2
                            minX = maxX
                        } else {
                            minX = screenWidth - curW
                        }

                        if(curH < screenHeight) {
                            maxY = (screenHeight - curH) / 2
                            minY = maxY
                        } else {
                            maxY = 0f
                            minY = screenHeight - curH
                        }

                        if(tmpX < minX)
                            curX = minX
                        else if(tmpX > 0)
                            curX = 0f
                        else curX = tmpX

                        if(tmpY < minY)
                            curY = minY
                        else if(tmpY > maxY)
                            curY = maxY
                        else curY = tmpY

                    }
                    bitmapMatrix.postTranslate(-tmpX,-tmpY)
                    bitmapMatrix.postTranslate(curX,curY)
                }
                else if(pointerCount == 2 && event.pointerCount == 1) {
                    if(tmpRatio <= originRatio) {
                        bitmapMatrix.setScale(originRatio,originRatio,0f,0f)
                        bitmapMatrix.postTranslate(startX,startY)
                        isScaled = false
                        curX = startX
                        curY = startY
                        ratio = originRatio
                    } else {
                        isScaled = true
                    }
                }
                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawARGB(255,0,0,0)
        var tmpBitmap = bitmap



        if(screenWidth != width || tmpBitmap == null) {
            screenWidth = width
            screenHeight = height

            if(tmpBitmap == null) {
                val opt = BitmapFactory.Options()
                opt.inJustDecodeBounds = true
                BitmapFactory.decodeFile(picPath,opt)
                opt.inJustDecodeBounds = false

                val measureRatioW = screenWidth * 1.0f / opt.outWidth
                val measureRatioH = screenHeight * 1.0f / opt.outHeight

                if(measureRatioW <= 1 && measureRatioH <= 1)
                    opt.inSampleSize = 1
                else opt.inSampleSize = if(measureRatioW > measureRatioH) measureRatioW.toInt() else measureRatioH.toInt()


                tmpBitmap = BitmapFactory.decodeFile(picPath,opt)
                bitmap = tmpBitmap


                val biggerSize = if(tmpBitmap!!.width > tmpBitmap!!.height) tmpBitmap!!.width else tmpBitmap!!.height
                val smallerSize = if(tmpBitmap!!.width > tmpBitmap!!.height) tmpBitmap!!.height else tmpBitmap!!.width

                maxmumRatio = MAX_SIZE / smallerSize
                minimumRatio = MIN_SIZE / biggerSize
            }


            bitmapMatrix.reset()
            val bitmapW = tmpBitmap.width
            val bitmapH = tmpBitmap.height

            val ratioW = width * 1.0f / bitmapW
            val ratioH = height * 1.0f / bitmapH
            originRatio = if(ratioW > ratioH) ratioH else ratioW

            val afterW = bitmapW * originRatio
            val afterH = bitmapH * originRatio

            startX = (width - afterW) / 2.0f
            startY = (height - afterH) / 2.0f

            bitmapMatrix.setScale(originRatio,originRatio,0f,0f)
            bitmapMatrix.postTranslate(startX,startY)

            curX = startX
            curY = startY
            ratio = originRatio


//            logV("min:$minimumRatio,max:$maxmumRatio,ratio:$originRatio")
        }

        canvas.drawBitmap(bitmap,bitmapMatrix,paint)
    }


    interface OnLongPressListener {
        fun onLongPress() {

        }
    }
}
