package com.example.yumi.jpushtest.ui.login

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class EventSteam(val handleMethod : (String)->Boolean) {
    var subBackEventSteam : EventSteam? = null
    var parentBackEventSteam : EventSteam? = null

    fun checkEvent(event : String) : Boolean {
        return if(subBackEventSteam != null && subBackEventSteam!!.checkEvent(event))
            true
        else handleMethod(event)
    }

    fun clear() {
        subBackEventSteam = null
        if(parentBackEventSteam != null)
            parentBackEventSteam!!.subBackEventSteam = null
        parentBackEventSteam = null
    }

    fun addEvent(eventSteam: EventSteam?) {
        subBackEventSteam = eventSteam
        if(eventSteam != null)
            eventSteam.parentBackEventSteam = this
    }
}