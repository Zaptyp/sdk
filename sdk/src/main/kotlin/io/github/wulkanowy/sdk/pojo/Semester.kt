package io.github.wulkanowy.sdk.pojo

import java.time.LocalDate

data class Semester(
    val diaryId: Int,
    val kindergartenDiaryId: Int,
    val diaryName: String,
    val schoolYear: Int,
    val semesterId: Int,
    val semesterNumber: Int,
    val start: LocalDate,
    val end: LocalDate,
    val classId: Int,
    val unitId: Int
)
