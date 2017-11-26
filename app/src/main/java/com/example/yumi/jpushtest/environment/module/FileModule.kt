package com.example.yumi.jpushtest.environment.module

import android.content.Context
import android.os.Environment
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.MEDIA_MOUNTED
import com.example.yumi.jpushtest.base.IModule
import com.example.yumi.jpushtest.environment.APP_NAME
import java.io.File


/**
 * Created by CimZzz(王彦雄) on 11/24/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class FileModule(context: Context) : IModule(context) {
    val rootDirPath: String = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        Environment.getExternalStorageDirectory().toString() + File.separator + APP_NAME
    else
        context.filesDir.path

}