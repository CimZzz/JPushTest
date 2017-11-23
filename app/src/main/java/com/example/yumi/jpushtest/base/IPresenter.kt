package com.example.yumi.jpushtest.base

import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
abstract class IPresenter<out T : IView, out E : IMethod> (
        val view : T,
        val method : E
){
    open fun onSaveInstanceState(outState: Bundle?) {}



    fun registerBroadcastReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
        view.registerBroadcastReceiver(receiver, filter)
    }
    fun unregisterBroadcastReceiver(receiver: BroadcastReceiver) {
        view.unregisterBroadcastReceiver(receiver)
    }

    open fun onDestroy() {

    }
}