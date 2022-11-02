package com.nikita.demoapplication.app.framework.base

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * Created by nikita
 */
class DatePickerFragment : DialogFragment() {
    var mDateSetListener: OnDateSetListener? = null
    private var year = 0
    private var month = 0
    private var day = 0
    private var minDateLimit: Long = -1
    private var maxDateLimit: Long = -1
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        val c = Calendar.getInstance()


        args?.let {
            //use the value in the bundle arguement to set current.
            year = if (it.containsKey(KEY_YEAR)) {
                it.getInt(KEY_YEAR, c[Calendar.YEAR])
            } else {
                c[Calendar.YEAR]
            }
            month = if (it.containsKey(KEY_MONTH)) {
                it.getInt(KEY_MONTH, c[Calendar.MONTH])
            } else {
                c[Calendar.MONTH]
            }
            day = if (it.containsKey(KEY_DAY)) {
                it.getInt(KEY_DAY, c[Calendar.YEAR])
            } else {
                c[Calendar.DAY_OF_MONTH]
            }
            if (it.containsKey(KEY_MIN_DATE_LIMIT)) {
                minDateLimit = it.getLong(KEY_MIN_DATE_LIMIT)
            }
            if (it.containsKey(KEY_MAX_DATE_LIMIT)) {
                maxDateLimit = it.getLong(KEY_MAX_DATE_LIMIT)
            }
        } ?: kotlin.run {
            // Use the current date as the default date in the picker
            year = c[Calendar.YEAR]
            month = c[Calendar.MONTH]
            day = c[Calendar.DAY_OF_MONTH]
        }
    }

    fun setCallback(dateSetListener: OnDateSetListener) {
        mDateSetListener = dateSetListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateDialog = DatePickerDialog(requireContext(), mDateSetListener, year, month, day)
        if (maxDateLimit != -1L) {
            dateDialog.datePicker.maxDate = maxDateLimit
        }
        if (minDateLimit != -1L) {
            dateDialog.datePicker.minDate = minDateLimit
        }


        // Create a new instance of DatePickerDialog and return it
        return dateDialog
    }

    fun hideFragment() {
        dismiss()
    }

    companion object {
        const val KEY_YEAR = "year"
        const val KEY_MONTH = "month"
        const val KEY_DAY = "day"
        const val KEY_MAX_DATE_LIMIT = "max_date"
        const val KEY_MIN_DATE_LIMIT = "min_date"
        fun getDate(year: Int, month: Int, day: Int): String {
            return year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
        }
    }


}