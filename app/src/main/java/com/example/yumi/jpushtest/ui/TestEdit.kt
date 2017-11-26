package com.example.yumi.jpushtest.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.*
import android.widget.EditText
import com.example.yumi.jpushtest.utils.logV
import android.view.inputmethod.InputConnection
import android.view.inputmethod.EditorInfo



/**
 * Created by CimZzz(王彦雄) on 11/26/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class TestEdit(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
    var conn : MyInputConnectionWrapper? = null
    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val inputConnection = super.onCreateInputConnection(outAttrs) ?: return null
        conn = MyInputConnectionWrapper(inputConnection, true)
        return conn
    }

    inner class MyInputConnectionWrapper(target: InputConnection?, mutable: Boolean) : InputConnectionWrapper(target, mutable) {

    }
}