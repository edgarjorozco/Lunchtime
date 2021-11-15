package com.edgarjorozco.lunchtime.models

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class OpenHoursPeriod (
    val open: OpenHoursDetail,

    val close: OpenHoursDetail
)

/**
 * @param day A number from 0–6, corresponding to the days of the week, starting on Sunday. For example, 2 means Tuesday.
 * @param time May contain a time of day in 24-hour hhmm format. Values are in the range 0000–2359. The time will be reported in the place’s time zone.
 */
@JsonClass(generateAdapter = true)
data class OpenHoursDetail (
    val day: Int,

    val time: String
)