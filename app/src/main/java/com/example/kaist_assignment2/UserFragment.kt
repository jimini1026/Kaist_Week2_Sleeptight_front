package com.example.kaist_assignment2

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.AlarmManagerCompat
import androidx.fragment.app.Fragment
import com.example.kaist_assignment2.MusicFragment.Companion
import java.text.SimpleDateFormat
import java.util.*

class UserFragment : Fragment() {

    private lateinit var timeTextView: TextView
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var textView5: TextView
    private lateinit var textView6: TextView
    private lateinit var confirmButton: Button

    private var selectedTextView: TextView? = null

    companion object {
        private const val ARG_USER_NAME = "user_name"
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userName: String?, userId: String?): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            args.putString(ARG_USER_NAME, userName)
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        timeTextView = view.findViewById(R.id.timeTextView)
        val setTimeButton: Button = view.findViewById(R.id.setTimeButton)
        textView1 = view.findViewById(R.id.textView1)
        textView2 = view.findViewById(R.id.textView2)
        textView3 = view.findViewById(R.id.textView3)
        textView4 = view.findViewById(R.id.textView4)
        textView5 = view.findViewById(R.id.textView5)
        textView6 = view.findViewById(R.id.textView6)
        confirmButton = view.findViewById(R.id.confirmButton)

        // 현재 시간으로 TextView들 초기화
        val calendar = Calendar.getInstance()
        updateTimeViews(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

        setTimeButton.setOnClickListener {
            showTimePickerDialog()
        }

        setTextViewClickListener(textView1)
        setTextViewClickListener(textView2)
        setTextViewClickListener(textView3)
        setTextViewClickListener(textView4)
        setTextViewClickListener(textView5)
        setTextViewClickListener(textView6)

        confirmButton.setOnClickListener {
            confirmSelectedTimes()
        }

        return view
    }

    private fun setTextViewClickListener(textView: TextView) {
        textView.setOnClickListener {
            toggleTextViewSelection(textView)
        }
    }

    private fun toggleTextViewSelection(textView: TextView) {
        if (selectedTextView == textView) {
            // 선택된 TextView를 다시 클릭하면 선택 해제
            selectedTextView?.setBackgroundResource(android.R.color.transparent)
            selectedTextView = null
        } else {
            // 이전에 선택된 TextView의 선택 해제
            selectedTextView?.setBackgroundResource(android.R.color.transparent)

            // 현재 TextView를 선택 상태로 변경
            selectedTextView = textView
            textView.setBackgroundResource(R.color.whiteblue)
        }
    }

    private fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                updateTimeViews(hourOfDay, minute)
                val timeText = String.format("%02d:%02d", hourOfDay, minute)
                timeTextView.text = timeText
            },
            hour, // 초기 시간 설정 (현재 시간)
            minute,  // 초기 분 설정 (현재 분)
            true // 24시간 형식 사용 여부
        )

        timePickerDialog.show()
    }

    private fun updateTimeViews(hour: Int, minute: Int) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // 현재 시간 설정
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        // timeTextView 초기화
        timeTextView.text = sdf.format(calendar.time)

        // TextView 1부터 6까지 1시간 30분씩 증가하여 설정
        for (i in 1..6) {
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            calendar.add(Calendar.MINUTE, 30)
            when (i) {
                1 -> textView1.text = sdf.format(calendar.time)
                2 -> textView2.text = sdf.format(calendar.time)
                3 -> textView3.text = sdf.format(calendar.time)
                4 -> textView4.text = sdf.format(calendar.time)
                5 -> textView5.text = sdf.format(calendar.time)
                6 -> textView6.text = sdf.format(calendar.time)
            }
        }
    }

    private fun confirmSelectedTimes() {
        if (selectedTextView == null) {
            Toast.makeText(requireContext(), "Please select a time", Toast.LENGTH_SHORT).show()
        } else {
            val sleepTime = timeTextView.text.toString()
            val selectedTime = selectedTextView!!.text.toString()

            // 자는 시간 파싱 후 가져오기

            // 선택된 시간 파싱
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedDate = sdf.parse(selectedTime)
            val calendar = Calendar.getInstance()

            // 현재 시간 가져오기
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentTime.get(Calendar.MINUTE)

            // 선택된 시간으로 Calendar 설정
            calendar.time = selectedDate
            val selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
            val selectedMinute = calendar.get(Calendar.MINUTE)

            // 시간 차이 계산
            var alarmHour = selectedHour - currentHour
            var alarmMinute = selectedMinute - currentMinute

            // 음수 처리
            if (alarmMinute < 0) {
                alarmHour -= 1
                alarmMinute += 60
            }

            // AlarmManager 설정
            setAlarm(requireContext(), sleepTime, alarmHour, alarmMinute, selectedHour, selectedMinute)

            // 사용자에게 선택된 시간 알림
            Toast.makeText(requireContext(), "SleepTime : $sleepTime Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAlarm(context: Context, sleepTime: String, hours: Int, minutes: Int, selectedHour: Int, selectedMinute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("userId", arguments?.getString(UserFragment.ARG_USER_ID))
            putExtra("currentDate", currentDate)
            putExtra("sleepTime", sleepTime)
            putExtra("selectedHour", selectedHour)
            putExtra("selectedMinute", selectedMinute)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // 알람 시간 계산
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        calendar.add(Calendar.MINUTE, minutes)

        // 알람 설정
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
