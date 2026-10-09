package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CourseCategory
import com.example.model.LiveBannerUpdate
import com.example.model.LiveClassItem
import com.example.model.StudyDocument
import com.example.model.UserSession
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyBlueLight
import com.example.ui.theme.AcademyRed
import com.example.ui.theme.AcademyYellow
import com.example.util.Localization
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    userSession: UserSession,
    currentLanguage: AppLanguage,
    liveUpdates: List<LiveBannerUpdate>,
    liveClasses: List<LiveClassItem>,
    recentDocuments: List<StudyDocument>,
    onNavigateCourses: () -> Unit,
    onNavigateLiveClasses: () -> Unit,
    onNavigateTestSeries: () -> Unit,
    onNavigateStudyMaterial: () -> Unit,
    onOpenDocument: (StudyDocument) -> Unit,
    onOpenLiveClass: (LiveClassItem) -> Unit,
    onOpenAdminPanel: () -> Unit,
    onCourseCategoryChange: (CourseCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Auto-advancing banner slider state
    var activeBannerIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(liveUpdates.size) {
        if (liveUpdates.isNotEmpty()) {
            while (true) {
                delay(4000)
                activeBannerIndex = (activeBannerIndex + 1) % liveUpdates.size
            }
        }
    }

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            // Course Filter Quick Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CourseCategory.values()) { category ->
                    val isSelected = userSession.selectedCourse == category
                    val name = when (currentLanguage) {
                        AppLanguage.ENGLISH -> category.displayName
                        AppLanguage.PUNJABI -> category.displayNamePa
                        AppLanguage.HINDI -> category.displayNameHi
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) AcademyBlue else Color(0xFFF0F4FA),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) AcademyYellow else Color(0xFFD3DBE8),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onCourseCategoryChange(category) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = name,
                            color = if (isSelected) Color.White else Color(0xFF2E384D),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1. TOP BANNER SLIDER FOR LIVE RECENT UPDATES
            if (liveUpdates.isNotEmpty()) {
                val currentBanner = liveUpdates.getOrElse(activeBannerIndex) { liveUpdates.first() }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("live_updates_banner_slider")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(AcademyBlueDark, AcademyBlue, AcademyBlueLight)
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(AcademyRed, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = currentBanner.tag,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                if (currentBanner.isLiveNow) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(AcademyYellow, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "LIVE NOW",
                                            color = AcademyYellow,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = currentBanner.date,
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 10.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.ENGLISH -> currentBanner.title
                                    AppLanguage.PUNJABI -> currentBanner.titlePa
                                    AppLanguage.HINDI -> currentBanner.titleHi
                                },
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = currentBanner.subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Slider dots indicator
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    liveUpdates.indices.forEach { index ->
                                        Box(
                                            modifier = Modifier
                                                .size(if (index == activeBannerIndex) 14.dp else 6.dp, 6.dp)
                                                .background(
                                                    if (index == activeBannerIndex) AcademyYellow else Color.White.copy(alpha = 0.4f),
                                                    RoundedCornerShape(3.dp)
                                                )
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (currentBanner.linkUrl.isNotBlank()) {
                                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentBanner.linkUrl))
                                            context.startActivity(browserIntent)
                                        } else {
                                            onNavigateCourses()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = "View Details",
                                        color = AcademyBlueDark,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. CATEGORIES GRID
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "ACADEMY DASHBOARD",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AcademyBlueDark,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoryItemCard(
                        title = Localization.getText("cat_my_courses", currentLanguage),
                        icon = Icons.Default.School,
                        color = AcademyBlue,
                        badge = "120+ Hrs",
                        onClick = onNavigateCourses,
                        modifier = Modifier.weight(1f)
                    )
                    CategoryItemCard(
                        title = Localization.getText("cat_live_classes", currentLanguage),
                        icon = Icons.Default.LiveTv,
                        color = AcademyRed,
                        badge = "Daily 7PM",
                        onClick = onNavigateLiveClasses,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoryItemCard(
                        title = Localization.getText("cat_tests", currentLanguage),
                        icon = Icons.Default.Quiz,
                        color = Color(0xFFE65100),
                        badge = "Rank Test",
                        onClick = onNavigateTestSeries,
                        modifier = Modifier.weight(1f)
                    )
                    CategoryItemCard(
                        title = Localization.getText("cat_notes", currentLanguage),
                        icon = Icons.Default.Description,
                        color = Color(0xFF2E7D32),
                        badge = "Secure PDF",
                        onClick = onNavigateStudyMaterial,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (userSession.isAdmin) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenAdminPanel() }
                            .testTag("home_admin_panel_card")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(AcademyRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Admin Panel (7888213000)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AcademyBlueDark
                                )
                                Text(
                                    text = "Upload PDF notes, post Live updates & view registered students",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = AcademyBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. LIVE & UPCOMING CLASSES SECTION
            val liveClass = liveClasses.firstOrNull { it.isLiveNow } ?: liveClasses.firstOrNull()
            if (liveClass != null) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = Localization.getText("upcoming_classes", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AcademyBlueDark
                        )
                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            color = AcademyBlue,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateLiveClasses() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLiveClass(liveClass) }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (liveClass.isLiveNow) AcademyRed else AcademyBlue,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = if (liveClass.isLiveNow) "🔴 LIVE NOW" else "SCHEDULED",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = liveClass.streamType,
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = "${liveClass.viewerCount} students watching",
                                    fontSize = 10.sp,
                                    color = Color(0xFF388E3C),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = liveClass.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AcademyBlueDark
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "By ${liveClass.educator} • ${liveClass.scheduledTime}",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { onOpenLiveClass(liveClass) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (liveClass.isLiveNow) AcademyRed else AcademyBlue
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (liveClass.isLiveNow) "Join Live Lecture Now" else "Watch Lecture",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. RECENT STUDY MATERIAL & PDF NOTES
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "UNIT NOTES & PDFS (ANTI-PIRACY)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AcademyBlueDark
                    )
                    Text(
                        text = "Browse Folders",
                        fontSize = 12.sp,
                        color = AcademyBlue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateStudyMaterial() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                recentDocuments.take(3).forEach { doc ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onOpenDocument(doc) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFE8EEFA), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = AcademyBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.PUNJABI -> doc.titlePa
                                        else -> doc.title
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AcademyBlueDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${doc.unitName} • ${doc.pagesCount} Pages • In-App Secure Viewer",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF8E1), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Read",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AcademyBlue
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. HELPLINE & ADMISSION SUPPORT CARD
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AcademyYellow),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Need Admission Guidance? Contact Director",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AcademyBlueDark
                    )
                    Text(
                        text = "Call or WhatsApp: 7888213000 for UGC NET, B.Ed & Punjab Govt Exams batches.",
                        fontSize = 12.sp,
                        color = Color(0xFF263238),
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:7888213000"))
                                context.startActivity(dialIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AcademyBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call 7888213000", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val waIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/917888213000?text=Hello%20Teacher%20Vision%20Academy%2C%20I%20want%20details%20about%20your%20courses.")
                                )
                                context.startActivity(waIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItemCard(
    title: String,
    icon: ImageVector,
    color: Color,
    badge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(color.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(color, RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AcademyBlueDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
