package com.example.yumi.jpushtest.ui.picpreview

import cn.jiguang.api.JCoreInterface
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.model.Conversation
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.downloadOriginImage
import com.example.yumi.jpushtest.utils.logV
import java.io.File

/**
 * Created by CimZzz(王彦雄) on 11/26/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */

class PicPreviewPresenter(view : IPicPreviewContract.View, method :IPicPreviewContract.Method) : IPresenter<IPicPreviewContract.View, IPicPreviewContract.Method>(view, method) {
    companion object {
        val RES_SUCCESS = 0
        val RES_FAILED = 1
        val RES_INVALID = 2
    }

    fun downloadOriginImage(userName:String,mediaId : String, mediaCrc : Long) {
        downloadOriginImage(userName, mediaId, mediaCrc,{
            p0,p1,p2->
            logV("Download : $p0,$p1")
            if(p0 == 0)
                view.setDownloadResult(RES_SUCCESS,p2!!.absolutePath)
            else view.setDownloadResult(RES_FAILED,"")
        })
    }
}