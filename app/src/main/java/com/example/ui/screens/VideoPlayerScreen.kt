package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LiveClassItem
import com.example.model.UserSession
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyRed
import com.example.ui.theme.AcademyYellow
import com.example.util.DynamicWatermarkOverlay
import com.example.util.EnforceScreenSecurity

@Composable
fun VideoPlayerScreen(
    liveClass: LiveClassItem,
    userSession: UserSession,
    currentLanguage: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Anti-Piracy FLAG_SECURE enforcement
    EnforceScreenSecurity(enabled = true)

    var isPlaying by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Chat, 1: Class Notes

    // Live chat messages
    val chatMessages = remember {
        mutableStateListOf(
            "Kiranjeet: Sat Sri Akal Sir!",
            "Harman: Today's topic is very important for UGC NET",
            "Deepika: Sir can we get PDF after class?",
            "Teacher Vision: Yes Deepika, PDF is available in Study Material section!"
        )
    }
    var chatInput by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Video Player Top Bar
            Surface(color = AcademyBlue, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = liveClass.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = if (liveClass.isLiveNow) "🔴 Live Stream" else "Recorded Lecture",
                            color = AcademyYellow,
                            fontSize = 11.sp
                        )
                    }

                    // Open external app if desired (YouTube / Zoom)
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(liveClass.streamUrl))
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open App",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Video Player Frame with Anti-Piracy Watermark
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                // Video Screen Emulation
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .background(AcademyYellow, CircleShape)
                                .clickable { isPlaying = !isPlaying }
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = AcademyBlueDark,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = liveClass.subject,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Teacher: ${liveClass.educator} • Speed: ${playbackSpeed}x",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }

                    // Security Indicator Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = AcademyRed,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SCREEN SECURE",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Speed controls at bottom right
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        listOf(1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .background(
                                        if (playbackSpeed == speed) AcademyYellow else Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .clickable { playbackSpeed = speed }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${speed}x",
                                    color = if (playbackSpeed == speed) AcademyBlueDark else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Dynamic Anti-Piracy Watermark across Video Frame
                DynamicWatermarkOverlay(
                    studentName = userSession.name,
                    studentMobile = userSession.mobile
                )
            }

            // Lecture Meta
            Card(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = liveClass.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AcademyBlueDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${liveClass.educator} • ${liveClass.scheduledTime}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "${liveClass.viewerCount} live attendees",
                            fontSize = 11.sp,
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Tabs: Live Chat vs Lesson Notes
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = AcademyBlue
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Live Chat (${chatMessages.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Class Notes & Highlights", fontWeight = FontWeight.Bold) }
                )
            }

            // Tab Content
            if (selectedTab == 0) {
                // Live Chat
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages) { msg ->
                            val parts = msg.split(":", limit = 2)
                            val sender = parts.getOrNull(0) ?: "Student"
                            val content = parts.getOrNull(1) ?: ""

                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (sender.contains("Teacher")) Color(0xFFE8EEFA) else Color.White
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "$sender: ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (sender.contains("Teacher")) AcademyRed else AcademyBlue
                                    )
                                    Text(
                                        text = content,
                                        fontSize = 12.sp,
                                        color = Color(0xFF263238)
                                    )
                                }
                            }
                        }
                    }

                    // Message input
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("Ask doubt during live lecture...", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    chatMessages.add("${userSession.name}: $chatInput")
                                    chatInput = ""
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .background(AcademyBlue, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            } else {
                // Lesson Notes Tab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Class Highlights & Important Topics:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AcademyBlueDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "1. Bloom's Revised Taxonomy (Creating vs Evaluating)\n2. Formative vs Summative Evaluation principles\n3. SWAYAM & SWAYAM PRABHA 40 DTH channels\n4. Gurmat Kavya Dhara & Asaa Di Vaar 24 Pauris\n5. Baba Farid Ji - First authentic Punjabi Sufi poet",
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
