package be.vives.vivesplus.screens.uurrooster

import com.alamkanak.weekview.WeekViewEntity
import com.alamkanak.weekview.jsr310.WeekViewPagingAdapterJsr310
import com.alamkanak.weekview.jsr310.setEndTime
import com.alamkanak.weekview.jsr310.setStartTime
import java.time.LocalDate

class MyPagingEventAdapter (private val clickOnEventListener : ClickOnEventListener, private val loadMoreHandler : LoadMoreHandler, private val rangeChangedListener : RangeChangedListener) : WeekViewPagingAdapterJsr310<Event>() {

    override fun onCreateEntity(item: Event): WeekViewEntity {
        val style : WeekViewEntity.Style = WeekViewEntity.Style.Builder()
            .setBackgroundColor(item.color)
            .build()

        return WeekViewEntity.Event.Builder(item)
            .setId(item.id)
            .setTitle(item.title)
            .setStartTime(item.startTime)
            .setEndTime(item.endTime)
            .setSubtitle(item.location)
            .setAllDay(item.isAllDay)
            .setStyle(style)
            .build()
    }

    override fun onLoadMore(startDate: LocalDate, endDate: LocalDate) {
        loadMoreHandler.onLoadMore(startDate, endDate)
    }

    override fun onEventClick(data: Event) {
        clickOnEventListener.onClick(data)
    }

    override fun onRangeChanged(firstVisibleDate: LocalDate, lastVisibleDate: LocalDate) {
        rangeChangedListener.onRangeChanged(firstVisibleDate, lastVisibleDate)
    }
}

class ClickOnEventListener(val clickOnEventListener: (event : Event) -> Unit){
    fun onClick(event : Event) = clickOnEventListener(event)
}

class LoadMoreHandler(val loadMoreHandler: (startDate : LocalDate, endDate : LocalDate) -> Unit){
    fun onLoadMore(startDate: LocalDate, endDate: LocalDate) = loadMoreHandler(startDate, endDate)
}

class RangeChangedListener(val rangeChangedListener : (firstVisibleDate: LocalDate, lastVisibleDate: LocalDate) -> Unit){
    fun onRangeChanged(firstVisibleDate: LocalDate, lastVisibleDate: LocalDate) = rangeChangedListener(firstVisibleDate, lastVisibleDate)
}
