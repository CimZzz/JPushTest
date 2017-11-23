package com.example.yumi.jpushtest.base

/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
interface IFunction<in T : IPresenter<*, *>> {
    fun onInit(t: T?)
    fun onResume()
    fun onPause()
    fun onDestroy()
}