package com.example.yumi.jpushtest.ui.picpicker

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.ui_picpicker.*
import pub.devrel.easypermissions.EasyPermissions
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.RES_PIC_SUCCESS
import com.example.yumi.jpushtest.ui.chat.ChatUI.Companion.KV_PIC_PATH
import com.example.yumi.jpushtest.utils.loadPic
import com.example.yumi.jpushtest.utils.logV
import kotlinx.android.synthetic.main.actionbar_picpicker.*


/**
 * Created by CimZzz(王彦雄) on 2017/11/23.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class PicPickerUI : BaseUI<IPresenter<*,*>>(),EasyPermissions.PermissionCallbacks {

    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_picpicker)
        creater.setActionBarID(R.layout.actionbar_picpicker)
    }
    override fun onViewInit(savedInstanceState: Bundle?) {
        picPickerTitle.text = "选取图片"

        picPickerBack.setOnClickListener {
            finish()
        }

        requireSomePermission()
    }

    private fun init() {
        val fileNames = ArrayList<String>()
        val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        if(cursor != null ) {
            while (cursor.moveToNext()) {
                //获取图片的生成日期
                val data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA))

                fileNames.add("file:///${String(data, 0, data.size - 1)}")
            }
            cursor.close()
        }
        picPickerGrid.layoutManager = GridLayoutManager(this,4,OrientationHelper.VERTICAL,false)
        picPickerGrid.adapter = object : Adapter<RecyclerView.ViewHolder>() {
            val gridClickListener = View.OnClickListener {
                val filePath = fileNames[it.tag as Int]
                val intent = Intent()
                intent.putExtra(KV_PIC_PATH,filePath)
                setResult(RES_PIC_SUCCESS,intent)
                finish()
            }
            val gridLongClickListener = View.OnCreateContextMenuListener {
                menu, v, menuInfo ->
                val filePath = fileNames[v.tag as Int]
            }

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
                    = ItemHolder(LayoutInflater.from(this@PicPickerUI).inflate(R.layout.item_picpicker,parent,false))

            override fun getItemCount(): Int = fileNames.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
                val itemHolder = holder as ItemHolder
                itemHolder.itemView.tag = position
                val width = picPickerGrid.width / 4
                val params = itemHolder.itemView.layoutParams
                params.width = width
                itemHolder.itemView.layoutParams = params
                loadPic(fileNames[position],itemHolder.imgView)
            }

            inner class ItemHolder(item : View) : RecyclerView.ViewHolder(item) {
                val imgView = item.findViewById<ImageView>(R.id.picPickerItemImg)
                init {
                    itemView.setOnClickListener(gridClickListener)
                    itemView.setOnCreateContextMenuListener(gridLongClickListener)
                }
            }
        }
    }

    override fun onFirstShowView() {
        val adapter = picPickerGrid.adapter
        picPickerGrid.adapter = null
        picPickerGrid.adapter = adapter
    }

    private fun requireSomePermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            init()
        } else {
            EasyPermissions.requestPermissions(this, "选取图片需要读取本地图片权限，请确认",
                    123, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        init()
    }

}