package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.yumi.jpushtest.R
import java.util.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/28.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class TimePickerView(context: Context, attrs: AttributeSet) : ListView(context, attrs) {
    val clickListener = View.OnClickListener {
//        logV(SimpleDateFormat("yyyy-MM-dd").format(it.tag as Long))
        listener?.invoke(it.tag as Long)
    }
    var listener : ((Long)->Unit)? = null

    val calendar = Calendar.getInstance()
    val adapter = object : BaseAdapter() {
        val monthList = ArrayList<MonthItem>()
        init {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR,0)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.SECOND,0)
            var month = calendar.get(Calendar.MONTH) + 1
            var week = calendar.get(Calendar.DAY_OF_WEEK)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var year = calendar.get(Calendar.YEAR)

            var totalMonth = 15

            do {
                if(day != 1) {
                    calendar.set(Calendar.DAY_OF_MONTH,1)
                }

                monthList.add(MonthItem(year,month,week,day,getMonthDay(month,year) - day + 1))

                calendar.add(Calendar.MONTH,1)
                month = calendar.get(Calendar.MONTH) + 1
                week = calendar.get(Calendar.DAY_OF_WEEK)
                day = calendar.get(Calendar.DAY_OF_MONTH)
                year = calendar.get(Calendar.YEAR)

                totalMonth--
            } while (totalMonth > 0)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view : ViewGroup
            if(convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_time_item,parent,false) as ViewGroup
                var curLine = 0
                var totalLine = 6
                while (totalLine > 0) {
                    val container : ViewGroup = when (curLine) {
                        0 -> view.findViewById(R.id.timeItemLine1)
                        1 -> view.findViewById(R.id.timeItemLine2)
                        2 -> view.findViewById(R.id.timeItemLine3)
                        3 -> view.findViewById(R.id.timeItemLine4)
                        4 -> view.findViewById(R.id.timeItemLine5)
                        else -> view.findViewById(R.id.timeItemLine6)
                    }

                    (0 until container.childCount).map { container.getChildAt(it) }.forEach {
                        it.setOnClickListener(clickListener)
                    }
                    curLine ++
                    totalLine --
                }
            }
            else view = convertView as ViewGroup
            val item = monthList[position]

            val topView = view.findViewById<TextView>(R.id.timeItemTop)
            topView.text = "${item.year}年${item.month}月"

            var marginLength = item.startWeek - 1
            var length = item.length
            var curLine = 0
            var totalLine = 6
            var day = item.startDay
            var container : ViewGroup
            while (totalLine > 0) {
                container = when(curLine) {
                    0-> view.findViewById(R.id.timeItemLine1)
                    1-> view.findViewById(R.id.timeItemLine2)
                    2-> view.findViewById(R.id.timeItemLine3)
                    3-> view.findViewById(R.id.timeItemLine4)
                    4-> view.findViewById(R.id.timeItemLine5)
                    else-> view.findViewById(R.id.timeItemLine6)
                }
                if(length != 0) {
                    container.visibility = View.VISIBLE
                    (0 until container.childCount).map { container.getChildAt(it) as TextView }.forEach {
                        if (marginLength > 0) {
                            it.visibility = View.INVISIBLE
                            it.text = ""
                            marginLength--
                        } else if (length == 0) {
                            it.visibility = View.INVISIBLE
                            it.text = ""
                        } else {
                            it.visibility = View.VISIBLE
                            it.text = "$day"
                            calendar.clear()
                            calendar.set(Calendar.DAY_OF_MONTH,day)
                            calendar.set(Calendar.MONTH,item.month - 1)
                            calendar.set(Calendar.YEAR,item.year)
                            it.tag = calendar.timeInMillis
                            day++
                            length--
                        }
                    }
                } else {
                    container.visibility = View.GONE
                }

                curLine ++
                totalLine --
            }


            return view
        }

        override fun getItem(position: Int): Any = 0

        override fun getItemId(position: Int): Long = 0

        override fun getCount(): Int = monthList.size

        private fun getMonthDay(month : Int,year : Int) : Int
        =when(month) {
            1->31
            2->if (isLeapYear(year)) 29 else 28
            3->31
            4->30
            5->31
            6->30
            7->31
            8->31
            9->30
            10->31
            11->30
            12->31
            else->-1
        }
        private fun isLeapYear(year : Int) : Boolean = (year%4==0&&year%100!=0)||(year%400==0)

        inner class MonthItem (
                val year : Int,
                val month : Int,
                val startWeek : Int,
                val startDay : Int,
                val length : Int
        )
    }

    init {
        setAdapter(adapter)
        overScrollMode = View.OVER_SCROLL_NEVER
    }
}