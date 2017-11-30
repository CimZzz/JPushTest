package com.example.yumi.jpushtest.ui.country

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.RecyclerDecoration
import com.example.yumi.jpushtest.utils.isEmptyString
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.actionbar_title_back.*
import kotlinx.android.synthetic.main.ui_country.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/28.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class CountryUI : BaseUI<IPresenter<*,*>>() {
    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_country)
        creater.setActionBarID(R.layout.actionbar_title_back)
        openAutoCancelSoft = true
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        actionbarTitle.text = "国家选择"
        actionbarBack.setOnClickListener {
            finish()
        }
        countryList.layoutManager = LinearLayoutManager(this,OrientationHelper.VERTICAL,false)
        countryList.addItemDecoration(RecyclerDecoration(this,R.color.country_DividerColor))
        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            val TYPE_CAP = 0
            val TYPE_ITEM = 1
            val dataList = ArrayList<Cap>()
            var showList = dataList

            init {
                dataList.add(Cap('A',
                        Item("America",-1),
                        Item("Australia",-1)) )



                dataList.add(Cap('B',
                        Item("Big Country",-1),
                        Item("Big Big Country",-1)))
                dataList.add(Cap('C'))
                dataList.add(Cap('D'))
                dataList.add(Cap('E'))
                dataList.add(Cap('F'))
                dataList.add(Cap('G'))
                dataList.add(Cap('H'))
                dataList.add(Cap('I'))
                dataList.add(Cap('J'))
                dataList.add(Cap('K'))
                dataList.add(Cap('L'))
                dataList.add(Cap('M'))
                dataList.add(Cap('N'))
                dataList.add(Cap('O'))
                dataList.add(Cap('P'))
                dataList.add(Cap('Q'))
                dataList.add(Cap('R'))
                dataList.add(Cap('S'))
                dataList.add(Cap('T'))
                dataList.add(Cap('U'))
                dataList.add(Cap('V'))
                dataList.add(Cap('W'))
                dataList.add(Cap('X'))
                dataList.add(Cap('Y'))
                dataList.add(Cap('Z'))
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = getItemAt(position)
                if(item is Cap) {
                    val concretedHolder = holder as CapHolder
                    concretedHolder.capText.text = item.cap.toString()
                }
                else if(item is Item){
                    val concretedHolder = holder as ItemHolder
                    concretedHolder.itemName.text = item.countryName
                    if(item.countryPic != -1)
                        concretedHolder.itemImg.setImageResource(item.countryPic)
                }
            }


            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
                = if(viewType == TYPE_CAP)
                    CapHolder(LayoutInflater.from(this@CountryUI).inflate(R.layout.item_country_cap,parent,false))
                else ItemHolder(LayoutInflater.from(this@CountryUI).inflate(R.layout.item_country_item,parent,false))


            override fun getItemCount(): Int {
                var size = showList.size
                showList.forEach{
                    size += it.itemCount
                }

                return size
            }

            override fun getItemViewType(position: Int): Int
                = if(getItemAt(position) is Cap)
                    TYPE_CAP
                else TYPE_ITEM

            fun getCharPosition(c : Char) : Int{
                var position = -1
                var isFind = false

                for(i in 0 until showList.size) {
                    position ++

                    if(showList[i].cap == c) {
                        isFind = true
                        break
                    }

                    position += showList[i].itemCount
                }
                return if(isFind) position else -1
            }

            fun filterList(filterStr : String) {
                if(isEmptyString(filterStr)) {
                    showList = dataList
                    notifyDataSetChanged()
                    return
                }

                showList = ArrayList()
                dataList.forEach {
                    cap->
                    var tempCap : Cap? = null
                    cap.arr.forEach {
                        if(it.countryName.toUpperCase().contains(filterStr.toUpperCase())) {
                            if(tempCap == null)
                                tempCap = Cap(cap.cap)
                            tempCap!!.addItem(it)
                        }
                    }

                    if(tempCap != null) {
                        showList.add(tempCap!!)
                        tempCap = null
                    }
                }

                notifyDataSetChanged()
            }

            private fun getItemAt(position : Int) : Any {
                var tmpPosition = position
                showList.forEach {
                    tmpPosition -= 1
                    if(tmpPosition < 0)
                        return it

                    tmpPosition -= it.itemCount
                    if(tmpPosition < 0)
                        return it.arr[tmpPosition + it.itemCount]
                }

                return Unit
            }

            inner class Cap(val cap : Char,vararg items : Item) {
                val arr = ArrayList<Item>()
                var itemCount : Int
                init {
                    arr.addAll(items)
                    itemCount = arr.size
                }

                fun addItem(item : Item) {
                    arr.add(item)
                    itemCount++
                }
            }


            inner class Item (
                val countryName : String,
                val countryPic : Int
            )


            inner class CapHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val capText : TextView = itemView.findViewById(R.id.countryItemCap)
            }

            inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val itemImg : ImageView = itemView.findViewById(R.id.countryItemImg)
                val itemName : TextView = itemView.findViewById(R.id.countryItemName)
            }
        }
        countryList.adapter = adapter
        countryLetterPicker.listener = {
            val position = adapter.getCharPosition(it)
            logV("Listener Doing : $position")
            countryList.scrollToPosition(position)
        }

        countryInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filterList(s.toString())
            }

        })
    }

}