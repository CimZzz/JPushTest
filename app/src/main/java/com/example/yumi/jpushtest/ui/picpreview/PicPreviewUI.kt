package com.example.yumi.jpushtest.ui.picpreview

import android.graphics.BitmapFactory
import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.ui_picpreview.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/23.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class PicPreviewUI : BaseUI<IPresenter<*, *>>() {
    companion object {
        val KV_PREVIEW_PATH = "kvpreviewpath"
    }

    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_picpreview)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
//        logV(intent.getStringExtra(KV_PREVIEW_PATH))
//        picPreviewGallery.setPicture(intent.getStringExtra(KV_PREVIEW_PATH))
//        picPreviewGallery.setPicture("/data/user/0/com.example.yumi.jpushtest/files/media/9974cd776f407222647dfed1/single_CimZzz/images/thumbnails/A92207DB2736442C8288557F354472AA")
    }

}