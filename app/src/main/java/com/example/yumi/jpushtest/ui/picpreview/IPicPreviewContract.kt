package com.example.yumi.jpushtest.ui.picpreview

import com.example.yumi.jpushtest.base.IMethod
import com.example.yumi.jpushtest.base.IView

/**
 * Created by CimZzz(王彦雄) on 11/26/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */

interface IPicPreviewContract {
    interface View : IView{
        fun setDownloadResult(result:Int, filePath : String)
    }
    interface Method : IMethod{

    }
}