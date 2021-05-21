package io.github.wulkanowy.sdk.scrapper.timetable

import io.github.wulkanowy.sdk.scrapper.BaseLocalTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimetableTest : BaseLocalTest() {

    private val timetable by lazy {
        runBlocking { getStudentRepo(TimetableTest::class.java, "PlanLekcji.json").getTimetableNormal(getLocalDate(2018, 9, 24)) }
    }

    private val timetableBefore1911 by lazy {
        runBlocking { getStudentRepo(TimetableTest::class.java, "PlanLekcji-before-19.11.json").getTimetableNormal(getLocalDate(2018, 9, 24)) }
    }

    private val additional by lazy {
        runBlocking { getStudentRepo(TimetableTest::class.java, "PlanLekcji.json").getTimetableAdditional(getLocalDate(2020, 10, 9)) }
    }

    companion object {
        private const val MONDAY_OFFSET = 0
        private const val TUESDAY_OFFSET = 5
        private const val WEDNESDAY_OFFSET = 10
        private const val THURSDAY_OFFSET = 15
        private const val FRIDAY_OFFSET = 20
    }

    @Test
    fun getTimetableTest() {
        assertEquals(25, timetable.size)
        assertEquals(18, timetableBefore1911.size)
    }

    @Test
    fun getSimpleLesson() {
        with(timetable[MONDAY_OFFSET]) {
            // poniedziałek, 0
            assertEquals(0, number)
            assertEquals(getDate(2018, 9, 24, 7, 10, 0), start)
            assertEquals(getDate(2018, 9, 24, 7, 55, 0), end)

            assertEquals("Matematyka", subject)
            assertEquals("Rachunek Beata", teacher)
            assertEquals("23", room)
            assertEquals("", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals("", group)
            assertEquals("", info)
            assertEquals(false, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getSimpleLesson_canceled() {
        with(timetable[TUESDAY_OFFSET]) {
            // wtorek, 0
            assertEquals(0, number)
            assertEquals(getDate(2018, 9, 25, 7, 10, 0), start)

            assertEquals("Język polski", subject)
            assertEquals("Polonistka Joanna", teacher)
            assertEquals("W3", room)
            assertEquals("oddział nieobecny - Wycieczka warsztat", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)
            assertEquals(true, canceled)

            assertEquals("", group)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getSimpleLesson_replacementSameTeacher() {
        with(timetable[WEDNESDAY_OFFSET]) {
            // środa, 0
            assertEquals(0, number)
            assertEquals(getDate(2018, 9, 26, 7, 10, 0), start)

            assertEquals("Język polski", subject)
            assertEquals("Polonistka Joanna", teacher)
            assertEquals("5", room)
            assertEquals("", info)
            assertEquals("Religia", subjectOld)
            assertEquals("Polonistka Joanna", teacherOld)
            assertEquals("3", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)

            assertEquals("", group)
        }
    }

    @Test
    fun getSimpleLesson_replacementDifferentTeacher() {
        with(timetable[THURSDAY_OFFSET]) {
            // czwartek, 0
            assertEquals(0, number)
            assertEquals(getDate(2018, 9, 27, 7, 10, 0), start)

            assertEquals("Wychowanie do życia w rodzinie", subject)
            assertEquals("Telichowska Aleksandra", teacher)
            assertEquals("5", room)
            assertEquals("zastępstwo, poprzednio: Religia", info)
            assertEquals("Religia", subjectOld)
            assertEquals("Religijny Janusz", teacherOld)
            assertEquals("3", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)

            assertEquals("", group)
        }
    }

    @Test
    fun getGroupLesson() {
        with(timetable[FRIDAY_OFFSET]) {
            // piątek, 0
            assertEquals(0, number)
            assertEquals(getDate(2018, 9, 28, 7, 10, 0), start)

            assertEquals("Fizyka", subject)
            assertEquals("zaw2", group)
            assertEquals("Fizyczny Janusz", teacher)
            assertEquals("19", room)
            assertEquals("", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(false, changes)
            assertEquals("", info)
        }
    }

    @Test
    fun getGroupLesson_canceled() {
        with(timetable[MONDAY_OFFSET + 1]) {
            // poniedziałek, 1
            assertEquals(1, number)
            assertEquals(getDate(2018, 9, 24, 8, 0, 0), start)

            assertEquals("Język angielski", subject)
            assertEquals("J1", group)
            assertEquals("Angielska Amerykanka", teacher)
            assertEquals("24", room)
            assertEquals("uczniowie przychodzą później", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(true, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getGroupLesson_replacementSameTeacher() {
        with(timetable[TUESDAY_OFFSET + 1]) {
            // wtorek, 1
            assertEquals(1, number)
            assertEquals(getDate(2018, 9, 25, 8, 0, 0), start)

            assertEquals("Wychowanie fizyczne", subject)
            assertEquals("zaw2", group)
            assertEquals("Wychowawczy Kazimierz", teacher)
            assertEquals("WG", room)
            assertEquals("", info)
            assertEquals("Naprawa komputera", subjectOld)
            assertEquals("Naprawowy Andrzej", teacherOld)
            assertEquals("32", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getGroupLesson_replacementDifferentTeacher() {
        with(timetable[WEDNESDAY_OFFSET + 1]) {
            // środa, 1
            assertEquals(1, number)
            assertEquals(getDate(2018, 9, 26, 8, 0, 0), start)

            assertEquals("Tworzenie i administrowanie bazami danych", subject)
            assertEquals("Kobyliński Leszek", teacher)
            assertEquals("34", room)
            assertEquals("zastępstwo, poprzednio: Tworzenie i administrowanie bazami danych", info)
            assertEquals("Tworzenie i administrowanie bazami danych", subjectOld)
            assertEquals("Dębicki Robert", teacherOld)
            assertEquals("34", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)

            assertEquals("zaw2", group)
        }
    }

    @Test
    fun getLesson_button() {
        with(timetable[THURSDAY_OFFSET + 1]) {
            // czwartek, 1
            assertEquals(1, number)
            assertEquals(getDate(2018, 9, 27, 8, 0, 0), start)

            assertEquals("Język polski", subject)
            assertEquals("Polonistka Joanna", teacher)
            assertEquals("16", room)
            assertEquals("oddział nieobecny: egzamin", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(true, canceled)
            assertEquals(false, changes)

            assertEquals("", group)
        }
    }

    @Test
    fun getLesson_emptyOriginal() {
        with(timetable[FRIDAY_OFFSET + 1]) {
            // piątek, 1
            assertEquals(1, number)
            assertEquals(getDate(2018, 9, 28, 8, 0, 0), start)

            assertEquals("Wychowanie fizyczne", subject)
            assertEquals("zaw2", group)
            assertEquals("", teacher)
            assertEquals("G3", room)
            assertEquals("przeniesiona z lekcji 7, 01.12.2017", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getLesson() {
        with(timetable[MONDAY_OFFSET + 2]) {
            // poniedziałek, 2
            assertEquals(2, number)
            assertEquals(getDate(2018, 9, 24, 8, 50, 0), start)

            assertEquals("Geografia", subject)
            assertEquals("", group)
            assertEquals("Światowy Michał", teacher)
            assertEquals("23", room)
            assertEquals("zastępstwo, poprzednio: Religia", info)
            assertEquals("Religia", subjectOld)
            assertEquals("Religijny Janusz", teacherOld)
            assertEquals("23", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getLesson_invAndChange() {
        with(timetable[TUESDAY_OFFSET + 2]) {
            // wtorek, 2
            assertEquals(2, number)
            assertEquals(getDate(2018, 9, 25, 8, 50, 0), start)

            assertEquals("Język angielski", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("", room)
            assertEquals("przeniesiona z lekcji 4, 07.03.2019", info)
            assertEquals("Matematyka", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getSimpleLesson_replacementDifferentTeacherv2() {
        with(timetable[WEDNESDAY_OFFSET + 2]) {
            // środa, 2
            assertEquals(2, number)
            assertEquals(getDate(2018, 9, 26, 8, 50, 0), start)

            assertEquals("Język angielski", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("", room)
            assertEquals("Poprzednio: Matematyka (przeniesiona na lekcję 2, 07.03.2019)", info)
            assertEquals("Matematyka", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getSimpleLesson_movedWithButton() {
        with(timetable[THURSDAY_OFFSET + 2]) {
            // czwartek, 2
            assertEquals(2, number)
            assertEquals(getDate(2018, 9, 27, 8, 50, 0), start)

            assertEquals("Podstawy przedsiębiorczości", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("", room)
            assertEquals("Przeniesiona: Podstawy przedsiębiorczości z 8 godz. z 25.03.", info)
            assertEquals("Urządzenia techniki komputerowej", subjectOld)
            assertEquals("Nowak Karolina", teacherOld)
            assertEquals("43", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getSimpleLesson_canceledWithoutReason() {
        with(timetable[FRIDAY_OFFSET + 2]) {
            // piątek, 2
            assertEquals(2, number)
            assertEquals(getDate(2018, 9, 28, 8, 50, 0), start)

            assertEquals("Wychowanie fizyczne", subject)
            assertEquals("CH", group)
            assertEquals("Imię Nauczyciela", teacher)
            assertEquals("", room)
            assertEquals("", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(true, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getLesson_buttonWithChanges() {
        with(timetable[MONDAY_OFFSET + 3]) {
            // poniedziałek, 3
            assertEquals(3, number)
            assertEquals(getDate(2018, 9, 24, 9, 45, 0), start)
            assertEquals(getDate(2018, 9, 24, 10, 40, 0), end)

            assertEquals("Historia", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("6", room)
            assertTrue("przeniesiona z lekcji 7, 30.04.2019: Historia z 7 godz. lekcyjnej.".startsWith(info)) // button doesn't exist in `student` ver
            assertEquals("Podstawy przedsiębiorczości", subjectOld)
            assertEquals("C Urszula", teacherOld)
            assertEquals("4", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getLesson_buttonWithChanges2() {
        with(timetable[TUESDAY_OFFSET + 3]) {
            // wtorek, 3
            assertEquals(3, number)
            assertEquals(getDate(2018, 9, 25, 9, 45, 0), start)
            assertEquals(getDate(2018, 9, 25, 10, 40, 0), end)

            assertEquals("Administracja i eksploatacja systemów komputerowych, urządzeń peryferyjnych i lokalnych sieci komputerowych", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("", room)
            assertEquals("z grupą - p. (Nazwisko nauczyciela, który ma zajęcia z drugą grupą)", info)
            assertEquals("Podstawy informatyki", subjectOld)
            assertEquals("(Imię i nazwisko nauczyciela, który miał mieć z nami zajęcia)", teacherOld)
            assertEquals("125", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getLesson_tripleChange() {
        with(timetable[WEDNESDAY_OFFSET + 3]) {
            // środa 3
            assertEquals(3, number)
            assertEquals(getDate(2018, 9, 26, 9, 45, 0), start)
            assertEquals(getDate(2018, 9, 26, 10, 40, 0), end)

            assertEquals("Eksploatacja urządzeń techniki komputerowej", subject)
            assertEquals("t.infor.", group)
            assertEquals("", teacher)
            assertEquals("216", room)
            assertEquals("przeniesiona z lekcji 6, 06.12.2019", info)
            assertEquals("Wiedza o kulturze", subjectOld)
            assertEquals("[nauczyciel od WOK]", teacherOld)
            assertEquals("010", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getLesson_tripleChange2() {
        with(timetable[THURSDAY_OFFSET + 3]) {
            // czwartek, 3
            assertEquals(3, number)
            assertEquals(getDate(2018, 9, 27, 9, 45, 0), start)
            assertEquals(getDate(2018, 9, 27, 10, 40, 0), end)

            assertEquals("systemy operacyjne", subject)
            assertEquals("t.infor.", group)
            assertEquals("", teacher)
            assertEquals("29", room)
            assertEquals("przeniesiona z lekcji 1, 10.02.2020", info)
            assertEquals("Wiedza o społeczeństwie", subjectOld)
            assertEquals("Jan Kowalski", teacherOld)
            assertEquals("50", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getLesson_two_change_lines_no_group() {
        with(timetable[FRIDAY_OFFSET + 3]) {
            // piątek, 3
            assertEquals(3, number)
            assertEquals(getDate(2018, 9, 28, 9, 45, 0), start)
            assertEquals(getDate(2018, 9, 28, 10, 40, 0), end)

            assertEquals("Matematyka", subject)
            assertEquals("", group)
            assertEquals("Jan Kowalski", teacher)
            assertEquals("114", room)
            assertEquals("przeniesiona z lekcji 5, 04.11.2020", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getClasslessLesson_withoutChanges() {
        with(timetable[MONDAY_OFFSET + 4]) {
            // poniedziałek, 4
            assertEquals(4, number)
            assertEquals(getDate(2018, 9, 24, 10, 50, 0), start)
            assertEquals(getDate(2018, 9, 24, 11, 35, 0), end)

            assertEquals("Fizyka", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("A10", room)
            assertEquals("", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getLightLesson_withChanges_butWithoutClasses() {
        with(timetable[TUESDAY_OFFSET + 4]) {
            // wtorek, 4
            assertEquals(4, number)
            assertEquals(getDate(2018, 9, 25, 10, 50, 0), start)
            assertEquals(getDate(2018, 9, 25, 11, 35, 0), end)

            assertEquals("podstawy algorytmiki i programowania", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("A114", room)
            assertEquals("zastępstwo: Stanisław Baran", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getClasslessLesson_groupWithChanges() {
        with(timetable[WEDNESDAY_OFFSET + 4]) {
            // środa, 4
            assertEquals(4, number)
            assertEquals(getDate(2018, 9, 26, 10, 50, 0), start)
            assertEquals(getDate(2018, 9, 26, 11, 35, 0), end)

            assertEquals("Język angielski", subject)
            assertEquals("JA 2", group)
            assertEquals("", teacher)
            assertEquals("A315", room)
            assertEquals("nieobecność nauczyciela: czytelnia", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(true, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getLightLesson_withChanges_withInvClass() {
        with(timetable[THURSDAY_OFFSET + 4]) {
            // czwartek, 4
            assertEquals(4, number)
            assertEquals(getDate(2018, 9, 27, 10, 50, 0), start)
            assertEquals(getDate(2018, 9, 27, 11, 35, 0), end)

            assertEquals("Fizyka", subject)
            assertEquals("", group)
            assertEquals("", teacher)
            assertEquals("A10", room)
            assertEquals("nieobecność nauczyciela: uczniowie przychodzą później", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(true, canceled)
            assertEquals(false, changes)
        }
    }

    @Test
    fun getLightLesson_withoutClasses_withSubstitution() {
        with(timetable[FRIDAY_OFFSET + 4]) {
            // piątek, 4
            assertEquals(4, number)
            assertEquals(getDate(2018, 9, 28, 10, 50, 0), start)
            assertEquals(getDate(2018, 9, 28, 11, 35, 0), end)

            assertEquals("pracownia programowania", subject)
            assertEquals("JA 2", group)
            assertEquals("", teacher)
            assertEquals("B149", room)
            assertEquals("zastępstwo: Tabaluga Jakub", info)
            assertEquals("", subjectOld)
            assertEquals("", teacherOld)
            assertEquals("", roomOld)

            assertEquals(false, canceled)
            assertEquals(true, changes)
        }
    }

    @Test
    fun getAdditionalLesson() {
        with(additional[0]) {
            assertEquals(getDate(2020, 3, 9), date)
            assertEquals(getDate(2020, 3, 9, 14, 50, 0), start)
            assertEquals(getDate(2020, 3, 9, 15, 35, 0), end)

            assertEquals("zajęcia wyrównawcze dla kl. 6d", subject)
        }
    }

    @Test
    fun getAdditionalLessonWithSpan() {
        with(additional[1]) {
            assertEquals(getDate(2020, 3, 11), date)
            assertEquals(getDate(2020, 3, 11, 13, 20, 0), start)
            assertEquals(getDate(2020, 3, 11, 14, 5, 0), end)

            assertEquals("Zajęcia dodatkowe 2", subject)
        }
    }
}
