package com.example.yumi.jpushtest.environment.config

import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IMethod
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.base.IView

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */


fun BaseUI<out IPresenter<IView, IMethod>>.getFileModule() = getCustomApplication().fileModule
fun BaseUI<out IPresenter<IView, IMethod>>.getHttpModule() = getCustomApplication().httpModule
fun BaseUI<out IPresenter<IView, IMethod>>.getIMModule() = getCustomApplication().imModule