package io.github.wulkanowy.api.grades

import io.github.wulkanowy.api.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GradesSummaryTest : BaseTest() {

    private val std by lazy {
        getFixture(GradesSummaryTest::class, GradesSummaryResponse::class.java, "OcenyWszystkie-subjects.html").subjects
    }

    private val average by lazy {
        getFixture(GradesSummaryTest::class, GradesSummaryResponse::class.java, "OcenyWszystkie-subjects-average.html").subjects
    }

    @Test fun getSummaryTest() {
        assertEquals(5, std.size)
        assertEquals(5, average.size)
    }

    @Test fun getNameTest() {
        assertEquals("Zachowanie", std[0].name)
        assertEquals("Praktyka zawodowa", std[1].name)
        assertEquals("Metodologia programowania", std[2].name)
        assertEquals("Podstawy przedsiębiorczości", std[3].name)
        assertEquals("Wychowanie do życia w rodzinie", std[4].name)

        assertEquals("Zachowanie", average[0].name)
        assertEquals("Język polski", average[1].name)
        assertEquals("Wychowanie fizyczne", average[2].name)
        assertEquals("Język angielski", average[3].name)
        assertEquals("Wiedza o społeczeństwie", average[4].name)
    }

    @Test fun getPredictedRatingTest() {
        assertEquals("bardzo dobre", std[0].predicted)
        assertEquals("", std[1].predicted)
        assertEquals("bardzo dobry", std[2].predicted)
        assertEquals("3/4", std[3].predicted)
        assertEquals("", std[4].predicted)

        assertEquals("bardzo dobre", average[0].predicted)
        assertEquals("", average[1].predicted)
        assertEquals("bardzo dobry", average[2].predicted)
        assertEquals("4/5", average[3].predicted)
        assertEquals("", average[4].predicted)
    }

    @Test
    fun getFinalRatingTest() {
        assertEquals("bardzo dobre", std[0].final)
        assertEquals("celujący", std[1].final)
        assertEquals("celujący", std[2].final)
        assertEquals("dostateczny", std[3].final)
        assertEquals("", std[4].final)

        assertEquals("bardzo dobre", average[0].final)
        assertEquals("dobry", average[1].final)
        assertEquals("celujący", average[2].final)
        assertEquals("bardzo dobry", average[3].final)
        assertEquals("", average[4].final)
    }
}
