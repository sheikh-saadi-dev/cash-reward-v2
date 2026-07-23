package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

const val SAMPLE_APPS_SCRIPT_CODE = """function doPost(e) {
  try {
    var sheet = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
    var data = JSON.parse(e.postData.contents);
    
    // Auto add headers if sheet is empty
    if (sheet.getLastRow() === 0) {
      sheet.appendRow([
        "Request ID", "User ID", "User Name", "Verification Status", 
        "Method", "Account Number", "Points Used", "Amount (BDT)", "Date", "Status"
      ]);
    }
    
    sheet.appendRow([
      data.requestId,
      data.userId,
      data.userName,
      data.verificationStatus,
      data.method,
      data.accountNumber,
      data.pointsUsed,
      data.amountBdt,
      data.date,
      data.status
    ]);
    
    return ContentService.createTextOutput(JSON.stringify({ "status": "success" }))
      .setMimeType(ContentService.MimeType.JSON);
  } catch(err) {
    return ContentService.createTextOutput(JSON.stringify({ "status": "error", "message": err.toString() }))
      .setMimeType(ContentService.MimeType.JSON);
  }
}"""

@Composable
fun GoogleSheetsGuideModal(
    currentWebhookUrl: String,
    onSaveWebhookUrl: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputUrl by remember { mutableStateOf(currentWebhookUrl) }
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp)
                .border(1.dp, Color(0xFF472778), RoundedCornerShape(20.dp))
                .testTag("google_sheets_guide_modal"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Google Sheets Webhook Setup",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Google Apps Script Web App URL:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = { inputUrl = it },
                    placeholder = { Text("https://script.google.com/macros/s/.../exec", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("webhook_url_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFC107),
                        unfocusedBorderColor = Color(0xFF472778),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF140A26),
                        unfocusedContainerColor = Color(0xFF140A26)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onSaveWebhookUrl(inputUrl)
                        Toast.makeText(context, "Webhook URL সংরক্ষণ করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107), contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text("Save URL", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Google Apps Script Code (Code.gs):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF140A26))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Code.gs",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Apps Script Code", SAMPLE_APPS_SCRIPT_CODE)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "কোড কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("copy_script_code_button")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFFFFC107))
                            }
                        }
                        Text(
                            text = SAMPLE_APPS_SCRIPT_CODE,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF81C784)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "সেটআপ করার সহজ ধাপ:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                val steps = listOf(
                    "১. Google Sheets খুলে Extensions > Apps Script-এ যান।",
                    "২. উপরের Code.gs সম্পূর্ণ পেস্ট করুন এবং Save করুন।",
                    "৩. Deploy > New deployment সিলেক্ট করুন।",
                    "৪. Select type: Web app বেছে নিন।",
                    "৫. Execute as: 'Me', Who has access: 'Anyone' সেট করে Deploy দিন।",
                    "৬. প্রাপ্ত Web App URL কপি করে এই অ্যাপে পেস্ট করে Save করুন।"
                )

                steps.forEach { step ->
                    Text(
                        text = step,
                        fontSize = 12.sp,
                        color = Color(0xFFD1C4E9),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF321958), contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("বন্ধ করুন")
                }
            }
        }
    }
}
