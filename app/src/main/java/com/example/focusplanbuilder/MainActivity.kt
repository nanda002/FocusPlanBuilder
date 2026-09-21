package com.example.focusplanbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int
)

fun durationCategory(minutes: Int): String {
    return when {
        minutes < 10 -> "Invalid"
        minutes in 10..29 -> "Quick review"
        minutes in 30..60 -> "Focused session"
        else -> "Extended session"
    }
}

fun recommendedBreak(minutes: Int): Int {
    return when {
        minutes in 10..29 -> 5
        minutes in 30..60 -> 10
        else -> 15
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                FocusPlanRoute()
            }
        }
    }
}

@Composable
fun FocusPlanRoute(
    modifier: Modifier = Modifier
) {
    var subject by rememberSaveable {
        mutableStateOf("")
    }

    var minutesText by rememberSaveable {
        mutableStateOf("")
    }

    var plan by rememberSaveable {
        mutableStateOf<FocusPlan?>(null)
    }

    val minutes: Int? = minutesText.toIntOrNull()

    val canCreatePlan =
        subject.isNotBlank() &&
                minutes != null &&
                minutes in 10..180

    FocusPlanScreen(
        subject = subject,
        minutesText = minutesText,
        plan = plan,
        onSubjectChange = {
            subject = it
            plan = null
        },
        onMinutesChange = {
            minutesText = it
            plan = null
        },
        canCreatePlan = canCreatePlan,
        onCreatePlan = {
            if (canCreatePlan && minutes != null) {
                plan = FocusPlan(
                    subject = subject.trim(),
                    minutes = minutes,
                    category = durationCategory(minutes),
                    breakMinutes = recommendedBreak(minutes)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun FocusPlanScreen(
    subject: String,
    minutesText: String,
    plan: FocusPlan?,
    onSubjectChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    canCreatePlan: Boolean,
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Focus Plan Builder",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter a subject and the amount of time you have available to create a focused study plan."
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = onSubjectChange,
                label = {
                    Text("Study subject")
                },
                placeholder = {
                    Text("e.g. Kotlin, Databases, Compose state")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = minutesText,
                onValueChange = onMinutesChange,
                label = {
                    Text("Available minutes")
                },
                placeholder = {
                    Text("10–180")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCreatePlan,
                enabled = canCreatePlan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create plan")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (plan != null) {
                FocusPlanCard(plan)
            }
        }
    }
}

@Composable
fun FocusPlanCard(plan: FocusPlan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = plan.subject,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(
                    text = "Duration: ",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "${plan.minutes} minutes"
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text(
                    text = "Category: ",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = plan.category
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text(
                    text = "Recommended break: ",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "${plan.breakMinutes} minutes"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Study ${plan.subject} for ${plan.minutes} minutes, and then take a ${plan.breakMinutes}-minute break.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}