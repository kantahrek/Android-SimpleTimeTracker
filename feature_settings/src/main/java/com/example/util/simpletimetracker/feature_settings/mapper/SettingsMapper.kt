package com.example.util.simpletimetracker.feature_settings.mapper

import com.example.util.simpletimetracker.core.mapper.TimeMapper
import com.example.util.simpletimetracker.core.repo.ResourceRepo
import com.example.util.simpletimetracker.core.view.spinner.CustomSpinner
import com.example.util.simpletimetracker.domain.extension.orZero
import com.example.util.simpletimetracker.domain.model.CardOrder
import com.example.util.simpletimetracker.domain.model.DayOfWeek
import com.example.util.simpletimetracker.feature_settings.R
import com.example.util.simpletimetracker.feature_settings.viewData.CardOrderViewData
import com.example.util.simpletimetracker.feature_settings.viewData.FirstDayOfWeekViewData
import java.util.Calendar
import javax.inject.Inject

class SettingsMapper @Inject constructor(
    private val resourceRepo: ResourceRepo,
    private val timeMapper: TimeMapper
) {

    private val cardOrderList: List<CardOrder> = listOf(
        CardOrder.NAME,
        CardOrder.COLOR,
        CardOrder.MANUAL
    )

    private val dayOfWeekList: List<DayOfWeek> = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    fun toCardOrderViewData(currentOrder: CardOrder): CardOrderViewData {
        return CardOrderViewData(
            items = cardOrderList
                .map(::toCardOrderName)
                .map(CustomSpinner::CustomSpinnerTextItem),
            selectedPosition = toPosition(currentOrder),
            isManualConfigButtonVisible = currentOrder == CardOrder.MANUAL
        )
    }

    fun toCardOrder(position: Int): CardOrder {
        return cardOrderList.getOrElse(position) { cardOrderList.first() }
    }

    fun toFirstDayOfWeekViewData(currentOrder: DayOfWeek): FirstDayOfWeekViewData {
        return FirstDayOfWeekViewData(
            items = dayOfWeekList
                .map(timeMapper::toShortDayOfWeekName)
                .map(CustomSpinner::CustomSpinnerTextItem),
            selectedPosition = toPosition(currentOrder)
        )
    }

    fun toDayOfWeek(position: Int): DayOfWeek {
        return dayOfWeekList.getOrElse(position) { dayOfWeekList.first() }
    }

    fun toInactivityReminderText(duration: Long): String {
        return if (duration > 0) {
            timeMapper.formatDuration(duration)
        } else {
            resourceRepo.getString(R.string.settings_inactivity_reminder_disabled)
        }
    }

    fun toUseMilitaryTimeHint(useMilitaryTime: Boolean): String {
        val hintTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return timeMapper.formatTime(hintTime, useMilitaryTime)
    }

    private fun toPosition(cardOrder: CardOrder): Int {
        return cardOrderList.indexOf(cardOrder).takeUnless { it == -1 }.orZero()
    }

    private fun toCardOrderName(cardOrder: CardOrder): String {
        return when (cardOrder) {
            CardOrder.NAME -> R.string.settings_sort_by_name
            CardOrder.COLOR -> R.string.settings_sort_by_color
            CardOrder.MANUAL -> R.string.settings_sort_manually
        }.let(resourceRepo::getString)
    }

    private fun toPosition(dayOfWeek: DayOfWeek): Int {
        return dayOfWeekList.indexOf(dayOfWeek).takeUnless { it == -1 }.orZero()
    }
}