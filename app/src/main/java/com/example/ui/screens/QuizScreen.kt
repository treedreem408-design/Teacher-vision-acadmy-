package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.model.AppLanguage
import com.example.model.CourseCategory
import com.example.model.LeaderboardUser
import com.example.model.Quiz
import com.example.model.QuizAttemptResult
import com.example.model.QuizQuestion
import com.example.model.UserSession
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyRed
import com.example.ui.theme.AcademyYellow
import com.example.util.EnforceScreenSecurity
import com.example.util.Localization
import kotlinx.coroutines.delay

@Composable
fun QuizHubScreen(
    userSession: UserSession,
    currentLanguage: AppLanguage,
    quizzes: List<Quiz>,
    attemptHistory: List<QuizAttemptResult>,
    leaderboard: List<LeaderboardUser>,
    onStartQuiz: (Quiz) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Available Tests, 1: Leaderboard, 2: History

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AcademyBlue)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        text = Localization.getText("quiz_hub", currentLanguage),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real Exam Pattern • Timer & Negative Marking • Instant Solutions",
                        color = AcademyYellow,
                        fontSize = 12.sp
                    )
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = AcademyBlue
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Tests (${quizzes.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Leaderboard", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("History (${attemptHistory.size})", fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                0 -> {
                    // Available Tests List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(bottom = 70.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(quizzes) { quiz ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_card_${quiz.id}")
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(AcademyBlue, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = quiz.category.displayName,
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Timer,
                                                contentDescription = null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${quiz.durationMinutes} Mins",
                                                fontSize = 11.sp,
                                                color = Color.Gray,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = when (currentLanguage) {
                                            AppLanguage.PUNJABI -> quiz.titlePa
                                            else -> quiz.title
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = AcademyBlueDark
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "${quiz.questions.size} Questions • ${quiz.questions.size * quiz.marksPerQuestion} Marks • -${quiz.negativeMarks} Negative Marking",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { onStartQuiz(quiz) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(42.dp)
                                            .testTag("start_quiz_btn_${quiz.id}")
                                    ) {
                                        Text(
                                            text = Localization.getText("start_quiz", currentLanguage),
                                            color = AcademyBlueDark,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Leaderboard Screen
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(bottom = 70.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = AcademyBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = AcademyYellow,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "All-Punjab Academy Rankers",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Based on recent Mock Tests and Speed Quizzes",
                                            color = AcademyYellow,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }

                        items(leaderboard) { ranker ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (ranker.rank <= 3) Color(0xFFFFFDE7) else Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                when (ranker.rank) {
                                                    1 -> Color(0xFFFFD700)
                                                    2 -> Color(0xFFC0C0C0)
                                                    3 -> Color(0xFFCD7F32)
                                                    else -> Color(0xFFE0E0E0)
                                                },
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "#${ranker.rank}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = ranker.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = AcademyBlueDark
                                        )
                                        Text(
                                            text = "${ranker.city} • Accuracy ${ranker.accuracy}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${ranker.score} Pts",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = AcademyBlue
                                        )
                                        Text(
                                            text = ranker.badge,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Attempt History Screen
                    if (attemptHistory.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {
                            Text(
                                text = "No test attempts yet.\nTake a mock test to track your rank and performance!",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .padding(bottom = 70.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(attemptHistory) { attempt ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = attempt.quizTitle,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = AcademyBlueDark
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Score: ${attempt.score} / ${attempt.totalMarks} (${attempt.percentage.toInt()}%)",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (attempt.percentage >= 60) Color(0xFF2E7D32) else AcademyRed
                                            )
                                            Text(
                                                text = "Correct: ${attempt.correctAnswers} | Wrong: ${attempt.wrongAnswers}",
                                                fontSize = 11.sp,
                                                color = Color.DarkGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizTakeScreen(
    quiz: Quiz,
    userSession: UserSession,
    onFinishQuiz: (QuizAttemptResult) -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Screen security active during examination
    EnforceScreenSecurity(enabled = true)

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    var questionLang by remember { mutableStateOf(AppLanguage.ENGLISH) }
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }

    // Timer countdown in seconds
    var secondsRemaining by remember { mutableIntStateOf(quiz.durationMinutes * 60) }
    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }
        // Auto-submit when timer expires
        val attemptResult = calculateResult(quiz, userAnswers, (quiz.durationMinutes * 60) - secondsRemaining)
        onFinishQuiz(attemptResult)
    }

    val currentQuestion = quiz.questions.getOrElse(currentQuestionIndex) { quiz.questions.first() }

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Exam Header Bar
            Surface(
                color = AcademyBlueDark,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = onExit) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Exam",
                            tint = Color.White
                        )
                    }

                    // Timer Display
                    val minutes = secondsRemaining / 60
                    val seconds = secondsRemaining % 60
                    val isLowTime = secondsRemaining <= 120

                    Box(
                        modifier = Modifier
                            .background(
                                if (isLowTime) AcademyRed else Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (isLowTime) Color.White else AcademyYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Question Language Switcher Pill
                    Row(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        listOf(AppLanguage.ENGLISH to "EN", AppLanguage.PUNJABI to "ਪੰ", AppLanguage.HINDI to "हि").forEach { (lang, label) ->
                            Text(
                                text = label,
                                color = if (questionLang == lang) AcademyYellow else Color.White,
                                fontWeight = if (questionLang == lang) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .clickable { questionLang = lang }
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }

            // Question Palette Navigation
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quiz.questions.indices.forEach { index ->
                    val isCurrent = index == currentQuestionIndex
                    val isAnswered = userAnswers.containsKey(index)

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                when {
                                    isCurrent -> AcademyYellow
                                    isAnswered -> Color(0xFF388E3C)
                                    else -> Color(0xFFECEFF1)
                                },
                                CircleShape
                            )
                            .border(
                                width = if (isCurrent) 2.dp else 0.dp,
                                color = AcademyBlue,
                                shape = CircleShape
                            )
                            .clickable { currentQuestionIndex = index }
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = when {
                                isCurrent -> AcademyBlueDark
                                isAnswered -> Color.White
                                else -> Color.DarkGray
                            }
                        )
                    }
                }
            }

            // Main Question Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of ${quiz.questions.size}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AcademyBlue
                            )
                            Text(
                                text = "+${quiz.marksPerQuestion} | -${quiz.negativeMarks}",
                                fontSize = 11.sp,
                                color = AcademyRed,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Question Text in selected language
                        val questionPrompt = when (questionLang) {
                            AppLanguage.PUNJABI -> currentQuestion.questionPa
                            AppLanguage.HINDI -> currentQuestion.questionHi
                            else -> currentQuestion.questionEn
                        }

                        Text(
                            text = questionPrompt,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AcademyBlueDark,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4 Multiple Choice Options
                        val optionsList = when (questionLang) {
                            AppLanguage.PUNJABI -> currentQuestion.optionsPa
                            AppLanguage.HINDI -> currentQuestion.optionsHi
                            else -> currentQuestion.optionsEn
                        }

                        optionsList.forEachIndexed { optIndex, optText ->
                            val isSelected = userAnswers[currentQuestionIndex] == optIndex

                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFE8EEFA) else Color(0xFFFAFAFA)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) AcademyBlue else Color(0xFFE0E0E0)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable { userAnswers[currentQuestionIndex] = optIndex }
                                    .testTag("option_${currentQuestionIndex}_$optIndex")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(26.dp)
                                            .background(
                                                if (isSelected) AcademyBlue else Color(0xFFE0E0E0),
                                                CircleShape
                                            )
                                    ) {
                                        Text(
                                            text = listOf("A", "B", "C", "D").getOrElse(optIndex) { "${optIndex + 1}" },
                                            color = if (isSelected) Color.White else Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = optText,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = AcademyBlueDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation & Submit Bar
            Surface(
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        },
                        enabled = currentQuestionIndex > 0,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = { showSubmitConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AcademyRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("submit_exam_button")
                    ) {
                        Text("Submit Exam", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            if (currentQuestionIndex < quiz.questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                showSubmitConfirmDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AcademyBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (currentQuestionIndex == quiz.questions.size - 1) "Review" else "Next")
                    }
                }
            }
        }
    }

    if (showSubmitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            title = { Text("Submit Exam?") },
            text = {
                Text(
                    "You answered ${userAnswers.size} of ${quiz.questions.size} questions.\nDo you want to submit and view instant result & leaderboard rank?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        val timeTaken = (quiz.durationMinutes * 60) - secondsRemaining
                        val result = calculateResult(quiz, userAnswers, timeTaken)
                        onFinishQuiz(result)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow)
                ) {
                    Text("Yes, Submit", color = AcademyBlueDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun calculateResult(quiz: Quiz, answers: Map<Int, Int>, timeTakenSeconds: Int): QuizAttemptResult {
    var correct = 0
    var wrong = 0
    var skipped = 0

    quiz.questions.forEachIndexed { index, q ->
        val chosen = answers[index]
        when {
            chosen == null -> skipped++
            chosen == q.correctIndex -> correct++
            else -> wrong++
        }
    }

    val totalMarksPossible = quiz.questions.size * quiz.marksPerQuestion.toDouble()
    val earned = (correct * quiz.marksPerQuestion) - (wrong * quiz.negativeMarks)
    val finalScore = maxOf(0.0, earned)
    val percentage = if (totalMarksPossible > 0) (finalScore / totalMarksPossible) * 100 else 0.0

    return QuizAttemptResult(
        quizId = quiz.id,
        quizTitle = quiz.title,
        totalQuestions = quiz.questions.size,
        correctAnswers = correct,
        wrongAnswers = wrong,
        skippedAnswers = skipped,
        score = finalScore,
        totalMarks = totalMarksPossible,
        percentage = percentage,
        timeTakenSeconds = timeTakenSeconds,
        userAnswers = answers
    )
}

@Composable
fun QuizResultScreen(
    result: QuizAttemptResult,
    quiz: Quiz,
    onReviewSolutions: () -> Unit,
    onBackToHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .background(AcademyYellow, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = AcademyBlueDark,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "EXAM COMPLETED",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = AcademyBlue,
                letterSpacing = 1.sp
            )

            Text(
                text = result.quizTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = AcademyBlueDark,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Scorecard Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Your Final Score",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${String.format("%.1f", result.score)} / ${result.totalMarks.toInt()}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = if (result.percentage >= 60) Color(0xFF2E7D32) else AcademyRed
                    )

                    Text(
                        text = "${result.percentage.toInt()}% Percentage",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = AcademyBlue
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MetricItem("Correct", "${result.correctAnswers}", Color(0xFF2E7D32))
                        MetricItem("Wrong", "${result.wrongAnswers}", AcademyRed)
                        MetricItem("Skipped", "${result.skippedAnswers}", Color.Gray)
                        MetricItem("Time", "${result.timeTakenSeconds}s", AcademyBlue)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Review Solutions Button
            Button(
                onClick = onReviewSolutions,
                colors = ButtonDefaults.buttonColors(containerColor = AcademyBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("review_solutions_button")
            ) {
                Text("Review Question Solutions (All Languages)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBackToHub,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Back to Test Hub", fontWeight = FontWeight.Bold, color = AcademyBlueDark)
            }
        }
    }
}

@Composable
fun QuizSolutionReviewScreen(
    quiz: Quiz,
    attempt: QuizAttemptResult,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var reviewLang by remember { mutableStateOf(AppLanguage.PUNJABI) }

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                color = AcademyBlue,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Text(
                        text = "Solutions & Explanations",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    // Language Switcher
                    Row(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        listOf(AppLanguage.ENGLISH to "EN", AppLanguage.PUNJABI to "ਪੰ", AppLanguage.HINDI to "हि").forEach { (lang, label) ->
                            Text(
                                text = label,
                                color = if (reviewLang == lang) AcademyYellow else Color.White,
                                fontWeight = if (reviewLang == lang) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .clickable { reviewLang = lang }
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(quiz.questions.indices.toList()) { index ->
                    val q = quiz.questions[index]
                    val userChoice = attempt.userAnswers[index]
                    val isCorrect = userChoice == q.correctIndex

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Q${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AcademyBlue
                                )
                                Text(
                                    text = if (isCorrect) "✓ Correct (+2)" else if (userChoice == null) "Skipped" else "✗ Wrong (-0.5)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isCorrect) Color(0xFF2E7D32) else if (userChoice == null) Color.Gray else AcademyRed
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = when (reviewLang) {
                                    AppLanguage.PUNJABI -> q.questionPa
                                    AppLanguage.HINDI -> q.questionHi
                                    else -> q.questionEn
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AcademyBlueDark
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            val opts = when (reviewLang) {
                                AppLanguage.PUNJABI -> q.optionsPa
                                AppLanguage.HINDI -> q.optionsHi
                                else -> q.optionsEn
                            }

                            opts.forEachIndexed { optIdx, optText ->
                                val isRight = optIdx == q.correctIndex
                                val isUserWrong = userChoice == optIdx && !isRight

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .background(
                                            when {
                                                isRight -> Color(0xFFE8F5E9)
                                                isUserWrong -> Color(0xFFFFEBEE)
                                                else -> Color(0xFFF9F9F9)
                                            },
                                            RoundedCornerShape(6.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = when {
                                                isRight -> Color(0xFF388E3C)
                                                isUserWrong -> AcademyRed
                                                else -> Color(0xFFE0E0E0)
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${listOf("A", "B", "C", "D").getOrElse(optIdx) { "" }}. $optText" +
                                                (if (isRight) "  ✓ (Correct Answer)" else if (isUserWrong) "  ✗ (Your Choice)" else ""),
                                        fontSize = 12.sp,
                                        fontWeight = if (isRight || isUserWrong) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isRight -> Color(0xFF2E7D32)
                                            isUserWrong -> AcademyRed
                                            else -> Color.DarkGray
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Detailed Explanation
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFFDE7), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Explanation / ਵਿਆਖਿਆ:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color(0xFFF57F17)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = when (reviewLang) {
                                            AppLanguage.PUNJABI -> q.explanationPa
                                            AppLanguage.HINDI -> q.explanationHi
                                            else -> q.explanationEn
                                        },
                                        fontSize = 12.sp,
                                        color = Color(0xFF37474F),
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
        Text(text = label, fontSize = 11.sp, color = Color.Gray)
    }
}
