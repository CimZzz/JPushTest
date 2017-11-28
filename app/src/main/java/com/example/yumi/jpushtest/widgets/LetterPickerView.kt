package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.px2sp

/**
 * Created by CimZzz(王彦雄) on 2017/11/28.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class LetterPickerView(context: Context, attrs: AttributeSet) : ListView(context, attrs) {
    val letterArr : ArrayList<Char> = ArrayList()
    private val letterColor : Int
    private val letterSize : Float

    private val clickListener = View.OnClickListener {
        listener?.invoke(it.tag as Char)
    }

    var listener : ((Char)->Unit)? = null

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.LetterPickerView)
        letterColor = arr.getColor(R.styleable.LetterPickerView_letterColor, Color.BLACK)
        letterSize = px2sp(context,arr.getDimension(R.styleable.LetterPickerView_letterSize,0f)).toFloat()

        arr.recycle()

        letterArr.add('A')
        letterArr.add('B')
        letterArr.add('C')
        letterArr.add('D')
        letterArr.add('E')
        letterArr.add('F')
        letterArr.add('G')
        letterArr.add('H')
        letterArr.add('I')
        letterArr.add('J')
        letterArr.add('K')
        letterArr.add('L')
        letterArr.add('M')
        letterArr.add('N')
        letterArr.add('O')
        letterArr.add('P')
        letterArr.add('Q')
        letterArr.add('R')
        letterArr.add('S')
        letterArr.add('T')
        letterArr.add('U')
        letterArr.add('V')
        letterArr.add('W')
        letterArr.add('X')
        letterArr.add('Y')
        letterArr.add('Z')
        letterArr.add('#')

        adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var view = convertView
                val textView : TextView
                if(view == null) {
                    view = TextView(context)
                    textView = view
                    textView.setOnClickListener(clickListener)
                    textView.setTextColor(letterColor)
                    textView.textSize = letterSize
                }
                else textView = view as TextView

                textView.tag = letterArr[position]
                textView.text = "${letterArr[position]}"

                return textView
            }

            override fun getItem(position: Int): Any = letterArr[position]

            override fun getItemId(position: Int): Long = 0

            override fun getCount(): Int = letterArr.size

        }
    }
}