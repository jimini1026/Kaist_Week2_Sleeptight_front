// DateInfoDialogFragment.kt
package com.example.kaist_assignment2

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DateInfoDialogFragment : DialogFragment() {

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
        val infoTextView = view.findViewById<TextView>(R.id.infoTextView)

        // Retrieve date and info from arguments
        val date = arguments?.getString("date")
        val info = arguments?.getString("info")

        dateTextView.text = date
        infoTextView.text = info
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
