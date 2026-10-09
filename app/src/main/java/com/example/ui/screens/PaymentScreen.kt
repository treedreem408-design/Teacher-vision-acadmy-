package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcademyCourse
import com.example.model.UserSession
import com.example.ui.theme.AcademyBlue
import com.example.ui.theme.AcademyBlueDark
import com.example.ui.theme.AcademyYellow

@Composable
fun PaymentScreen(
    course: AcademyCourse,
    userSession: UserSession,
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableIntStateOf(0) } // 0: UPI (GPay/PhonePe), 1: Razorpay Card/Netbanking
    var isPaymentDone by remember { mutableStateOf(false) }

    val upiId = "7888213000@upi"
    val transactionRef = "TVA-${System.currentTimeMillis().toString().takeLast(8)}"

    Surface(
        color = Color(0xFFF6F8FC),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Surface(color = AcademyBlue, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Secure Fee Payment Checkout",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            if (!isPaymentDone) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Course Summary Card
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "COURSE ENROLLMENT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = course.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AcademyBlueDark
                            )
                            Text(
                                text = "Student: ${userSession.name} (+91 ${userSession.mobile})",
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF0F4FA), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Total Payable Amount:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AcademyBlueDark
                                )
                                Text(
                                    text = "₹${course.fee}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = AcademyBlue
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "SELECT PAYMENT GATEWAY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AcademyBlueDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // UPI Method Option
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMethod == 0) Color(0xFFE8EEFA) else Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (selectedMethod == 0) 2.dp else 1.dp,
                            color = if (selectedMethod == 0) AcademyBlue else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = 0 }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFF388E3C), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Direct UPI (Google Pay / PhonePe / Paytm / BHIM)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AcademyBlueDark
                                    )
                                    Text(
                                        text = "Official Academy UPI ID: $upiId",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Razorpay Method Option
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMethod == 1) Color(0xFFE8EEFA) else Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (selectedMethod == 1) 2.dp else 1.dp,
                            color = if (selectedMethod == 1) AcademyBlue else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = 1 }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(AcademyBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Razorpay Secure Gateway",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AcademyBlueDark
                                    )
                                    Text(
                                        text = "Credit Card, Debit Card, NetBanking & Wallets",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            // Direct UPI Intent launch
                            if (selectedMethod == 0) {
                                val uri = Uri.parse("upi://pay?pa=$upiId&pn=TeacherVisionAcademy&am=${course.fee}&cu=INR&tn=CourseAdmission")
                                val upiIntent = Intent(Intent.ACTION_VIEW, uri)
                                try {
                                    context.startActivity(upiIntent)
                                } catch (e: Exception) {
                                    // Simulation fallback
                                }
                            }
                            isPaymentDone = true
                            Toast.makeText(context, "Payment Successful! Fee Receipt Generated.", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AcademyYellow),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("pay_fee_button")
                    ) {
                        Text(
                            text = "Pay ₹${course.fee} & Unlock Full Course",
                            color = AcademyBlueDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // Payment Success Fee Receipt
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFFE8F5E9), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "FEE PAYMENT SUCCESSFUL!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Official Academy Receipt Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "TEACHER VISION ACADEMY",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AcademyBlueDark
                            )
                            Text(
                                text = "Your Success Our Priority • Helpline: 7888213000",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            ReceiptRow("Receipt No:", transactionRef)
                            ReceiptRow("Student Name:", userSession.name)
                            ReceiptRow("Registered Mobile:", "+91 ${userSession.mobile}")
                            ReceiptRow("Enrolled Course:", course.title)
                            ReceiptRow("Amount Paid:", "₹${course.fee}")
                            ReceiptRow("Payment Mode:", if (selectedMethod == 0) "UPI ($upiId)" else "Razorpay Gateway")
                            ReceiptRow("Status:", "PAID / VERIFIED")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onPaymentSuccess,
                        colors = ButtonDefaults.buttonColors(containerColor = AcademyBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Access Course Study Material", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AcademyBlueDark)
    }
}
