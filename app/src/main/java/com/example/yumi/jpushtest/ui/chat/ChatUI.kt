package com.example.yumi.jpushtest.ui.chat

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
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
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.entity.BaseChatItem
import com.example.yumi.jpushtest.environment.REQ_CHAT
import com.example.yumi.jpushtest.environment.RES_PIC_SUCCESS
import com.example.yumi.jpushtest.ui.logintest.LoginTestUI
import com.example.yumi.jpushtest.ui.picpicker.PicPickerUI
import com.example.yumi.jpushtest.ui.picpreview.PicPreviewUI
import com.example.yumi.jpushtest.ui.picpreview.PicPreviewUI.Companion.KV_PREVIEW_PATH
import com.example.yumi.jpushtest.utils.isEmptyString
import com.virtualightning.gridpagerview.GridPagerAdapter
import com.virtualightning.gridpagerview.ViewPool
import com.virtualightning.gridpagerview.ViewWrapper
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.actionbar_chat.*
import kotlinx.android.synthetic.main.ui_chat.*


class ChatUI : BaseUI<ChatPresenter>() , IChatContract.View{

    var oppoUserName : String = ""
    var userName : String = ""
    var adapter : ChatAdapter = ChatAdapter()

    /*Extend interface function*/
    override fun getOppositeUserName(): String = oppoUserName
    override fun getMyUserName(): String = userName

    override fun getTextMsg(): String {
        val text = chatInput.editableText.toString()
        chatInput.text.clear()
        return text
    }

    override fun addMsg(msg: BaseChatItem) {
        adapter.addMsg(msg)
    }

    override fun sendMessageResult(msgId: Int, isSuccess: Boolean) {
        sendToast("发送$isSuccess")
    }

    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_chat)
        creater.setActionBarID(R.layout.actionbar_chat)
        presenter = ChatPresenter(this,ChatMethod())
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        userName = intent.extras.getString("UserName")
        chatTitle.text = userName
        oppoUserName = intent.extras.getString("OppositeUserName")

        chatHAHA.setOnClickListener {

            startActivity(Intent(this,PicPreviewUI::class.java)
                    , ActivityOptions.makeSceneTransitionAnimation(this,chatHAHA,"SHARE").toBundle())
        }


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
            }
        }

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
            presenter!!.sendTextMsg()
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
            override fun onTextContentClick(text: String) {
            }

            override fun onImageContentClick(imgPath: String, view: View) {
                val intent = Intent(this@ChatUI,PicPreviewUI::class.java)
                intent.putExtra(KV_PREVIEW_PATH,imgPath)
                startActivity(intent
                        , ActivityOptionsCompat.makeSceneTransitionAnimation(this@ChatUI
                        , view
                        , "SHARE")
                        .toBundle())

            }

            override fun onVoiceContentClick(voicePath: String) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode != REQ_CHAT)
            return super.onActivityResult(requestCode, resultCode, data)

        when(resultCode) {
            RES_PIC_SUCCESS-> {
                //send pic msg
                val path = data!!.getStringExtra(PicPickerUI.KV_PIC_PATH).replace("file:///","")
                chatInput.append(path)
                presenter!!.sendPicMsg(path)
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