package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AcademyRepository
import com.example.model.CourseCategory
import com.example.model.QuizAttemptResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Teacher Vision Academy", appName)
    }

    @Test
    fun `verify admin login recognition for 7888213000`() {
        AcademyRepository.login("7888213000", "Admin Owner", CourseCategory.UGC_NET)
        val session = AcademyRepository.userSession.value
        assertTrue(session.isAdmin)
        assertTrue(session.isLoggedIn)
        assertEquals("7888213000", session.mobile)
    }

    @Test
    fun `verify live updates and study folders present`() {
        val updates = AcademyRepository.liveUpdates.value
        assertTrue(updates.isNotEmpty())
        assertTrue(updates.any { it.title.contains("UGC NET") })

        val folders = AcademyRepository.folders.value
        assertTrue(folders.isNotEmpty())
        assertTrue(folders.any { it.courseCategory == CourseCategory.UGC_NET })
    }

    @Test
    fun `verify quiz scoring and recording`() {
        val quiz = AcademyRepository.quizzes.value.first()
        assertNotNull(quiz)

        val result = QuizAttemptResult(
            quizId = quiz.id,
            quizTitle = quiz.title,
            totalQuestions = quiz.questions.size,
            correctAnswers = 3,
            wrongAnswers = 1,
            skippedAnswers = 1,
            score = 5.5,
            totalMarks = 10.0,
            percentage = 55.0,
            timeTakenSeconds = 120,
            userAnswers = mapOf(0 to 2, 1 to 1)
        )
        AcademyRepository.recordAttempt(result)
        assertEquals(1, AcademyRepository.attemptHistory.value.size)
    }
}
