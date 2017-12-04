package com.example.yumi.jpushtest.ui.chat

import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cn.jpush.im.android.api.JMessageClient
import com.bigkoo.alertview.AlertView
import com.czt.mp3recorder.MP3Recorder
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.entity.BaseChatItem
import com.example.yumi.jpushtest.entity.ImageChatItem
import com.example.yumi.jpushtest.entity.TextChatItem
import com.example.yumi.jpushtest.entity.VoiceChatItem
import com.example.yumi.jpushtest.environment.REQ_CHAT
import com.example.yumi.jpushtest.environment.RES_PIC_PATH_CHANGE
import com.example.yumi.jpushtest.environment.RES_PIC_SUCCESS
import com.example.yumi.jpushtest.environment.config.getFileModule
import com.example.yumi.jpushtest.ui.logintest.LoginTestUI
import com.example.yumi.jpushtest.ui.picpicker.PicPickerUI
import com.example.yumi.jpushtest.ui.picpreview.PicPreviewUI
import com.example.yumi.jpushtest.utils.isEmptyString
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.widgets.VoiceIconView
import com.virtualightning.gridpagerview.GridPagerAdapter
import com.virtualightning.gridpagerview.ViewPool
import com.virtualightning.gridpagerview.ViewWrapper
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.actionbar_chat.*
import kotlinx.android.synthetic.main.ui_chat2.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class ChatUI : BaseUI<ChatPresenter>() , IChatContract.View ,EasyPermissions.PermissionCallbacks{

    companion object {
        val KV_PIC_PATH = "kvpicpath"
        val KV_MESSAGE_ID = "kvmessageid"
    }

    var oppoUserName : String = ""
    var userName : String = ""
    var adapter : ChatAdapter = ChatAdapter()
    var recorder : MP3Recorder? = null
    var player : MediaPlayer? = null
    var curPlayItem : VoiceChatItem? = null

    /*Implement interface function*/
    override fun getOppositeUserName(): String = oppoUserName
    override fun getMyUserName(): String = userName

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?)=sendToast("获取录音权限失败，请重试")
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?)=Unit

    override fun setDownloadVoiceResult(result: Int, msgId: Int, filePath: String) {
        when(result) {
            ChatPresenter.RES_SUCCESS->{
                adapter.updateVoicePath(msgId,filePath)
                if(curPlayItem != null && curPlayItem!!.msgId == msgId) {
                    playVoice(curPlayItem!!)
                }
            }
            ChatPresenter.RES_FAILED-> {
                adapter.updateVoicePlayStatus(msgId,false)
                sendToast("下载语音失败，请检查网络后重试")
            }
            ChatPresenter.RES_INVALID-> {
            }
        }
    }

    override fun addMsg(msg: BaseChatItem) {
        logV("Add Msg ${msg.javaClass.simpleName}")
        val isNeedScroll = chatList.isListViewLastItemVisible
        adapter.addMsg(msg)
        logV("Added Msg ${msg.javaClass.simpleName}")
        if(isNeedScroll)
            chatList.recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    override fun sendMessageResult(msgId: Int, result: Int) {
        adapter.updateMsgStatus(msgId,result)

    }


    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_chat2)
        creater.setActionBarID(R.layout.actionbar_chat)
        presenter = ChatPresenter(this,ChatMethod())
        openAutoCancelSoft = true
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        userName = intent.extras.getString("UserName")
        chatTitle.text = userName
        oppoUserName = intent.extras.getString("OppositeUserName")

        chatBack.setOnClickListener { quit() }

        chatList.setAdapter(adapter)
        chatList.setLayoutManager(LinearLayoutManager(this,OrientationHelper.VERTICAL,false))

        chatMic.setOnClickListener {
            chatMic.visibility = View.GONE
            chatKeyboard.visibility = View.VISIBLE

            chatInput.visibility = View.GONE
            chatVoice.visibility = View.VISIBLE

            chatSend.visibility = View.GONE
            chatMore.visibility = View.VISIBLE

            val fadeAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            val translateAnim = AnimationUtils.loadAnimation(this,R.anim.translate_in_from_bot)
            chatKeyboard.startAnimation(fadeAnim)
            chatVoice.startAnimation(translateAnim)
            if(chatMore.visibility == View.GONE)
                chatMore.startAnimation(fadeAnim)
        }

        chatKeyboard.setOnClickListener {
            chatMic.visibility = View.VISIBLE
            chatKeyboard.visibility = View.GONE

            chatInput.visibility = View.VISIBLE
            chatVoice.visibility = View.GONE

            val fadeAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            val translateAnim = AnimationUtils.loadAnimation(this,R.anim.translate_in_from_bot)
            chatMic.startAnimation(fadeAnim)
            chatInput.startAnimation(translateAnim)

            if(isEmptyString(chatInput.editableText.toString())) {
                chatSend.visibility = View.GONE
                chatMore.visibility = View.VISIBLE
            } else {
                val scaleAnim = AnimationUtils.loadAnimation(this,R.anim.scale_bigger)
                chatSend.visibility = View.VISIBLE
                chatMore.visibility = View.GONE
                chatOtherFunBar.visibility = View.GONE
                chatMore.clearAnimation()
                chatSend.startAnimation(scaleAnim)
            }
        }

        chatInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(isEmptyString(s.toString())) {
                    if(chatMore.visibility == View.VISIBLE)
                        return
                    chatSend.visibility = View.GONE
                    chatMore.visibility = View.VISIBLE
                    val fadeAnim = AnimationUtils.loadAnimation(this@ChatUI,R.anim.fade_in)
                    chatMore.startAnimation(fadeAnim)
                } else {
                    if(chatSend.visibility == View.VISIBLE)
                        return
                    chatSend.visibility = View.VISIBLE
                    chatMore.visibility = View.GONE
                    val scaleAnim = AnimationUtils.loadAnimation(this@ChatUI,R.anim.scale_bigger)
                    chatSend.startAnimation(scaleAnim)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        chatInput.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus && chatOtherFunBar.visibility == View.VISIBLE) {
                chatOtherFunBar.visibility = View.GONE
                chatMore.clearAnimation()
                chatList.recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        chatVoice.setOnTouchListener(object : View.OnTouchListener {
            var isNeedSend = true
            var fileName = ""
            var startMillisecond = 0L

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val action = event.action
                when(action) {
                    MotionEvent.ACTION_DOWN-> {
                        chatVoice.text = "松开发送语音"
                        isNeedSend = true
                        //开始录音
                        if(requireSomePermission()) {
                            val dir = getDir("cache", Context.MODE_PRIVATE)
                            val file = File(dir,"${System.currentTimeMillis()}.mp3")
                            fileName = file.absolutePath
                            startMillisecond = System.currentTimeMillis()
                            startRecord(file)
                        }
                        else {
                            event.action = MotionEvent.ACTION_CANCEL
                            chatVoice.text = "按住发送语音"
                            return false
                        }
                    }
                    MotionEvent.ACTION_MOVE-> {
                        if(event.x < 0 || event.x > chatVoice.width || event.y < -chatVoice.height || event.y > chatVoice.height) {
                            if(isNeedSend) {
                                chatVoice.text = "松开取消发送"
                                isNeedSend = false
                            }
                        }
                        else if (!isNeedSend) {
                            chatVoice.text = "松开发送语音"
                            isNeedSend = true
                        }
                    }
                    MotionEvent.ACTION_UP-> {
                        if(isNeedSend) {
                            var second = ((System.currentTimeMillis() - startMillisecond) / 1000).toInt()
                            if(second == 0)
                                second = 1
                            stopRecord()
                            presenter!!.sendVoiceMsg(fileName,second)
                        }
                        cancelRecord()
                        chatVoice.text = "按住发送语音"
                    }
                }
                return true
            }

        })

        chatMore.setOnClickListener {
            if(chatOtherFunBar.visibility == View.VISIBLE) {
                chatOtherFunBar.visibility = View.GONE
                val rotateAnim = AnimationUtils.loadAnimation(this,R.anim.rotate_0_180)
                chatMore.startAnimation(rotateAnim)
            } else {
                chatOtherFunBar.visibility = View.VISIBLE
                val rotateAnim = AnimationUtils.loadAnimation(this,R.anim.rotate_180_0)
                chatMore.startAnimation(rotateAnim)
            }
        }

        chatSend.setOnClickListener {
            val text = chatInput.editableText.toString()
            chatInput.setText("")
            presenter!!.sendTextMsg(text)
        }

        chatOtherFunBar.setAdapter(object : GridPagerAdapter(){
            val itemList = arrayListOf(
                    Item(R.drawable.icon_picture,"发送图片"),
                    Item(R.drawable.icon_order,"发送订单")
            )

            val clickListener = View.OnClickListener {
                onBottomFunctionClick(it.tag as Int)
            }


            override fun getItemCount(): Int = itemList.size

            override fun getChildView(context: Context, container: ViewGroup, viewPool: ViewPool, position: Int): ViewWrapper {
                val item = itemList[position]
                val itemContainer = FrameLayout(context)
                val view = LayoutInflater.from(context).inflate(R.layout.item_chat_bottom,itemContainer,false)
                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.CENTER)
                val imageView = view.findViewById<ImageView>(R.id.chatBotItemPic)
                val textView = view.findViewById<TextView>(R.id.chatBotItemText)
                imageView.setImageResource(item.srcId)
                textView.text = item.text
                itemContainer.tag = position
                itemContainer.addView(view,params)
                itemContainer.setOnClickListener(clickListener)
                return ViewWrapper(0,itemContainer)
            }

            override fun getItemAt(position: Int): Any? = null

            inner class Item(val srcId : Int, val text : String)
        })

        adapter.listener = object : ChatAdapter.IContentClickListener {
            override fun onUploadFailedClick(item: BaseChatItem) {
                if(item is TextChatItem) {
                    adapter.removeMsg(item.msgId)
                    presenter!!.sendTextMsg(item.message)
                }
                else if(item is ImageChatItem){
                    adapter.removeMsg(item.msgId)
                    presenter!!.sendPicMsg(item.imgLink!!)
                }
                else if(item is VoiceChatItem) {
                    adapter.removeMsg(item.msgId)
                    presenter!!.sendVoiceMsg(item.voiceLink,item.second)
                }
            }

            override fun onVoiceContentClick(item: VoiceChatItem, view: VoiceIconView) {
                if(item.downloadFlag == BaseChatItem.DOWNLOAD_DOWNLOADED) {
                    if(stopPlayingVoice() == item.msgId)
                        return
                    playVoice(item)
                } else {
                    if(stopPlayingVoice() == item.msgId)
                        return
                    curPlayItem = item
                    adapter.updateVoicePlayStatus(item.msgId,true)
                    presenter!!.downloadVoice(item.msgId,item.media_id,item.media_crc32)
                }
            }

            override fun onTextContentClick(text: String) {
            }

            override fun onImageContentClick(item: ImageChatItem, view: View) {
                val intent = Intent(this@ChatUI,PicPreviewUI::class.java)
                if(item.downloadFlag == BaseChatItem.DOWNLOAD_NOT_DOWNLOAD) {
                    intent.putExtra(PicPreviewUI.KV_ALLOW_DOWNLOAD,true)
                    intent.putExtra(PicPreviewUI.KV_MESSAGE_ID,item.msgId)
                    intent.putExtra(PicPreviewUI.KV_MEDIA_ID,item.media_id)
                    intent.putExtra(PicPreviewUI.KV_MEDIA_CRC,item.media_crc32)
                    intent.putExtra(PicPreviewUI.KV_FILE_SIZE,item.fileSize)
                    intent.putExtra(PicPreviewUI.KV_USER_NAME,userName)
                }
                intent.putExtra(PicPreviewUI.KV_PREVIEW_PATH,item.imgLink)

                startActivityForResult(intent
                        , REQ_CHAT
                        , ActivityOptionsCompat.makeSceneTransitionAnimation(this@ChatUI
                        , view
                        , "SHARE")
                        .toBundle())
            }


        }
    }

    fun onBottomFunctionClick(position : Int) {
        when(position) {
            0-> {

                AlertView.Builder().setContext(this)
                    .setStyle(AlertView.Style.ActionSheet)
                    .setTitle("选择获取图片方式")
                    .setMessage(null)
                    .setCancelText("取消")
                    .setDestructive("从相册中选择")
                    .setOthers(null)
                    .setOnItemClickListener({ o, position ->
                        val alertView = o as AlertView
                        when(position) {
                            -1->alertView.dismiss()
                            0->changeUI(PicPickerUI::class.java, REQ_CHAT)
                        }
                    })
                    .build()
                    .show()
            }
        }
    }

    fun playVoice(voiceChatItem: VoiceChatItem) {
        this.curPlayItem = voiceChatItem
        adapter.updateVoicePlayStatus(voiceChatItem.msgId,true)
        player = MediaPlayer()
        player!!.setDataSource(curPlayItem!!.voiceLink)
        player!!.setOnPreparedListener {
            if(player != null)
                player!!.start()
        }
        player!!.setOnCompletionListener {
            stopPlayingVoice()
            adapter.updateVoicePlayStatus(voiceChatItem.msgId,false)
        }
        player!!.prepareAsync()
    }

    fun stopPlayingVoice() : Int{
        if(curPlayItem != null) {
            val curId = curPlayItem!!.msgId
            player!!.stop()
            player!!.release()
            adapter.updateVoicePlayStatus(curPlayItem!!.msgId,false)
            curPlayItem = null
            return curId
        }

        return -1
    }

    fun startRecord(file: File) {
        recorder = MP3Recorder(file)
        recorder!!.start()
    }

    fun stopRecord() {
        recorder!!.isRecording
        recorder!!.stop()
    }

    fun cancelRecord() {
        if(recorder != null && recorder!!.isRecording)
            recorder!!.stop()
        recorder = null
    }

    private fun requireSomePermission() : Boolean {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO)) {
            return true
        } else {
            EasyPermissions.requestPermissions(this, "发送语音功能需要录音权限，请确认",
                    123, Manifest.permission.RECORD_AUDIO)
            return false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode != REQ_CHAT)
            return super.onActivityResult(requestCode, resultCode, data)

        when(resultCode) {
            RES_PIC_SUCCESS-> {
                //send pic msg
                val path = data!!.getStringExtra(KV_PIC_PATH).replace("file:///","")
                presenter!!.sendPicMsg(path)
            }
            RES_PIC_PATH_CHANGE -> {
                val path = data!!.getStringExtra(KV_PIC_PATH)
                val msg = data.getIntExtra(KV_MESSAGE_ID,-1)
                adapter.updateImagePath(msg,path)
            }
        }
    }

    override fun isShouldHideInput(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val leftTop = intArrayOf(0, 0)
            v.getLocationInWindow(leftTop)
            var left = leftTop[0]
            var top = leftTop[1]
            var bottom = top + v.getHeight()
            var right = left + v.getWidth()
            var isInside = event.x > left && event.x < right && event.y > top && event.y < bottom
            if(isInside)
                return false
            if(chatSend.visibility != View.VISIBLE)
                return true
            chatSend.getLocationInWindow(leftTop)
            left = leftTop[0]
            top = leftTop[1]
            bottom = top + chatSend.height
            right = left + chatSend.width
            isInside = event.x > left && event.x < right && event.y > top && event.y < bottom
            return !isInside
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            quit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun quit() {
        JMessageClient.logout()
        finish()
        changeUI(LoginTestUI::class.java)
    }
}