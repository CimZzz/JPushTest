package com.example.yumi.jpushtest.extend

import com.alibaba.fastjson.JSONObject
import com.virtualightning.stateframework.state.BaseObserver

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
abstract class HTTPJSONObserver : BaseObserver() {

    abstract fun onHttpCallBack(isSuccess:Boolean,json:JSONObject?,msg:String?)

    override fun notify(vararg objects: Any?) {
        onHttpCallBack(objects[0] as Boolean,objects[1] as JSONObject?,objects[2] as String?)
    }

}