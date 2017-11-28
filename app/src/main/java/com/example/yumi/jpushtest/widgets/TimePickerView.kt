package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import com.example.yumi.jpushtest.R
import java.util.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/28.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class TimePickerView(context: Context, attrs: AttributeSet) : ListView(context, attrs) {
    val adapter = object : BaseAdapter() {
        val MAX_MONTH_COUNT = 12 * 1
        var curDate = Calendar.getInstance()
        var startMonth : Int
        var startDay : Int
        var startWeek : Int
        init {
            curDate.timeInMillis = System.currentTimeMillis()
            startMonth = curDate.get(Calendar.MONTH) + 1
            startDay = curDate.get(Calendar.DAY_OF_MONTH)
            startWeek = curDate.get(Calendar.DAY_OF_WEEK)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view : View
            if(convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_time_item,parent,false)
            } else view = convertView



            return view
        }

        override fun getItem(position: Int): Any = 0

        override fun getItemId(position: Int): Long = 0

        override fun getCount(): Int = MAX_MONTH_COUNT

    }

    init {

    }
}