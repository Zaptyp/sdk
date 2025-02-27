package io.github.wulkanowy.sdk.pojo

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class Conference(
    val title: String,
    val subject: String,
    val agenda: String,
    val presentOnConference: String,
    val online: Any?,
    val id: Int,
    @Deprecated("use dateZoned instead")
    val date: LocalDateTime,
    val dateZoned: ZonedDateTime,
)
