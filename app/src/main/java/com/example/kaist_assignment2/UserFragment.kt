package com.example.kaist_assignment2


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class UserFragment : Fragment() {

    companion object {
        private const val ARG_USER_NAME = "user_name"

        fun newInstance(userName: String?): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            args.putString(ARG_USER_NAME, userName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val userName = arguments?.getString(ARG_USER_NAME)

        val userNameTextView: TextView = view.findViewById(R.id.user_name_text)
        userNameTextView.text = userName ?: "No User Name"

        return view
    }
}
