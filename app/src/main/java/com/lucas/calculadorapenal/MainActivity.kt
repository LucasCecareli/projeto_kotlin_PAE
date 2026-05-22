package com.lucas.calculadorapenal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucas.calculadorapenal.ui.theme.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculadoraPenalTheme {
                TelaCalculadoraPenal()
            }
        }
    }
}

@Composable
fun TelaCalculadoraPenal() {

    var anos by remember { mutableStateOf("") }
    var meses by remember { mutableStateOf("") }
    var dias by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Calculadora Penal",
            color = Gold,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Execução Penal • Lei 13.964/19",
            color = TextGray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = CardBlue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Duração da Pena",
                    color = Gold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = anos,
                    onValueChange = { anos = it },
                    label = {
                        Text("Anos")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = meses,
                    onValueChange = { meses = it },
                    label = {
                        Text("Meses")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = dias,
                    onValueChange = { dias = it },
                    label = {
                        Text("Dias")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}