package com.example.yumi.jpushtest.ui.chat2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import com.czt.mp3recorder.MP3Recorder
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.entity.*
import com.example.yumi.jpushtest.environment.REQ_CHAT
import com.example.yumi.jpushtest.environment.RES_PIC_PATH_CHANGE
import com.example.yumi.jpushtest.environment.RES_PIC_SUCCESS
import com.example.yumi.jpushtest.environment.config.getIMModule
import com.example.yumi.jpushtest.environment.config.wholeObserver
import com.example.yumi.jpushtest.environment.module.IMModule
import com.example.yumi.jpushtest.ui.chat.ChatUI
import com.example.yumi.jpushtest.ui.picpicker.PicPickerUI
import com.example.yumi.jpushtest.ui.picpreview.PicPreviewUI
import com.example.yumi.jpushtest.utils.downloadVoice
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.actionbar_user_back.*
import kotlinx.android.synthetic.main.ui_chat.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ChatUI2 : BaseUI<ChatPresenter>() {
    companion object {
        val KV_OPPOSITE_USER_NAME = "OppositeUserName"
        val KV_OPPOSITE_HEAD_PIC = "OppositeHeadPic"
        val KV_OPPOSITE_USER_STATUS = "OppositeUserStatus"
    }
    val stateRecord : StateRecord = StateRecord.newInstance(ChatUI2::class.java)

    lateinit var oppositeUserName : String
    lateinit var headPic : String
    lateinit var adapter : ChatAdapter2

    var recorder : MP3Recorder? = null
    var player : MediaPlayer? = null
    var curPlayItem : VoiceChatItem? = null

    var isOnline = false


    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_chat)
        creater.setActionBarID(R.layout.actionbar_user_back)
        openAutoCancelSoft = true

        stateRecord.wholeObserver(IMModule.STATE_NEW_MESSAGE,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val msg = objects[0] as BaseChatItem
                addMessage(msg)
            }
        })
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        oppositeUserName = intent.getStringExtra(KV_OPPOSITE_USER_NAME)
        headPic = intent.getStringExtra(KV_OPPOSITE_HEAD_PIC)?:""
        isOnline = intent.getBooleanExtra(KV_OPPOSITE_USER_STATUS,true)
        getIMModule().login()
        adapter = ChatAdapter2(getIMModule().curUserName)

        actionbarBack.setOnClickListener {
            finish()
        }
        actionbarHeadPic.setImageResource(R.drawable.icon_dog)
        actionbarUserName.text = oppositeUserName

        if(isOnline) {
            actionbarStatus.color = resources.getColor(R.color.actionbar_OnlineColor)
            actionbarStatusText.setTextColor(resources.getColor(R.color.actionbar_OnlineTextColor))
        } else {
            actionbarStatus.color = resources.getColor(R.color.actionbar_OfflineColor)
            actionbarStatusText.setTextColor(resources.getColor(R.color.actionbar_OfflineTextColor))
        }

        chatInput.setOnEditorActionListener {
            v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                val item = getIMModule().sendTextMsg(v.text.toString(),oppositeUserName)
                addMessage(item)
                v.text = ""
                true
            }
            else false
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
                            addMessage(getIMModule().sendVoiceMsg(fileName,second,oppositeUserName))
                        }
                        cancelRecord()
                        chatVoice.text = "按住发送语音"
                    }
                }
                return true
            }

        })
        chatBotVoice.setOnClickListener {
            chatInput.visibility = View.GONE
            chatBotVoice.visibility = View.GONE
            chatVoice.visibility = View.VISIBLE
            chatBotText.visibility = View.VISIBLE

            val fadeAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            val translateAnim = AnimationUtils.loadAnimation(this,R.anim.translate_in_from_bot)
            chatVoice.startAnimation(translateAnim)
            chatBotText.startAnimation(fadeAnim)
        }
        chatBotText.setOnClickListener {
            chatInput.visibility = View.VISIBLE
            chatBotVoice.visibility = View.VISIBLE
            chatVoice.visibility = View.GONE
            chatBotText.visibility = View.GONE

            val fadeAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            val translateAnim = AnimationUtils.loadAnimation(this,R.anim.translate_in_from_bot)
            chatInput.startAnimation(translateAnim)
            chatBotVoice.startAnimation(fadeAnim)
        }
        chatBotPic.setOnClickListener {
            changeUI(PicPickerUI::class.java, REQ_CHAT)
        }
        adapter.listener = object : ChatAdapter2.IContentClickListener {
            override fun onTextContentClick(text: String) {
            }

            @SuppressLint("RestrictedApi")
            override fun onImageContentClick(item: ImageChatItem, view: View) {
                val intent = Intent(this@ChatUI2, PicPreviewUI::class.java)
                if(item.downloadFlag == BaseChatItem.DOWNLOAD_NOT_DOWNLOAD) {
                    intent.putExtra(PicPreviewUI.KV_ALLOW_DOWNLOAD,true)
                    intent.putExtra(PicPreviewUI.KV_MESSAGE_ID,item.msgId)
                    intent.putExtra(PicPreviewUI.KV_MEDIA_ID,item.media_id)
                    intent.putExtra(PicPreviewUI.KV_MEDIA_CRC,item.media_crc32)
                    intent.putExtra(PicPreviewUI.KV_FILE_SIZE,item.fileSize)
                    intent.putExtra(PicPreviewUI.KV_USER_NAME,item.fromUser)
                }
                intent.putExtra(PicPreviewUI.KV_PREVIEW_PATH,item.imgLink)


                startActivityForResult(intent
                        , REQ_CHAT
                        , ActivityOptionsCompat.makeSceneTransitionAnimation(this@ChatUI2
                        , view
                        , "SHARE")
                        .toBundle())
            }

            override fun onVoiceContentClick(item: VoiceChatItem) {
                if(item.downloadFlag == BaseChatItem.DOWNLOAD_DOWNLOADED) {
                    if(stopPlayingVoice() == item.msgId)
                        return
                    playVoice(item)
                } else {
                    if(stopPlayingVoice() == item.msgId)
                        return
                    curPlayItem = item
                    adapter.updateVoicePlayStatus(item.msgId,true)
                    downloadVoice(item.fromUser,item.media_id,item.media_crc32,{
                        p0,p1,p2->
                        if(p0 == 0) {
                            //下载语音成功
                            adapter.updateVoicePath(item.msgId,p2!!.absolutePath)
                            if(curPlayItem != null && curPlayItem!!.msgId == item.msgId) {
                                playVoice(curPlayItem!!)
                            }
                        }
                        else {
                            adapter.updateVoicePlayStatus(item.msgId,false)
                            sendToast("下载语音失败，请检查网络后重试")
                        }
                    })
                }
            }

            override fun onUploadFailedClick(item: BaseChatItem) {
            }

        }


        chatList.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        chatList.setAdapter(adapter)
      }

    fun addMessage(msg : BaseChatItem) {
        val isNeedScroll = chatList.isListViewLastItemVisible
        adapter.addMsg(msg)
        if(isNeedScroll)
            chatList.recyclerView.scrollToPosition(adapter.itemCount - 1)
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
            if(player != null) {
                player!!.stop()
                player!!.release()
            }
            adapter.updateVoicePlayStatus(curPlayItem!!.msgId,false)
            curPlayItem = null
            return curId
        }

        return -1
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
            RES_PIC_SUCCESS -> {
                val path = data!!.getStringExtra(ChatUI.KV_PIC_PATH).replace("file:///","")
                addMessage(getIMModule().sendPicMsg(path,oppositeUserName))
            }
            RES_PIC_PATH_CHANGE -> {
                val path = data!!.getStringExtra(ChatUI.KV_PIC_PATH)
                val msg = data.getIntExtra(ChatUI.KV_MESSAGE_ID,-1)
                adapter.updateImagePath(msg,path)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stateRecord.unregisterObserver()
    }
}