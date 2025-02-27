package io.github.wulkanowy.sdk.scrapper.timetable

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class TimetableResponse(

    @Json(name = "Headers")
    val headers: List<TimetableHeader> = emptyList(),

    @Json(name = "Rows")
    val rows: List<List<String>> = emptyList(),

    @Json(name = "Additionals")
    val additional: List<TimetableAdditionalDay>
)

@JsonClass(generateAdapter = true)
data class TimetableHeader(

    @Json(name = "Text")
    val date: String
)

@JsonClass(generateAdapter = true)
data class TimetableAdditionalDay(

    @Json(name = "Header")
    val header: String,

    @Json(name = "Descriptions")
    val descriptions: List<TimetableAdditionalLesson>
)

@JsonClass(generateAdapter = true)
data class TimetableAdditionalLesson(

    @Json(name = "Description")
    val description: String
)

data class TimetableCell(
    val number: Int = 0,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val date: LocalDate,
    val td: Element
)
