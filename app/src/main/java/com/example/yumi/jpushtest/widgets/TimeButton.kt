package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import com.example.yumi.jpushtest.utils.logV
import java.util.*

/**
 * Created by CimZzz(王彦雄) on 2017/12/6.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class TimeButton(context: Context?, attrs: AttributeSet?) : Button(context, attrs) {
    companion object {
        val STATE_INACTIVE = 0
        val STATE_ACTIVE = 1

        val TOTAL_SECOND = 10
    }

    lateinit var listener : View.OnClickListener
    private var timer : Timer? = null
    private var timerTask : TimerTask? = null

    var state = STATE_INACTIVE
    var curTime = 0L

    private val startText : String = text.toString()

    init {

        super.setOnClickListener {
            if(state == STATE_INACTIVE) {
                state = STATE_ACTIVE
                curTime = System.currentTimeMillis()
                listener.onClick(this)
                isSelected = true
                startTimer()
            }
            else return@setOnClickListener
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        listener = l!!
    }

    private fun startTimer() {
        stopTimer()
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                post {
                    val disTime = (TOTAL_SECOND - (System.currentTimeMillis() - curTime) / 1000).toInt()

                    logV("12312312")
                    if(disTime > 0)
                        text = "$disTime(秒)"
                    else resetTimer()

                    invalidate()
                }
            }
        }

        curTime = System.currentTimeMillis()
        timer!!.schedule(timerTask,0,1000)
    }

    private fun stopTimer() {
        text = startText
        timer?.cancel()
        timerTask?.cancel()
        isSelected = false
        timer = null
        timerTask = null
    }


    fun resetTimer() {
        state = STATE_INACTIVE
        stopTimer()
    }

}