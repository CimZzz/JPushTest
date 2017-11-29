package com.example.yumi.jpushtest.base

import android.os.Bundle
import com.virtualightning.library.simple2develop.ui.ActionBarUI
import android.support.annotation.CallSuper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.Intent
import android.view.MotionEvent
import com.example.yumi.jpushtest.environment.CustomApplication
import com.example.yumi.jpushtest.utils.sendToast
import android.view.View
import android.widget.EditText
import android.view.inputmethod.InputMethodManager
import com.virtualightning.stateframework.state.ObserverBuilder
import com.virtualightning.stateframework.state.StateRecord


abstract class BaseUI<T : IPresenter<*,*>> : ActionBarUI() {
    var functionGroup : FunctionGroup<T>? = null
    var presenter : T? = null
    var isFirstCreate : Boolean = false
    var openAutoCancelSoft : Boolean = false



    protected fun openFunctionGroup() {
        functionGroup = FunctionGroup()
    }

    protected fun addFunctionModule(module: IFunction<T>) {
        if (functionGroup != null)
            functionGroup!!.addModule(module, presenter!!)
    }

    protected open fun onFirstShowView() {}
    protected open fun onFocusShowView() {}

    fun openAutoCancelSoftKeyBoard(isOpen : Boolean) {
        openAutoCancelSoft = isOpen
    }

    fun changeUI(baseUICls: Class<out BaseUI<*>>) {
        changeUI(baseUICls, null, null)
    }

    fun changeUI(baseUICls: Class<out BaseUI<*>>, bundle: Bundle) {
        changeUI(baseUICls, bundle, null)
    }

    fun changeUI(baseUICls: Class<out BaseUI<*>>, requestCode: Int?) {
        changeUI(baseUICls, null, requestCode)
    }

    fun changeUI(baseUICls: Class<out BaseUI<*>>, bundle: Bundle?, requestCode: Int?) {
        val intent = Intent(this, baseUICls)
        if (bundle != null)
            intent.putExtras(bundle)
        if (requestCode != null)
            startActivityForResult(intent, requestCode)
        else
            startActivity(intent)
    }

    fun getCustomApplication(): CustomApplication = application as CustomApplication

    fun sendToast(toast: Any) {
        sendToast(applicationContext, toast)
    }

    fun registerBroadcastReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter) {
        registerReceiver(receiver, intentFilter)
    }

    fun unregisterBroadcastReceiver(receiver: BroadcastReceiver) {
        unregisterReceiver(receiver)
    }

    open fun gainStateRecord() : StateRecord? = null

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (isFirstCreate) {
                isFirstCreate = false
                onFirstShowView()
            }
            onFocusShowView()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (openAutoCancelSoft && ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && isShouldHideInput(v, ev)) {
                if (hideInputMethod(v)) {
                    return true //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    open fun isShouldHideInput(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val leftTop = intArrayOf(0, 0)
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    private fun hideInputMethod(v: View): Boolean {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(imm.hideSoftInputFromWindow(v.windowToken, 0)) {
            v.clearFocus()
            return true
        }
        return false
    }


    @CallSuper
    override fun onResume() {
        super.onResume()
        if (functionGroup != null)
            functionGroup!!.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        if (functionGroup != null)
            functionGroup!!.onPause()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        if (functionGroup != null)
            functionGroup!!.onDestroy()
        if(presenter != null) {
            presenter!!.onDestroy()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (presenter != null)
            presenter!!.onSaveInstanceState(outState)
    }
}