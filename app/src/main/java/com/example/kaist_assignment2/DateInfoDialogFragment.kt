package com.example.kaist_assignment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DateInfoDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomRoundedDialogTheme)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_info_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val sleptAtTimeTextView = view.findViewById<TextView>(R.id.sleptAtTimeTextView)
        val setAlarmAtTimeTextView = view.findViewById<TextView>(R.id.setAlarmAtTimeTextView)
        val wokeUpAtTimeTextView = view.findViewById<TextView>(R.id.wokeUpAtTimeTextView)
        val confirmTextView = view.findViewById<TextView>(R.id.confirmTextView)

        // Retrieve date and info from arguments
        val date = arguments?.getString("date")
        val info = arguments?.getString("info")

        dateTextView.text = date

        // Parse info string and set the times
        info?.let {
            val infoParts = it.split("\n")
            sleptAtTimeTextView.text = infoParts.getOrNull(0)?.substringAfter("Slept At ")
            setAlarmAtTimeTextView.text = infoParts.getOrNull(1)?.substringAfter("Set Alarm At ")
            wokeUpAtTimeTextView.text = infoParts.getOrNull(2)?.substringAfter("Woke Up At ")
        }

        // Set click listener to dismiss the dialog
        confirmTextView.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(date: String, info: String): DateInfoDialogFragment {
            val fragment = DateInfoDialogFragment()
            val args = Bundle()
            args.putString("date", date)
            args.putString("info", info)
            fragment.arguments = args
            return fragment
        }
    }
}