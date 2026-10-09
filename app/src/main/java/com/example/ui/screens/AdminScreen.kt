package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AcademyRepository
import com.example.model.AppLanguage
import com.example.model.CourseCategory
import com.example.model.Quiz
import com.example.model.QuizQuestion
import com.example.model.StudentEnrollment
import com.example.model.StudyFolder
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueContainer
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyRed
import com.example.ui.theme.AcademyYellow

@Composable
fun AdminScreen(
    students: List<StudentEnrollment>,
    folders: List<StudyFolder>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Post Live Update, 1: Upload PDF Note, 2: Create Quiz, 3: Student Roster

    // 1. Post Live Update states
    var updateTitle by remember { mutableStateOf("") }
    var updateTitlePa by remember { mutableStateOf("") }
    var updateSubtitle by remember { mutableStateOf("") }
    var updateTag by remember { mutableStateOf("EXAM ALERT") }

    // 2. Upload PDF Note states
    var noteTitle by remember { mutableStateOf("") }
    var noteTitlePa by remember { mutableStateOf("") }
    var noteCourse by remember { mutableStateOf(CourseCategory.UGC_NET) }
    var noteContent by remember { mutableStateOf("") }

    // 3. Create Quiz states
    var quizTitle by remember { mutableStateOf("") }
    var questionPrompt by remember { mutableStateOf("") }
    var questionPromptPa by remember { mutableStateOf("") }
    var optA by remember { mutableStateOf("") }
    var optB by remember { mutableStateOf("") }
    var optC by remember { mutableStateOf("") }
    var optD by remember { mutableStateOf("") }
    var correctIndex by remember { mutableIntStateOf(0) }
    var explanation by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Admin Top Header
            Surface(color = AcademyBlueDark, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .background(AcademyRed, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Admin Panel (Owner 7888213000)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Manage Live Updates, Study Material, Tests & Students",
                            color = AcademyYellow,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Admin Function Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = AcademyBlue,
                edgePadding = 12.dp
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("1. Post Live Update", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("2. Upload PDF Note", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("3. Create Quiz", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("4. Students (${students.size})", fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                0 -> {
                    // TAB 1: Post Live Recent Updates (Instantly appears on Home Screen banner slider)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Publish Instant Live Banner Update",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = AcademyBlueDark
                                    )
                                    Text(
                                        text = "Updates show immediately in the Home Screen top banner slider.",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    OutlinedTextField(
                                        value = updateTitle,
                                        onValueChange = { updateTitle = it },
                                        label = { Text("Update Title (English)") },
                                        placeholder = { Text("e.g. UGC NET 2026 Admit Card Released") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("admin_update_title_input")
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = updateTitlePa,
                                        onValueChange = { updateTitlePa = it },
                                        label = { Text("Update Title in Punjabi (ਗੁਰਮੁਖੀ)") },
                                        placeholder = { Text("e.g. ਯੂ.ਜੀ.ਸੀ. ਨੈੱਟ 2026 ਐਡਮਿਟ ਕਾਰਡ ਜਾਰੀ") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("admin_update_title_pa_input")
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = updateSubtitle,
                                        onValueChange = { updateSubtitle = it },
                                        label = { Text("Detailed Description / Subtitle") },
                                        placeholder = { Text("e.g. Download hall ticket from official NTA website now.") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = updateTag,
                                        onValueChange = { updateTag = it },
                                        label = { Text("Tag Badge (e.g. EXAM ALERT, NEW BATCH)") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            if (updateTitle.isNotBlank()) {
                                                AcademyRepository.addLiveUpdate(
                                                    title = updateTitle,
                                                    titlePa = updateTitlePa,
                                                    subtitle = updateSubtitle.ifBlank { "Important notice from Teacher Vision Academy" },
                                                    tag = updateTag.ifBlank { "ALERT" }
                                                )
                                                Toast.makeText(context, "Update posted to Home Screen!", Toast.LENGTH_SHORT).show()
                                                updateTitle = ""
                                                updateTitlePa = ""
                                                updateSubtitle = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("publish_update_button")
                                    ) {
                                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = AcademyBlueDark)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Publish to App Home Screen", color = AcademyBlueDark, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // TAB 2: Upload PDF Notes to Folder
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Add Protected PDF Notes to Folders",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = AcademyBlueDark
                                    )
                                    Text(
                                        text = "Documents will be anti-piracy protected with FLAG_SECURE and watermarks.",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    OutlinedTextField(
                                        value = noteTitle,
                                        onValueChange = { noteTitle = it },
                                        label = { Text("Note Title") },
                                        placeholder = { Text("e.g. Unit 4 Communication Short Notes") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("admin_note_title_input")
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = noteTitlePa,
                                        onValueChange = { noteTitlePa = it },
                                        label = { Text("Punjabi Title (Optional)") },
                                        placeholder = { Text("ਪੰਜਾਬੀ ਸਿਰਲੇਖ") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Target Course
                                    Text(text = "Target Exam:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    ) {
                                        listOf(CourseCategory.UGC_NET, CourseCategory.PUNJAB_GOVT, CourseCategory.MA).forEach { cat ->
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        if (noteCourse == cat) AcademyBlue else Color(0xFFEEEEEE),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                            ) {
                                                Text(
                                                    text = cat.displayName,
                                                    color = if (noteCourse == cat) Color.White else Color.Black,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = noteContent,
                                        onValueChange = { noteContent = it },
                                        label = { Text("Study Material Text / PDF Content Pages") },
                                        placeholder = { Text("Enter detailed notes and formulas...") },
                                        minLines = 4,
                                        modifier = Modifier.fillMaxWidth().testTag("admin_note_content_input")
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                                                AcademyRepository.addStudyDocument(
                                                    title = noteTitle,
                                                    titlePa = noteTitlePa,
                                                    folderId = "f_ugc_p1_u1",
                                                    category = noteCourse,
                                                    content = noteContent
                                                )
                                                Toast.makeText(context, "Study document added successfully!", Toast.LENGTH_SHORT).show()
                                                noteTitle = ""
                                                noteTitlePa = ""
                                                noteContent = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademyBlue),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth().height(48.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Description, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Save to Study Material Folder", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // TAB 3: Create Quiz
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Create Interactive Quiz Question",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = AcademyBlueDark
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = quizTitle,
                                        onValueChange = { quizTitle = it },
                                        label = { Text("Quiz Title") },
                                        placeholder = { Text("e.g. Higher Education Special Quiz") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = questionPrompt,
                                        onValueChange = { questionPrompt = it },
                                        label = { Text("Question (English)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = questionPromptPa,
                                        onValueChange = { questionPromptPa = it },
                                        label = { Text("Question in Punjabi (ਪੰਜਾਬੀ ਪ੍ਰਸ਼ਨ)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(value = optA, onValueChange = { optA = it }, label = { Text("Option A") }, modifier = Modifier.fillMaxWidth())
                                    OutlinedTextField(value = optB, onValueChange = { optB = it }, label = { Text("Option B") }, modifier = Modifier.fillMaxWidth())
                                    OutlinedTextField(value = optC, onValueChange = { optC = it }, label = { Text("Option C") }, modifier = Modifier.fillMaxWidth())
                                    OutlinedTextField(value = optD, onValueChange = { optD = it }, label = { Text("Option D") }, modifier = Modifier.fillMaxWidth())

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = explanation,
                                        onValueChange = { explanation = it },
                                        label = { Text("Explanation (Solution)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            if (questionPrompt.isNotBlank() && optA.isNotBlank()) {
                                                val newQuiz = Quiz(
                                                    id = "quiz_${System.currentTimeMillis()}",
                                                    title = quizTitle.ifBlank { "Academy Practice Quiz" },
                                                    titlePa = quizTitle.ifBlank { "ਅਭਿਆਸ ਕੁਇਜ਼" },
                                                    category = CourseCategory.UGC_NET,
                                                    durationMinutes = 10,
                                                    totalQuestions = 1,
                                                    questions = listOf(
                                                        QuizQuestion(
                                                            id = "q_admin_1",
                                                            questionEn = questionPrompt,
                                                            questionPa = questionPromptPa.ifBlank { questionPrompt },
                                                            questionHi = questionPrompt,
                                                            optionsEn = listOf(optA, optB, optC, optD),
                                                            optionsPa = listOf(optA, optB, optC, optD),
                                                            optionsHi = listOf(optA, optB, optC, optD),
                                                            correctIndex = correctIndex,
                                                            explanationEn = explanation,
                                                            explanationPa = explanation,
                                                            explanationHi = explanation
                                                        )
                                                    )
                                                )
                                                AcademyRepository.createQuiz(newQuiz)
                                                Toast.makeText(context, "Quiz created and added to Test Hub!", Toast.LENGTH_SHORT).show()
                                                questionPrompt = ""
                                                optA = ""
                                                optB = ""
                                                optC = ""
                                                optD = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth().height(48.dp)
                                    ) {
                                        Text("Save & Publish Quiz", color = AcademyBlueDark, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // TAB 4: Students List Who Joined
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "ENROLLED STUDENTS DIRECTORY",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(students) { student ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(AcademyBlueContainer, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Group,
                                            contentDescription = null,
                                            tint = AcademyBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = student.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = AcademyBlueDark
                                        )
                                        Text(
                                            text = "Mob: +91 ${student.mobile} • ${student.course.displayName}",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "Enrolled on: ${student.joinDate}",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Active",
                                            color = Color(0xFF2E7D32),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
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
