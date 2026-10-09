package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcademyCourse
import com.example.model.AppLanguage
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyRed
import com.example.ui.theme.AcademyYellow

@Composable
fun CoursesScreen(
    courses: List<AcademyCourse>,
    currentLanguage: AppLanguage,
    onEnrollCourse: (AcademyCourse) -> Unit,
    onOpenStudyMaterial: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        text = "Teacher Vision Academy Courses",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "UGC NET • Punjab Master Cadre • PSTET • B.Ed • MA",
                        color = AcademyYellow,
                        fontSize = 12.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 70.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(courses) { course ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("course_card_${course.id}")
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
                                        text = course.category.displayName,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (course.isEnrolled) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "✓ Enrolled",
                                            color = Color(0xFF2E7D32),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = course.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = AcademyBlueDark
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = course.description,
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Faculty: ${course.instructor}",
                                fontSize = 11.sp,
                                color = AcademyBlue,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Features metrics row
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF7F9FC), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                FeatureBadge(Icons.Default.LiveTv, "${course.totalLectures} Lectures")
                                FeatureBadge(Icons.Default.Description, "${course.totalNotes} PDF Notes")
                                FeatureBadge(Icons.Default.Quiz, "${course.totalTests} Tests")
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₹${course.fee}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AcademyBlueDark
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "₹${course.originalFee}",
                                        fontSize = 13.sp,
                                        color = Color.Gray,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }

                                if (course.isEnrolled) {
                                    Button(
                                        onClick = onOpenStudyMaterial,
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademyBlue),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Go to Class", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = { onEnrollCourse(course) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("enroll_btn_${course.id}")
                                    ) {
                                        Text("Enroll Now", color = AcademyBlueDark, fontWeight = FontWeight.Bold)
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

@Composable
fun FeatureBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = AcademyBlue, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    }
}
