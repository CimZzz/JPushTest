package com.example.yumi.jpushtest.ui.picpreview

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.environment.RES_PIC_PATH_CHANGE
import com.example.yumi.jpushtest.ui.chat.ChatUI
import com.example.yumi.jpushtest.utils.convert2FileSize
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.widgets.PicturePreviewView
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.ui_picpreview.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/23.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class PicPreviewUI : BaseUI<PicPreviewPresenter>(),IPicPreviewContract.View {

    companion object {
        val KV_PREVIEW_PATH = "kvpreviewpath"
        val KV_ALLOW_DOWNLOAD= "kvallowdownload"
        val KV_MESSAGE_ID = "kvmessageid"
        val KV_MEDIA_ID = "kvmediaid"
        val KV_MEDIA_CRC = "kvmediacrc"
        val KV_FILE_SIZE = "kvfilesize"
        val KV_USER_NAME = "kvusername"
    }

    var isAllowDownload = false
    var mediaId = ""
    var mediaCrc = 0L
    var fileSize = 0L
    var msgId = 0
    var userName = ""

    /*implement interface function*/
    override fun setDownloadResult(result: Int, filePath: String) {
        when(result) {
            PicPreviewPresenter.RES_SUCCESS-> {
                picPreviewDownload.visibility = View.GONE
                picPreviewGallery.setPicture(filePath)
                val intent = Intent()
                intent.putExtra(ChatUI.KV_MESSAGE_ID,msgId)
                intent.putExtra(ChatUI.KV_PIC_PATH,filePath)
                setResult(RES_PIC_PATH_CHANGE,intent)
            }
            PicPreviewPresenter.RES_FAILED-> {
                picPreviewDownload.text = "下载原图 ${convert2FileSize(fileSize)}"
                picPreviewDownload.isSelected = false
                sendToast("下载失败请检查网络后重试")
            }
        }
    }


    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_picpreview)
        presenter = PicPreviewPresenter(this,PicPreviewMethod())
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        isAllowDownload = intent.getBooleanExtra(KV_ALLOW_DOWNLOAD,false)
        if(isAllowDownload) {
            mediaId = intent.getStringExtra(KV_MEDIA_ID)
            msgId = intent.getIntExtra(KV_MESSAGE_ID,0)
            mediaCrc = intent.getLongExtra(KV_MEDIA_CRC,0)
            fileSize = intent.getLongExtra(KV_FILE_SIZE,0)
            userName = intent.getStringExtra(KV_USER_NAME)

            picPreviewDownload.visibility = View.VISIBLE
            picPreviewDownload.text = "下载原图 ${convert2FileSize(fileSize)}"
            picPreviewDownload.setOnClickListener {
                if(picPreviewDownload.isSelected)
                    return@setOnClickListener
                picPreviewDownload.isSelected = true
                picPreviewDownload.text = "正在下载中..."
                presenter!!.downloadOriginImage(userName,mediaId, mediaCrc)
            }
        }
        else picPreviewDownload.visibility = View.GONE


        picPreviewGallery.setPicture(intent.getStringExtra(KV_PREVIEW_PATH))
        picPreviewGallery.listener = object : PicturePreviewView.OnLongPressListener {
            override fun onLongPress() {
                logV("Trigger long press! Looper : ${Looper.myLooper() == Looper.getMainLooper()}")
            }
        }

//        logV(intent.getStringExtra(KV_PREVIEW_PATH))
    }


}