package com.example.yumi.jpushtest.environment.config

import android.content.Context
import com.nostra13.universalimageloader.utils.StorageUtils
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import java.io.File
import java.io.File.separator



/**
 * Created by CimZzz(王彦雄) on 11/24/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */

object ImageLoaderConfig {
    /*ImageLoader 配置参数*/
    val MAX_FILE_COUNT = 1024//最大磁盘缓存文件数
    val MAX_SAVE_WIDTH = 400//保存的缓存文件最大宽度
    val MAX_SAVE_HEIGHT = 800//保存的缓存文件最大高度
    val MAX_THREAD_COUNT = 3//最大并行线程数
    val MAX_MEMORY_OCCUPY = (Runtime.getRuntime().maxMemory() / 32).toInt()//最大占用内存
    val MAX_DISK_OCCUPY = 20 * 1024 * 1024//最大占用磁盘空间
    val CACHE_PATH = File.separator + "ImageLoaderCache"
    fun init(context: Context, rootDirPath: String) {
        val imageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.Builder(context)
                .diskCacheFileCount(MAX_FILE_COUNT)
                .memoryCacheExtraOptions(MAX_SAVE_WIDTH, MAX_SAVE_HEIGHT) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(MAX_THREAD_COUNT) //线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()//同样图像不同尺寸只存一张
                .memoryCacheSize(MAX_MEMORY_OCCUPY) //申请内存大小
                .memoryCache(WeakMemoryCache()) //弱缓存
                .diskCacheSize(MAX_DISK_OCCUPY) //申请硬盘缓存大小
                .diskCacheFileNameGenerator(Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .diskCacheFileCount(100)//缓存的文件数量
                .diskCache(UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(context, rootDirPath + CACHE_PATH)))// 自定义缓存路径
                .build())
    }
}