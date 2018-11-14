package com.thomas.garrison.traveladvisories

import android.app.DatePickerDialog
import android.content.Context
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun shakeError(): TranslateAnimation {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 400
        shake.interpolator = CycleInterpolator(4f)
        return shake
    }

    fun pickDate(context: Context, btn: Button) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
            btn.text = sdf.format(cal.time)
        }
        val dpd = DatePickerDialog(context,
                dateSetListener,
                // default to today's date
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
        dpd.datePicker.minDate = (System.currentTimeMillis() - 1000)
        dpd.show()
    }
}
