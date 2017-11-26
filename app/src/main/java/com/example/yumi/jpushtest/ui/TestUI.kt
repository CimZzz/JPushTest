package com.example.yumi.jpushtest.ui

import android.content.Context
import android.os.Bundle
import android.text.Spanned.SPAN_COMPOSING
import android.view.KeyEvent
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.ExtractedText
import android.view.inputmethod.InputMethodManager
import cn.jiguang.api.JCoreInterface
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.api.BasicCallback
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.loadPic
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.constant.ReferenceType
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.ObserverBuilder
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.ui_chat.*
import kotlinx.android.synthetic.main.ui_test.*
import java.io.File

/**
 * Created by CimZzz(王彦雄) on 11/25/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class TestUI : BaseUI<IPresenter<*,*>>() {
    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_test)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        testStart.setOnClickListener {
            testView.setText("")
        }
    }

    operator fun get(i : Int) : String = "${i + 10}"
}