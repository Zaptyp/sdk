package io.github.wulkanowy.sdk.scrapper

import io.github.wulkanowy.sdk.scrapper.attendance.AttendanceCategory
import io.github.wulkanowy.sdk.scrapper.messages.Folder
import io.github.wulkanowy.sdk.scrapper.messages.Recipient
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.time.Month

@Ignore
class ScrapperRemoteTest : BaseTest() {

    private lateinit var sdk: Scrapper

    @Before
    fun setUp() {
        sdk = Scrapper.Builder().build {
            logLevel = HttpLoggingInterceptor.Level.BASIC
            loginType = Scrapper.LoginType.STANDARD
            ssl = true
            host = "fakelog.cf"
            symbol = "powiatwulkanowy"
            email = "jan@fakelog.cf"
            password = "jan123"
            schoolSymbol = "123456"
            studentId = 1
            diaryId = 101
            classId = 1
            androidVersion = "9.0"
            buildTag = "Wulkanowy"
            appInterceptors = listOf(Interceptor {
                println("Request event ${it.request().url().host()}")
                it.proceed(it.request())
            } to true)
        }
    }

    @Test
    fun getPasswordResetCaptchaCode() {
        val code = runBlocking { sdk.getPasswordResetCaptcha("https://fakelog.cf", "Default") }

        assertEquals("https://cufs.fakelog.cf/Default/AccountManage/UnlockAccount", code.first)
        assertEquals("6LeAGMYUAAAAAMszd5VWZTEb5WQHqsNT1F4GCqUd", code.second)
    }

    @Test
    fun sendPasswordResetRequest() {
        val res = runBlocking {
            sdk.sendPasswordResetRequest("https://fakelog.cf",
                "Default",
                "jan@fakelog.cf",
                "03AOLTBLQRPyr0pWvWLRAgD4hRLfxktoqD2IVweeMuXwbkpR_8S9YQtcS3cAXqUOyEw3NxfvwzV0lTjgFWyl8j3UXGQpsc2nvQcqIofj1N8DYfxvtZO-h24W_S0Z9-fDnfXErd7vERS-Ny4d5IU1FupBAEKvT8rrf3OA3GYYbMM7TwB8b_o9Tt192TqYnSxkyIYE4UdaZnLBA0KIXxlBAoqM6QGlPEsSPK9gmCGx-0hn68w-UBQkv_ghRruf4kpv2Shw5emcP-qHBlv3YjAagsb_358K0v8uGJeyLrx4dXN9Ky02TXFMKYWNHz29fjhfunxT73u_PrsLj56f-MjOXrqO894NkUlJ7RkTTclwIsqXtJ794LEBH--mtsqZBND0miR5-odmZszqiNB3V5UsS5ObsqF_fWMl2TCWyNTTvF4elOGwOEeKiumVpjB6e740COxvxN3vbkNWxP9eeghpd5nPN5l2wUV3VL2R5s44TbqHqkrkNpUOd3h7efs3cQtCfGc-tCXoqLC26LxT7aztvKpjXMuqGEf-7wbQ")
        }

        assertTrue(res.startsWith("Wysłano wiadomość na zapisany w systemie adres e-mail"))
    }

    @Test
    fun studentsTest() {
        val students = runBlocking { sdk.getStudents() }

        students[0].run {
            assertEquals("powiatwulkanowy", symbol)
            assertEquals("jan@fakelog.cf", email)
            assertEquals("Jan", studentName)
            assertEquals("Kowalski", studentSurname)
            assertEquals("123456", schoolSymbol)
            assertEquals(1, studentId)
            assertEquals(1, classId)
            assertEquals("A", className)
            assertEquals("Publiczna szkoła Wulkanowego nr 1 w fakelog.cf", schoolName)
        }
    }

    @Test
    fun semestersTest() {
        val semesters = runBlocking { sdk.getSemesters() }

        semesters[0].run {
            assertEquals(15, diaryId)
            assertEquals("4A", diaryName)
//            assertEquals(true, current)
        }

        semesters[3].run {
            assertEquals(13, diaryId)
            assertEquals("3A", diaryName)
            assertEquals(2017, schoolYear)
        }

        semesters[5].run {
            assertEquals(11, diaryId)
            assertEquals("2A", diaryName)
            assertEquals(2016, schoolYear)
//            assertEquals(11, semesterId)
//            assertEquals(1, semesterNumber)
        }

        semesters[6].run {
            //            assertEquals(12, semesterId)
//            assertEquals(2, semesterNumber)
        }
    }

    @Test
    fun attendanceTest() {
        val attendance = runBlocking { sdk.getAttendance(getLocalDate(2018, 10, 1)) }

        attendance[0].run {
            assertEquals(1, number)
            assertEquals("Zajęcia z wychowawcą", subject)
            assertEquals(getDate(2018, 10, 1), date)

            assertEquals(AttendanceCategory.PRESENCE, category)
        }

        attendance[1].run {
            assertEquals(AttendanceCategory.ABSENCE_UNEXCUSED, category)
        }

        assertEquals(AttendanceCategory.ABSENCE_UNEXCUSED, attendance[3].category)
        assertEquals(AttendanceCategory.ABSENCE_UNEXCUSED, attendance[4].category)
        assertEquals(AttendanceCategory.ABSENCE_EXCUSED, attendance[5].category)
        assertEquals(AttendanceCategory.UNEXCUSED_LATENESS, attendance[6].category)
        assertEquals(AttendanceCategory.PRESENCE, attendance[9].category)
    }

    @Test
    fun getSubjects() {
        val subjects = runBlocking { sdk.getSubjects() }

        assertEquals(17, subjects.size)

        subjects[0].run {
            assertEquals(-1, value)
            assertEquals("Wszystkie", name)
        }
    }

    @Test
    fun attendanceSummaryTest() {
        val attendance = runBlocking { sdk.getAttendanceSummary() }

        assertEquals(10, attendance.size)

        attendance[0].run {
            assertEquals(Month.SEPTEMBER, month)
            assertEquals(32, presence)
            assertEquals(1, absence)
            assertEquals(2, absenceExcused)
            assertEquals(3, absenceForSchoolReasons)
            assertEquals(4, lateness)
            assertEquals(5, latenessExcused)
            assertEquals(6, exemption)
        }

        assertEquals(64, attendance[1].presence)
    }

    @Test
    fun examsTest() {
        val exams = runBlocking { sdk.getExams(getLocalDate(2018, 5, 7)) }

        exams[0].run {
            assertEquals(getDate(2018, 5, 7), date)
            assertEquals(getDate(1970, 1, 1), entryDate)
            assertEquals("Matematyka", subject)
            assertEquals("", group)
            assertEquals("Sprawdzian", type)
            assertEquals("Figury na płaszczyźnie.", description)
            assertEquals("Aleksandra Krajewska", teacher)
            assertEquals("AK", teacherSymbol)
        }
    }

    @Test
    fun homeworkTest() {
        val homework = runBlocking { sdk.getHomework(getLocalDate(2018, 9, 11)) }

        homework[1].run {
            assertEquals(getDate(2018, 9, 11), date)
            assertEquals(getDate(2017, 10, 26), entryDate)
            assertEquals("Etyka", subject)
            assertEquals("Notatka własna do zajęć o ks. Jerzym Popiełuszko.", content)
            assertEquals("Michał Mazur", teacher)
            assertEquals("MM", teacherSymbol)
        }
    }

    @Test
    fun notesTest() {
        val notes = runBlocking { sdk.getNotes() }

        notes[0].run {
            assertEquals(getDate(2018, 1, 16), date)
            assertEquals("Stanisław Krupa", teacher)
            assertEquals("BS", teacherSymbol)
            assertEquals("Kultura języka", category)
            assertEquals("Litwo! Ojczyzno moja! Ty jesteś jak zdrowie. Ile cię trzeba cenić, ten tylko aż kędy pieprz rośnie gdzie podział się? szukać prawodawstwa.", content)
        }
    }

    @Test
    fun gradesTest() {
        val grades = runBlocking { sdk.getGradesDetails(865) }

        grades[5].run {
            assertEquals("Religia", subject)
            assertEquals("1", entry)
            assertEquals("6ECD07", color)
            assertEquals("Kart", symbol)
            assertEquals("", description)
            assertEquals("3,00", weight)
            assertEquals(3.0, weightValue, .0)
            assertEquals(getDate(2018, 11, 19), date)
            assertEquals("Michał Mazur", teacher)
        }

        grades[0].run {
            assertEquals("Bież", symbol)
            assertEquals("", description)
        }
    }

    @Test
    fun gradesSummaryTest() {
        val summary = runBlocking { sdk.getGradesSummary(865) }

        summary[2].run {
            assertEquals("Etyka", name)
            assertEquals("4", predicted)
            assertEquals("4", final)
        }

        summary[5].run {
            assertEquals("Historia", name)
            assertEquals("4", predicted)
            assertEquals("4", final)
        }

        summary[8].run {
            assertEquals("Język niemiecki", name)
            assertEquals("", predicted)
            assertEquals("", final)
        }
    }

    @Test
    fun gradesStatisticsTest() {
        val stats = runBlocking { sdk.getGradesPartialStatistics(321) }

        assertEquals("Język polski", stats[0].subject)
        assertEquals("Matematyka", stats[7].subject)

        val annual = runBlocking { sdk.getGradesSemesterStatistics(123) }

        assertEquals("Język angielski", annual[0].subject)
    }

    @Test
    fun teachersTest() {
        val teachers = runBlocking { sdk.getTeachers() }

        assertEquals("Historia", teachers[1].subject)
        assertEquals("Aleksandra Krajewska", teachers[1].name)
        assertEquals("AK", teachers[1].short)
    }

    @Test
    fun schoolTest() {
        val school = runBlocking { sdk.getSchool() }

        assertEquals("Publiczna szkoła Wulkanowego nr 1 w fakelog.cf", school.name)
    }

    @Test
    fun studentInfoTest() {
//         val info = runBlocking { api.getStudentInfo() }
//
//         info.run {
//             assertEquals("Jan Marek Kowalski", student.fullName)
//             assertEquals("Jan", student.firstName)
//             assertEquals("Marek", student.secondName)
//             assertEquals("Kowalski", student.surname)
//             assertEquals(getDate(1970, 1, 1), student.birthDate)
//             assertEquals("Warszawa", student.birthPlace)
//             assertEquals("12345678900", student.pesel)
//             assertEquals("Mężczyzna", student.gender)
//             assertEquals("1", student.polishCitizenship)
//             assertEquals("Nowak", student.familyName)
//             assertEquals("Monika, Kamil", student.parentsNames)
//
//             assertEquals("", student.address)
//             assertEquals("", student.registeredAddress)
//             assertEquals("", student.correspondenceAddress)
//
//             assertEquals("", student.phoneNumber)
//             assertEquals("-", student.cellPhoneNumber)
// //            assertEquals("jan@fakelog.cf", student.email)
//
//             family[0].run {
//                 assertEquals("Monika Nowak", fullName)
//                 assertEquals("-", email)
//             }
//
//             assertEquals("-", family[1].email)
//         }
    }

    @Test
    fun messagesTest() {
        val units = runBlocking { sdk.getReportingUnits() }
        assertEquals(1, units.size)

        val recipients = runBlocking { sdk.getRecipients(6) }
        assertEquals(10, recipients.size)

        val messages = runBlocking { sdk.getMessages(Folder.RECEIVED) }
        assertEquals(2, messages.size)

        val inbox = runBlocking { sdk.getReceivedMessages(getLocalDateTime(2015, 10, 5)) }
        assertEquals(2, inbox.size)

        val sent = runBlocking { sdk.getSentMessages() }
        assertEquals(1, sent.size)

        val trash = runBlocking { sdk.getDeletedMessages() }
        assertEquals(1, trash.size)

        val mRecipients = runBlocking { sdk.getMessageRecipients(trash[0].messageId ?: 0) }
        assertEquals(2, mRecipients.size)

        val details = runBlocking { sdk.getMessageDetails(trash[0].messageId ?: 0, trash[0].folderId) }
        assertEquals(27214, details.id)
    }

    @Test
    fun sendMessage() {
        runBlocking {
            sdk.sendMessage("Temat wiadomości", "Treść",
                listOf(Recipient("0", "Kowalski Jan", 0, 0, 2, "hash"))
            )
        }
    }

    @Test
    fun devicesTest() {
        val devices = runBlocking { sdk.getRegisteredDevices() }

        assertEquals(2, devices.size)
    }

    @Test
    fun tokenTest() {
        val tokenizer = runBlocking { sdk.getToken() }

        tokenizer.run {
            assertEquals("FK100000", token)
            assertEquals("powiatwulkanowy", symbol)
            assertEquals("999999", pin)
        }
    }

    @Test
    fun unregisterTest() {
        val unregister = runBlocking { sdk.unregisterDevice(1234) }

        assertEquals(true, unregister)
    }

    @Test
    fun timetableTest() {
        val timetable = runBlocking { sdk.getTimetableNormal(getLocalDate(2018, 9, 17)) }

        timetable[0].run {
            assertEquals(1, number)
            assertEquals("Fizyka", subject)
            assertEquals("Karolina Kowalska", teacher)
            assertEquals("", group)
            assertEquals("213", room)
            assertEquals("", info)
            assertEquals(false, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun realizedTest() {
        val realized = runBlocking { sdk.getCompletedLessons(getLocalDate(2018, 9, 17)) }

        realized[0].run {
            assertEquals(getDate(2018, 9, 17), date)
            assertEquals(1, number)
            assertEquals("Historia i społeczeństwo", subject)
            assertEquals("Powstanie listopadowe", topic)
            assertEquals("Histeryczna Jadwiga", teacher)
            assertEquals("Hi", teacherSymbol)
            assertEquals("Nieobecność nieusprawiedliwiona", absence)
        }
    }

    @Test
    fun luckyNumberTest() {
        val luckyNumber = runBlocking { sdk.getKidsLuckyNumbers() }

        assertEquals(37, luckyNumber[0].number)
    }

    @Test
    fun freeDays() {
        val freeDays = runBlocking { sdk.getFreeDays() }
        assertEquals(2, freeDays.size)
    }
}
