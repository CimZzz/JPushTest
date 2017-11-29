package com.example.yumi.jpushtest.base

import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.virtualightning.stateframework.state.StateRecord


/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
interface IView {
    fun sendToast(toast: Any)
    fun registerBroadcastReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter)
    fun unregisterBroadcastReceiver(receiver: BroadcastReceiver)
    fun gainStateRecord() : StateRecord?
}