package com.prashi.vox.travel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.prashi.vox.ai.GroqManager

@Composable
fun TravelScreen() {

    var from by remember { mutableStateOf("") }
    var to by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var travellers by remember { mutableStateOf("1") }
    var date by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Any") }

    var aiResult by remember {
        mutableStateOf("Your AI generated itinerary will appear here.")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val groq = remember { GroqManager() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B18))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "✈ AI Travel",
            color = Color(0xFF00E5FF),
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "Plan your trip with AI",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = from,
            onValueChange = { from = it },
            label = { Text("From") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = to,
            onValueChange = { to = it },
            label = { Text("To") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = travellers,
            onValueChange = { travellers = it },
            label = { Text("Travellers") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = budget,
            onValueChange = { budget = it },
            label = { Text("Budget (₹)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(

            onClick = {

                loading = true

                val prompt = """
Plan a trip.

From : $from

To : $to

Budget : ₹$budget

Travellers : $travellers

Travel Mode : $mode

Departure Date : $date

Suggest:

Best transport

Hotels

Food

Weather

Places to visit

2 day itinerary
""".trimIndent()

                groq.askAI(

                    prompt,

                    onSuccess = {

                        loading = false

                        aiResult = it

                    },

                    onError = {

                        loading = false

                        aiResult = it

                    }

                )

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {

            Text("✨ PLAN MY TRIP")

        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF101B2D)
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    "AI Suggestions",
                    color = Color(0xFF00E5FF),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (loading)
                        "Planning your trip..."
                    else
                        aiResult,
                    color = Color.White
                )

            }

        }

    }

}