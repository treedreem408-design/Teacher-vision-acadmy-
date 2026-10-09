package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.StudyDocument
import com.example.model.UserSession
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyRed
import com.example.ui.theme.AcademyYellow
import com.example.util.DynamicWatermarkOverlay
import com.example.util.EnforceScreenSecurity

@Composable
fun PdfViewerScreen(
    document: StudyDocument,
    userSession: UserSession,
    currentLanguage: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Critical Security: Automatically engages FLAG_SECURE on Window
    EnforceScreenSecurity(enabled = true)

    var currentPageIndex by remember { mutableIntStateOf(0) }
    var isNightMode by remember { mutableStateOf(false) }
    var fontScale by remember { mutableFloatStateOf(1f) }

    val totalPages = document.pages.size
    val currentPage = document.pages.getOrElse(currentPageIndex) { document.pages.first() }
    val scrollState = rememberScrollState()

    val pageBgColor = if (isNightMode) Color(0xFF1E2024) else Color(0xFFFFFFFF)
    val pageTextColor = if (isNightMode) Color(0xFFE2E4E8) else Color(0xFF1F2937)

    Surface(
        color = if (isNightMode) Color(0xFF121417) else Color(0xFFEBEFF6),
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Action Bar
                Surface(
                    color = AcademyBlue,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
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
                                    text = when (currentLanguage) {
                                        AppLanguage.PUNJABI -> document.titlePa
                                        else -> document.title
                                    },
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Page ${currentPageIndex + 1} of $totalPages • In-App Document",
                                    color = AcademyYellow,
                                    fontSize = 11.sp
                                )
                            }

                            // Font size toggle
                            IconButton(
                                onClick = {
                                    fontScale = if (fontScale >= 1.3f) 1.0f else fontScale + 0.15f
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = "Font Scale",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Dark reading mode toggle
                            IconButton(
                                onClick = { isNightMode = !isNightMode },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Brightness4,
                                    contentDescription = "Toggle Night Mode",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Anti-Piracy Security Status Bar
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = AcademyYellow,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RESTRICTED DOCUMENT • SCREEN CAPTURE BLOCKED (FLAG_SECURE ACTIVE)",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Document Page Body Container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Document Page Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = pageBgColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("pdf_page_card")
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Page Content
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(20.dp)
                            ) {
                                // Academy Header on Page
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp)
                                ) {
                                    Text(
                                        text = "TEACHER VISION ACADEMY",
                                        color = AcademyBlue,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Helpline: 7888213000",
                                        color = AcademyRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = currentPage.heading,
                                    color = if (isNightMode) AcademyYellow else AcademyBlueDark,
                                    fontSize = (17 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = (22 * fontScale).sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = currentPage.content,
                                    color = pageTextColor,
                                    fontSize = (14 * fontScale).sp,
                                    lineHeight = (21 * fontScale).sp
                                )

                                if (currentPage.keyPoints.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(18.dp))
                                    Text(
                                        text = "Exam Key Takeaways:",
                                        color = if (isNightMode) AcademyYellow else AcademyBlue,
                                        fontSize = (13 * fontScale).sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    currentPage.keyPoints.forEach { point ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "• ",
                                                color = AcademyRed,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = (14 * fontScale).sp
                                            )
                                            Text(
                                                text = point,
                                                color = pageTextColor,
                                                fontSize = (13 * fontScale).sp,
                                                lineHeight = (18 * fontScale).sp
                                            )
                                        }
                                    }
                                }

                                if (currentPage.punjabiSummary.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isNightMode) Color(0xFF2C2A1E) else Color(0xFFFFFDE7)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "ਪੰਜਾਬੀ ਨੋਟਸ ਸਾਰ (Punjabi Summary):",
                                                color = Color(0xFFF57F17),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currentPage.punjabiSummary,
                                                color = pageTextColor,
                                                fontSize = (13 * fontScale).sp,
                                                lineHeight = (19 * fontScale).sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(40.dp))
                            }

                            // 2. Critical Security: Dynamic Diagonal Floating Watermark
                            DynamicWatermarkOverlay(
                                studentName = userSession.name,
                                studentMobile = userSession.mobile
                            )
                        }
                    }
                }

                // Bottom Page Navigation Bar
                Surface(
                    color = if (isNightMode) Color(0xFF1E2024) else Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        IconButton(
                            onClick = { if (currentPageIndex > 0) currentPageIndex-- },
                            enabled = currentPageIndex > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous Page",
                                tint = if (currentPageIndex > 0) AcademyBlue else Color.Gray
                            )
                        }

                        Text(
                            text = "Page ${currentPageIndex + 1} / $totalPages",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isNightMode) Color.White else AcademyBlueDark
                        )

                        IconButton(
                            onClick = { if (currentPageIndex < totalPages - 1) currentPageIndex++ },
                            enabled = currentPageIndex < totalPages - 1
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next Page",
                                tint = if (currentPageIndex < totalPages - 1) AcademyBlue else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
