package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AcademyRepository
import com.example.model.AcademyCourse
import com.example.model.AppLanguage
import com.example.model.CourseCategory
import com.example.model.LiveClassItem
import com.example.model.Quiz
import com.example.model.QuizAttemptResult
import com.example.model.StudyDocument
import com.example.ui.components.AcademyTopBar
import com.example.ui.components.WhatsAppFab
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CoursesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.PdfViewerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizHubScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.QuizSolutionReviewScreen
import com.example.ui.screens.QuizTakeScreen
import com.example.ui.screens.StudyMaterialScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyYellow
import com.example.ui.theme.MyApplicationTheme
import com.example.util.Localization

enum class MainTab {
    HOME,
    COURSES,
    STUDY_MATERIAL,
    TEST_SERIES,
    PROFILE
}

sealed class SubScreen {
    object None : SubScreen()
    data class PdfViewer(val document: StudyDocument) : SubScreen()
    data class VideoPlayer(val liveClass: LiveClassItem) : SubScreen()
    data class QuizTake(val quiz: Quiz) : SubScreen()
    data class QuizResult(val result: QuizAttemptResult, val quiz: Quiz) : SubScreen()
    data class QuizSolutionReview(val quiz: Quiz, val attempt: QuizAttemptResult) : SubScreen()
    object AdminPanel : SubScreen()
    data class Payment(val course: AcademyCourse) : SubScreen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                TeacherVisionApp()
            }
        }
    }
}

@Composable
fun TeacherVisionApp() {
    val userSession by AcademyRepository.userSession.collectAsStateWithLifecycle()
    val currentLanguage by AcademyRepository.currentLanguage.collectAsStateWithLifecycle()
    val liveUpdates by AcademyRepository.liveUpdates.collectAsStateWithLifecycle()
    val courses by AcademyRepository.courses.collectAsStateWithLifecycle()
    val folders by AcademyRepository.folders.collectAsStateWithLifecycle()
    val documents by AcademyRepository.documents.collectAsStateWithLifecycle()
    val quizzes by AcademyRepository.quizzes.collectAsStateWithLifecycle()
    val attemptHistory by AcademyRepository.attemptHistory.collectAsStateWithLifecycle()
    val liveClasses by AcademyRepository.liveClasses.collectAsStateWithLifecycle()
    val students by AcademyRepository.enrolledStudents.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(MainTab.HOME) }
    var subScreen by remember { mutableStateOf<SubScreen>(SubScreen.None) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    // If student is not logged in, show Login Screen
    if (!userSession.isLoggedIn) {
        LoginScreen(
            currentLanguage = currentLanguage,
            onLoginSuccess = { mobile, name, course ->
                AcademyRepository.login(mobile, name, course)
            }
        )
        return
    }

    // Back button handling
    BackHandler(enabled = subScreen !is SubScreen.None || activeTab != MainTab.HOME) {
        when {
            subScreen is SubScreen.QuizSolutionReview -> {
                val review = subScreen as SubScreen.QuizSolutionReview
                subScreen = SubScreen.QuizResult(review.attempt, review.quiz)
            }
            subScreen is SubScreen.QuizResult -> {
                subScreen = SubScreen.None
                activeTab = MainTab.TEST_SERIES
            }
            subScreen !is SubScreen.None -> {
                subScreen = SubScreen.None
            }
            activeTab != MainTab.HOME -> {
                activeTab = MainTab.HOME
            }
        }
    }

    // Check if current screen is fullscreen (PDF / Video / Quiz)
    val isFullscreenOverlay = subScreen is SubScreen.PdfViewer ||
            subScreen is SubScreen.VideoPlayer ||
            subScreen is SubScreen.QuizTake

    Scaffold(
        topBar = {
            if (!isFullscreenOverlay && subScreen !is SubScreen.AdminPanel && subScreen !is SubScreen.Payment) {
                AcademyTopBar(
                    userSession = userSession,
                    currentLanguage = currentLanguage,
                    onLanguageChange = { AcademyRepository.setLanguage(it) },
                    onNotificationClick = { showNotificationDialog = true },
                    onAdminClick = { subScreen = SubScreen.AdminPanel }
                )
            }
        },
        bottomBar = {
            if (!isFullscreenOverlay && subScreen !is SubScreen.AdminPanel && subScreen !is SubScreen.Payment) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = AcademyBlue,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = activeTab == MainTab.HOME,
                        onClick = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.HOME
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(Localization.getText("nav_home", currentLanguage), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AcademyBlue,
                            selectedTextColor = AcademyBlue,
                            indicatorColor = AcademyYellow.copy(alpha = 0.35f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == MainTab.COURSES,
                        onClick = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.COURSES
                        },
                        icon = { Icon(Icons.Default.School, contentDescription = null) },
                        label = { Text(Localization.getText("nav_courses", currentLanguage), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AcademyBlue,
                            selectedTextColor = AcademyBlue,
                            indicatorColor = AcademyYellow.copy(alpha = 0.35f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == MainTab.STUDY_MATERIAL,
                        onClick = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.STUDY_MATERIAL
                        },
                        icon = { Icon(Icons.Default.Description, contentDescription = null) },
                        label = { Text(Localization.getText("nav_material", currentLanguage), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AcademyBlue,
                            selectedTextColor = AcademyBlue,
                            indicatorColor = AcademyYellow.copy(alpha = 0.35f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == MainTab.TEST_SERIES,
                        onClick = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.TEST_SERIES
                        },
                        icon = { Icon(Icons.Default.Quiz, contentDescription = null) },
                        label = { Text(Localization.getText("nav_tests", currentLanguage), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AcademyBlue,
                            selectedTextColor = AcademyBlue,
                            indicatorColor = AcademyYellow.copy(alpha = 0.35f)
                        )
                    )

                    NavigationBarItem(
                        selected = activeTab == MainTab.PROFILE,
                        onClick = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.PROFILE
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text(Localization.getText("nav_profile", currentLanguage), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AcademyBlue,
                            selectedTextColor = AcademyBlue,
                            indicatorColor = AcademyYellow.copy(alpha = 0.35f)
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (!isFullscreenOverlay && subScreen !is SubScreen.AdminPanel) {
                WhatsAppFab(
                    modifier = Modifier.padding(bottom = 60.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = subScreen) {
                is SubScreen.PdfViewer -> {
                    PdfViewerScreen(
                        document = screen.document,
                        userSession = userSession,
                        currentLanguage = currentLanguage,
                        onBack = { subScreen = SubScreen.None }
                    )
                }
                is SubScreen.VideoPlayer -> {
                    VideoPlayerScreen(
                        liveClass = screen.liveClass,
                        userSession = userSession,
                        currentLanguage = currentLanguage,
                        onBack = { subScreen = SubScreen.None }
                    )
                }
                is SubScreen.QuizTake -> {
                    QuizTakeScreen(
                        quiz = screen.quiz,
                        userSession = userSession,
                        onFinishQuiz = { result ->
                            AcademyRepository.recordAttempt(result)
                            subScreen = SubScreen.QuizResult(result, screen.quiz)
                        },
                        onExit = { subScreen = SubScreen.None }
                    )
                }
                is SubScreen.QuizResult -> {
                    QuizResultScreen(
                        result = screen.result,
                        quiz = screen.quiz,
                        onReviewSolutions = {
                            subScreen = SubScreen.QuizSolutionReview(screen.quiz, screen.result)
                        },
                        onBackToHub = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.TEST_SERIES
                        }
                    )
                }
                is SubScreen.QuizSolutionReview -> {
                    QuizSolutionReviewScreen(
                        quiz = screen.quiz,
                        attempt = screen.attempt,
                        onBack = {
                            subScreen = SubScreen.QuizResult(screen.attempt, screen.quiz)
                        }
                    )
                }
                is SubScreen.AdminPanel -> {
                    AdminScreen(
                        students = students,
                        folders = folders,
                        onBack = { subScreen = SubScreen.None }
                    )
                }
                is SubScreen.Payment -> {
                    PaymentScreen(
                        course = screen.course,
                        userSession = userSession,
                        onPaymentSuccess = {
                            subScreen = SubScreen.None
                            activeTab = MainTab.STUDY_MATERIAL
                        },
                        onBack = { subScreen = SubScreen.None }
                    )
                }
                SubScreen.None -> {
                    when (activeTab) {
                        MainTab.HOME -> {
                            HomeScreen(
                                userSession = userSession,
                                currentLanguage = currentLanguage,
                                liveUpdates = liveUpdates,
                                liveClasses = liveClasses,
                                recentDocuments = documents,
                                onNavigateCourses = { activeTab = MainTab.COURSES },
                                onNavigateLiveClasses = {
                                    val firstLive = liveClasses.firstOrNull { it.isLiveNow } ?: liveClasses.first()
                                    subScreen = SubScreen.VideoPlayer(firstLive)
                                },
                                onNavigateTestSeries = { activeTab = MainTab.TEST_SERIES },
                                onNavigateStudyMaterial = { activeTab = MainTab.STUDY_MATERIAL },
                                onOpenDocument = { doc -> subScreen = SubScreen.PdfViewer(doc) },
                                onOpenLiveClass = { lc -> subScreen = SubScreen.VideoPlayer(lc) },
                                onOpenAdminPanel = { subScreen = SubScreen.AdminPanel },
                                onCourseCategoryChange = { AcademyRepository.selectCourse(it) }
                            )
                        }
                        MainTab.COURSES -> {
                            CoursesScreen(
                                courses = courses,
                                currentLanguage = currentLanguage,
                                onEnrollCourse = { course -> subScreen = SubScreen.Payment(course) },
                                onOpenStudyMaterial = { activeTab = MainTab.STUDY_MATERIAL }
                            )
                        }
                        MainTab.STUDY_MATERIAL -> {
                            StudyMaterialScreen(
                                currentLanguage = currentLanguage,
                                selectedCategory = userSession.selectedCourse,
                                folders = folders,
                                documents = documents,
                                onCategoryChange = { AcademyRepository.selectCourse(it) },
                                onOpenDocument = { doc -> subScreen = SubScreen.PdfViewer(doc) }
                            )
                        }
                        MainTab.TEST_SERIES -> {
                            QuizHubScreen(
                                userSession = userSession,
                                currentLanguage = currentLanguage,
                                quizzes = quizzes,
                                attemptHistory = attemptHistory,
                                leaderboard = AcademyRepository.leaderboard,
                                onStartQuiz = { quiz -> subScreen = SubScreen.QuizTake(quiz) }
                            )
                        }
                        MainTab.PROFILE -> {
                            ProfileScreen(
                                userSession = userSession,
                                currentLanguage = currentLanguage,
                                onLanguageChange = { AcademyRepository.setLanguage(it) },
                                onCourseChange = { AcademyRepository.selectCourse(it) },
                                onOpenAdminPanel = { subScreen = SubScreen.AdminPanel },
                                onLogout = { AcademyRepository.logout() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Notification broadcast dialog
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = AcademyBlue)
                    Text(" Live Broadcast Notifications", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(liveUpdates) { update ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = update.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AcademyBlueDark
                                )
                                Text(
                                    text = update.subtitle,
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "${update.tag} • ${update.date}",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("Dismiss", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
