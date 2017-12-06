package com.example.yumi.jpushtest.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import com.example.yumi.jpushtest.environment.CustomApplication
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.bigkoo.svprogresshud.SVProgressHUD
import com.example.yumi.jpushtest.ui.login.EventSteam
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.utils.sendToast
import com.virtualightning.stateframework.state.StateRecord


/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
abstract class BasePager<T : IPresenter<*, *>> : Fragment() {
    var customApplication: CustomApplication? = null
    protected var presenter: T? = null
    var rootView: View? = null
    var eventSteam: EventSteam? = null

    private var loadingBar : SVProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customApplication = activity.application as CustomApplication
        presenter = getPresenter(savedInstanceState)
        init()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(rootView == null)
            rootView = inflater.inflate(initViewID(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        onViewInitialization(savedInstanceState)
    }


    protected abstract fun initViewID(): Int
    protected open fun getPresenter(savedInstanceState: Bundle?): T? = null
    protected open fun init()=Unit
    protected open fun onViewInitialization(savedInstanceState: Bundle?) = Unit

    fun<T : View> findViewById(id : Int) : T = rootView!!.findViewById(id)


    fun showInfoBar(text : String) {
        SVProgressHUD(context).showInfoWithStatus(text)
    }

    fun showLoadingBar(text : String) {
        if(loadingBar != null) {
            loadingBar!!.dismissImmediately()
            loadingBar = null
        }

        loadingBar = SVProgressHUD(context)
        loadingBar!!.showInfoWithStatus(text)
    }

    fun closeLoadingBar() {
        if(loadingBar != null) {
            loadingBar!!.dismissImmediately()
            loadingBar = null
        }
    }

    fun changeUI(baseUICls: Class<out BaseUI<*>>, requestCode: Int?) {
        changeUI(baseUICls, null, requestCode)
    }

    @JvmOverloads
    fun changeUI(baseUICls: Class<out BaseUI<*>>, bundle: Bundle? = null, requestCode: Int? = null) {
        val intent = Intent(context, baseUICls)
        if (bundle != null)
            intent.putExtras(bundle)
        if (requestCode != null)
            startActivityForResult(intent, requestCode)
        else
            startActivity(intent)
    }

    fun sendToast(toast: String) {
        sendToast(customApplication!!.applicationContext, toast)
    }

    fun registerBroadcastReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter) {
        customApplication!!.applicationContext.registerReceiver(receiver, intentFilter)
    }

    fun unregisterBroadcastReceiver(receiver: BroadcastReceiver) {
        customApplication!!.applicationContext.unregisterReceiver(receiver)
    }


    open fun gainStateRecord() : StateRecord? = null

    @CallSuper
    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (presenter != null)
            presenter!!.onSaveInstanceState(outState)
    }
}