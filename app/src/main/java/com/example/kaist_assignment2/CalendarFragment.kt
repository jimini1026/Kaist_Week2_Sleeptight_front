package com.example.kaist_assignment2

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.Calendar

class CalendarFragment : Fragment() {

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarView: MaterialCalendarView = view.findViewById(R.id.calendarView)

        // 데코레이터 추가
        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val todayDecorator = ToDayDecorator(requireContext())

        // 이벤트 날짜 리스트
        val calendarList = mutableListOf<CalendarDay>()
        calendarList.add(CalendarDay.today())
        calendarList.add(CalendarDay.from(2022, 10, 25))
        val eventDecorator = EventDecorator(calendarList, requireContext(), Color.BLUE)

        calendarView.addDecorators(sundayDecorator, saturdayDecorator, todayDecorator, eventDecorator)

        return view
    }

    inner class ToDayDecorator(context: Context) : DayViewDecorator {
        private var date = CalendarDay.today()
        private val drawable = context.resources.getDrawable(R.drawable.data_select_deco, null)

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date) == true
        }

        override fun decorate(view: DayViewFacade?) {
            drawable?.let {
                view?.setBackgroundDrawable(it)
            }
        }
    }

    inner class SundayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    inner class SaturdayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.BLUE))
        }
    }

    inner class EventDecorator(private val dates: Collection<CalendarDay>, context: Context, private val color: Int) : DayViewDecorator {
        private val datesSet = HashSet(dates)

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return datesSet.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(5F, color))
        }
    }
}