package com.example.model

enum class CourseCategory(val displayName: String, val displayNamePa: String, val displayNameHi: String) {
    UGC_NET("UGC NET", "ਯੂ.ਜੀ.ਸੀ. ਨੈੱਟ", "यूजीसी नेट"),
    B_ED("B.Ed", "ਬੀ.ਐੱਡ.", "बी.एड"),
    MA("MA", "ਐੱਮ.ਏ.", "एम.ए."),
    BA("BA", "ਬੀ.ਏ.", "बी.ए."),
    PUNJAB_GOVT("Punjab Govt Exams", "ਪੰਜਾਬ ਸਰਕਾਰੀ ਪ੍ਰੀਖਿਆਵਾਂ", "पंजाब सरकारी परीक्षाएं")
}

enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    PUNJABI("pa", "ਪੰਜਾਬੀ"),
    HINDI("hi", "हिंदी")
}

data class UserSession(
    val mobile: String = "",
    val name: String = "",
    val selectedCourse: CourseCategory = CourseCategory.UGC_NET,
    val isAdmin: Boolean = false,
    val isLoggedIn: Boolean = false,
    val enrolledCourses: List<String> = listOf("UGC_NET_P1", "PUNJAB_MC_PUNJABI")
)

data class LiveBannerUpdate(
    val id: String,
    val title: String,
    val titlePa: String,
    val titleHi: String,
    val subtitle: String,
    val tag: String,
    val date: String,
    val isLiveNow: Boolean = false,
    val linkUrl: String = ""
)

data class AcademyCourse(
    val id: String,
    val title: String,
    val category: CourseCategory,
    val description: String,
    val instructor: String,
    val fee: Int,
    val originalFee: Int,
    val totalLectures: Int,
    val totalNotes: Int,
    val totalTests: Int,
    val isEnrolled: Boolean = false
)

data class StudyFolder(
    val id: String,
    val name: String,
    val namePa: String,
    val courseCategory: CourseCategory,
    val parentFolderId: String? = null,
    val itemCount: Int = 0
)

data class StudyDocument(
    val id: String,
    val folderId: String,
    val courseCategory: CourseCategory,
    val title: String,
    val titlePa: String,
    val unitName: String,
    val pagesCount: Int,
    val readTimeMinutes: Int,
    val isFreePreview: Boolean = true,
    val pages: List<DocumentPage>
)

data class DocumentPage(
    val pageNumber: Int,
    val heading: String,
    val content: String,
    val keyPoints: List<String> = emptyList(),
    val punjabiSummary: String = "",
    val hindiSummary: String = ""
)

data class QuizQuestion(
    val id: String,
    val questionEn: String,
    val questionPa: String,
    val questionHi: String,
    val optionsEn: List<String>,
    val optionsPa: List<String>,
    val optionsHi: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationPa: String,
    val explanationHi: String
)

data class Quiz(
    val id: String,
    val title: String,
    val titlePa: String,
    val category: CourseCategory,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val marksPerQuestion: Int = 2,
    val negativeMarks: Double = 0.5,
    val questions: List<QuizQuestion>
)

data class QuizAttemptResult(
    val quizId: String,
    val quizTitle: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedAnswers: Int,
    val score: Double,
    val totalMarks: Double,
    val percentage: Double,
    val timeTakenSeconds: Int,
    val userAnswers: Map<Int, Int>,
    val timestamp: Long = System.currentTimeMillis()
)

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val city: String,
    val score: Int,
    val accuracy: String,
    val badge: String
)

data class LiveClassItem(
    val id: String,
    val title: String,
    val subject: String,
    val educator: String,
    val scheduledTime: String,
    val isLiveNow: Boolean,
    val streamType: String = "YouTube Live",
    val streamUrl: String = "https://youtube.com/live/teachervision",
    val viewerCount: Int = 142
)

data class StudentEnrollment(
    val id: String,
    val name: String,
    val mobile: String,
    val course: CourseCategory,
    val joinDate: String,
    val feePaid: Boolean = true
)
