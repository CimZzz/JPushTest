package com.example.yumi.jpushtest.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.example.yumi.jpushtest.environment.CustomApplication
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.yumi.jpushtest.ui.login.BackEventSteam
import com.example.yumi.jpushtest.utils.ModuleGetter
import com.example.yumi.jpushtest.utils.sendToast
import com.virtualightning.library.simple2develop.state.Analyzer


/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
abstract class BasePager<T : IPresenter<*, *>> : Fragment() {
    var customApplication: CustomApplication? = null
    protected var presenter: T? = null
    private var rootView: View? = null
    private val getter = ModuleGetter(this)
    var backEventSteam : BackEventSteam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customApplication = activity.application as CustomApplication
        presenter = getPresenter(savedInstanceState)
        init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(initViewID(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        onViewInitialization(savedInstanceState)
    }

    protected abstract fun init()
    protected abstract fun initViewID(): Int
    protected abstract fun onViewInitialization(savedInstanceState: Bundle?)
    protected open fun getPresenter(savedInstanceState: Bundle?): T? = null

    fun getModuleGetter() : ModuleGetter = getter

    fun findViewById(resId: Int): View = rootView!!.findViewById(resId)

    fun changeUI(baseUICls: Class<out BaseUI<*>>, requestCode: Int?) {
        changeUI(baseUICls, null, requestCode)
    }

    @JvmOverloads
    fun changeUI(baseUICls: Class<out BaseUI<*>>, bundle: Bundle? = null, requestCode: Int? = null) {
        val intent = Intent(getContext(), baseUICls)
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