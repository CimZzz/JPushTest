package com.example.yumi.jpushtest.base

/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class FunctionGroup<T : IPresenter<*, *>> {
    private val modules: ArrayList<IFunction<T>> = ArrayList()
    private var state: Byte = 0

    init {
        state = STATE_INIT
    }

    fun addModule(module: IFunction<T>, t: T?) {
        if (state == STATE_DESTORY)
            return
        if (state != STATE_INIT)
            module.onInit(t)
        if (state == STATE_RUN)
            module.onResume()
        modules.add(module)
    }

    internal fun onInit(t: T?) {
        state = STATE_INITED
        val length = modules.size
        for (i in 0 until length)
            modules[i].onInit(t)
    }

    fun onResume() {
        val length = modules.size
        for (i in 0 until length)
            modules[i].onResume()
        state = STATE_RUN
    }

    fun onPause() {
        state = STATE_STOP
        val length = modules.size
        for (i in 0 until length)
            modules[i].onPause()
    }

    fun onDestroy() {
        state = STATE_DESTORY
        for (module in modules)
            module.onDestroy()
    }

    companion object {
        private val STATE_INIT: Byte = 0
        private val STATE_INITED: Byte = 1
        private val STATE_RUN: Byte = 2
        private val STATE_STOP: Byte = 3
        private val STATE_DESTORY: Byte = 4
    }
}