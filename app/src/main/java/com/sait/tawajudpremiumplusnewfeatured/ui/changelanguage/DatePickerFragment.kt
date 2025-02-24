package com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    companion object {
        private const val ARG_YEAR = "arg_year"
        private const val ARG_MONTH = "arg_month"
        private const val ARG_DAY = "arg_day"
        private const val ARG_TEXT_VIEW_ID  ="date_txt"
        private const val ARG_FRAGMENT_NAME = "arg_fragment_name" // Add this argument

        fun newInstance(year: Int, month: Int, day: Int, textViewId :Int, fragmentName: String): DatePickerFragment {
            val fragment = DatePickerFragment()
            val args = Bundle()
            args.putInt(ARG_YEAR, year)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_TEXT_VIEW_ID ,textViewId)
            args.putString(ARG_FRAGMENT_NAME,fragmentName)
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var textView: TextView
    private lateinit var fragmentName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            fragmentName= it.getString(ARG_FRAGMENT_NAME,"")
            textView = requireActivity().findViewById(it.getInt(ARG_TEXT_VIEW_ID))
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
/*        val now = Calendar.getInstance()
        val mDay = now[Calendar.DAY_OF_MONTH]
        val mMonth = now[Calendar.MONTH]
        val mYear = now[Calendar.YEAR]*/
        val c = Calendar.getInstance()
        val mYear = arguments?.getInt(ARG_YEAR) ?: c[Calendar.YEAR]
        val mMonth = arguments?.getInt(ARG_MONTH) ?: c[Calendar.MONTH]
        val mDay = arguments?.getInt(ARG_DAY) ?: c[Calendar.DAY_OF_MONTH]

        val dialog = DatePickerDialog(
            requireContext(),
            this,
            mYear,
            mMonth -1,
            mDay
        )
        dialog.datePicker.layoutDirection = DatePicker.LAYOUT_DIRECTION_LTR
        dialog.context.resources.configuration.locale=Locale.ENGLISH
        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
      /*  if ( fragmentName == "ViolationPendingFragment" && textView.id == arguments?.getInt(
                ARG_TEXT_VIEW_ID
            )
        ) {
            textView.text = ""
        }*/
            var monthOfYear = month + 1
            try {
                val yearNew = year
                monthOfYear = monthOfYear
                var monthnew = ""
                var daynew = ""
                monthnew = if (monthOfYear < 10) {
                    "0$monthOfYear"
                } else {
                    "" + monthOfYear
                }
                daynew = if (day < 10) {
                    "0$day"
                } else {
                    "" + day
                }

                textView.text = "$yearNew-$monthnew-$daynew"

            } catch (e: java.lang.Exception) {
            }

    }
}
