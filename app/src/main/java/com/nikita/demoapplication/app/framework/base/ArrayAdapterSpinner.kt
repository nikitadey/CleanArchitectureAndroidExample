package com.nikita.demoapplication.app.framework.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.nikita.demoapplication.R

class ArrayAdapterSpinner<T> : ArrayAdapter<T> {

    private val textColor: Int
    private val objects: Array<T>? = null
    private var objectList: List<T>
    private var backgroundColor = -1
    private var disabledColor = -1
    private var isDisabled = false
    private var isHintEnabled = false

    /**
     *
     * @param context
     * @param objects- list of objects to display
     * @param textColor- name of color
     */
    constructor(context: Context, objects: List<T>, @ColorRes textColor: Int) : super(context, R.layout.simple_spinner_item, objects) {
        this.textColor = textColor
        objectList = objects
    }

    constructor(context: Context, objects: List<T>, @ColorRes textColor: Int, @ColorRes layoutBgColor: Int) : super(context, R.layout.simple_spinner_item, objects) {
        this.textColor = textColor
        objectList = objects
        backgroundColor = layoutBgColor
    }

    constructor(context: Context, objects: List<T>, textColor: Int, @ColorRes dropDownBgColor: Int, @ColorRes disabledColor: Int) : super(context, R.layout.simple_spinner_item, objects) {
        this.textColor = textColor
        objectList = objects
        backgroundColor = dropDownBgColor
        this.disabledColor = disabledColor
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.simple_spinner_item, parent, false) as TextView
        textView.text = objectList[position].toString()
        //        if(position==0){
//            textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.textLabelColor));
//
//        }
//        else{
        textView.setTextColor(ContextCompat.getColor(parent.context, textColor))

//        }
        if (disabledColor != -1 && isDisabled) {
            textView.setTextColor(ContextCompat.getColor(parent.context, disabledColor))
        }
        return textView
    }

    fun setDisabled(disabled: Boolean) {
        isDisabled = disabled
    }

    fun setHintEnabled(enabled:Boolean){
        isHintEnabled=enabled

    }

    fun setObjects(objects: List<T>) {
        objectList = objects
        super.clear()
        super.addAll(objects)
        notifyDataSetChanged()
    }

    fun getItems():List<T>{
        return objectList
    }

    override fun getItem(position: Int): T {
        return objectList[position]
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view as TextView

        if(position==0 && isHintEnabled){
//         Set the hint text color gray
            textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.textLabelColor));

        }
        else {
            textView.setTextColor(ContextCompat.getColor(parent.getContext(), textColor));
        }
        if (backgroundColor != -1) {
            textView.setBackgroundColor(ContextCompat.getColor(parent.context, backgroundColor))
        }
        //        }
        return view
    }
}