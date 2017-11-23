package com.example.yumi.jpushtest.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader

/**
 * Created by CimZzz(王彦雄) on 2017/11/23.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

fun loadPic(path : String,imageView : ImageView) {
    ImageLoader.getInstance().displayImage(path,imageView)
}
