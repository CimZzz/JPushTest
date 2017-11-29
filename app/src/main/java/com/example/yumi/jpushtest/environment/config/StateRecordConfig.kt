package com.example.yumi.jpushtest.environment.config

import com.virtualightning.stateframework.constant.ReferenceType
import com.virtualightning.stateframework.constant.RunType
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.ObserverBuilder
import com.virtualightning.stateframework.state.StateRecord

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

fun StateRecord.registerObserver(stateId : String,observer: BaseObserver) {
    registerObserver(ObserverBuilder()
            .stateId(stateId)
            .allowStop(false)
            .refType(ReferenceType.STRONG)
            .observer(observer))
}

fun genDefaultObserverBuilder(stateId : String,body : ()->BaseObserver) : ObserverBuilder {
    return ObserverBuilder()
            .stateId(stateId)
            .allowStop(false)
            .refType(ReferenceType.STRONG)
            .observer(body.invoke())
}