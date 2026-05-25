package com.lucas.calculadorapenal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucas.calculadorapenal.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp

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

    var dataInicio by remember { mutableStateOf("") }
    var tipoCrime by remember { mutableStateOf("Comum") }
    var statusApenado by remember { mutableStateOf("Primário") }

    var diasTrabalhados by remember { mutableStateOf("") }
    var horasEstudo by remember { mutableStateOf("") }

    var resultadoVisivel by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_branca),
            contentDescription = "Logo Cespedes Lourenço",
            modifier = Modifier
                .width(220.dp)
                .height(90.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        AssistChip(
            onClick = {},
            label = {
                Text(
                    text = "Lei nº 13.964/2019",
                    color = Gold,
                    fontSize = 12.sp
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = CardBlue
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Calculadora de",
            color = TextWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Execução Penal",
            color = Gold,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ferramenta educativa para estimativa de prazos da execução penal",
            color = TextGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        CardEntradaPena(
            anos = anos,
            meses = meses,
            dias = dias,
            onAnosChange = { anos = it },
            onMesesChange = { meses = it },
            onDiasChange = { dias = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CardDadosJuridicos(
            dataInicio = dataInicio,
            onDataInicioChange = { dataInicio = it },
            tipoCrime = tipoCrime,
            onTipoCrimeChange = { tipoCrime = it },
            statusApenado = statusApenado,
            onStatusChange = { statusApenado = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CardRemicaoPena(
            diasTrabalhados = diasTrabalhados,
            onDiasTrabalhadosChange = { diasTrabalhados = it },
            horasEstudo = horasEstudo,
            onHorasEstudoChange = { horasEstudo = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                carregando = true

                resultadoVisivel = true

                carregando = false
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Gold,
                contentColor = TextWhite
            ),

            shape = RoundedCornerShape(18.dp),

            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp
            ),

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)

        ) {

            if (carregando) {

                CircularProgressIndicator(
                    color = TextWhite,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )

            } else {

                Text(
                    text = "Calcular Progressões",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (resultadoVisivel) {
            Spacer(modifier = Modifier.height(18.dp))
            CardResultado()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun CardEntradaPena(
    anos: String,
    meses: String,
    dias: String,
    onAnosChange: (String) -> Unit,
    onMesesChange: (String) -> Unit,
    onDiasChange: (String) -> Unit
) {
    CardBase {
        TituloCard(
            titulo = "Duração da Pena",
            subtitulo = "Tempo total estabelecido na sentença"
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CampoTextoPenal(
                valor = anos,
                aoAlterar = onAnosChange,
                label = "Anos",
                modifier = Modifier.weight(1f)
            )

            CampoTextoPenal(
                valor = meses,
                aoAlterar = onMesesChange,
                label = "Meses",
                modifier = Modifier.weight(1f)
            )

            CampoTextoPenal(
                valor = dias,
                aoAlterar = onDiasChange,
                label = "Dias",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CardDadosJuridicos(
    dataInicio: String,
    onDataInicioChange: (String) -> Unit,
    tipoCrime: String,
    onTipoCrimeChange: (String) -> Unit,
    statusApenado: String,
    onStatusChange: (String) -> Unit
) {
    CardBase {
        TituloCard(
            titulo = "Características do Crime",
            subtitulo = "Regime e tipo de delito"
        )

        Spacer(modifier = Modifier.height(18.dp))

        CampoTextoPenal(
            valor = dataInicio,
            aoAlterar = {

                val numeros = it
                    .filter { char -> char.isDigit() }
                    .take(8)

                val dataFormatada = buildString {

                    for (i in numeros.indices) {

                        append(numeros[i])

                        if ((i == 1 || i == 3) && i != numeros.lastIndex) {
                            append("/")
                        }
                    }
                }

                onDataInicioChange(dataFormatada)
            },
            label = "Data de início da pena"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tipo do Crime", color = TextGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LinhaOpcoes(
            opcaoSelecionada = tipoCrime,
            opcoes = listOf("Comum", "Hediondo"),
            aoSelecionar = onTipoCrimeChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Status do Apenado", color = TextGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LinhaOpcoes(
            opcaoSelecionada = statusApenado,
            opcoes = listOf("Primário", "Reincidente"),
            aoSelecionar = onStatusChange
        )
    }
}

@Composable
fun CardRemicaoPena(
    diasTrabalhados: String,
    onDiasTrabalhadosChange: (String) -> Unit,
    horasEstudo: String,
    onHorasEstudoChange: (String) -> Unit
) {
    CardBase {
        TituloCard(
            titulo = "Remição de Pena",
            subtitulo = "Desconto por trabalho e estudo"
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Dias Trabalhados",
            color = TextGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CampoTextoPenal(
            valor = diasTrabalhados,
            aoAlterar = onDiasTrabalhadosChange,
            label = "Ex: 20"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Horas de Estudo",
            color = TextGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CampoTextoPenal(
            valor = horasEstudo,
            aoAlterar = onHorasEstudoChange,
            label = "Ex: 20"
        )
    }
}

@Composable
fun CardResultado() {
    CardBase {
        Text(
            text = "Progressões e Benefícios",
            color = Gold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Datas calculadas conforme a Lei nº 13.964/2019",
            color = TextGray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        ItemResultado(
            titulo = "Progressão para Regime Semiaberto",
            descricao = "Após cumprir a fração exigida da pena",
            data = "25/12/2027",
            porcentagem = "60%"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ItemResultado(
            titulo = "Progressão para Regime Aberto",
            descricao = "Após cumprir nova etapa da pena",
            data = "02/03/2029",
            porcentagem = "70%"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ItemResultado(
            titulo = "Livramento Condicional",
            descricao = "Estimativa conforme requisitos legais",
            data = "02/03/2029",
            porcentagem = "70%"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Importante: esta calculadora fornece apenas estimativas educativas. Consulte sempre um advogado especializado.",
            color = TextGray,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun ItemResultado(
    titulo: String,
    descricao: String,
    data: String,
    porcentagem: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkBlue
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = Gold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⏳",
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = titulo,
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = descricao,
                        color = TextGray,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }

                Text(
                    text = porcentagem,
                    color = TextWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            Gold,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = BorderBlue.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Data estimada",
                color = TextGray,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = data,
                color = Gold,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CardBase(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBlue),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun TituloCard(
    titulo: String,
    subtitulo: String
) {
    Text(
        text = titulo,
        color = Gold,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = subtitulo,
        color = TextGray,
        fontSize = 12.sp
    )
}

@Composable
fun LinhaOpcoes(
    opcaoSelecionada: String,
    opcoes: List<String>,
    aoSelecionar: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        opcoes.forEach { opcao ->
            Button(
                onClick = { aoSelecionar(opcao) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (opcaoSelecionada == opcao) Gold else DarkBlue,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(opcao, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CampoTextoPenal(
    valor: String,
    aoAlterar: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,

        label = {
            Text(
                text = label,
                color = TextGray,
                fontSize = 12.sp
            )
        },

        shape = RoundedCornerShape(14.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = BorderBlue,

            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,

            focusedContainerColor = InputBlue,
            unfocusedContainerColor = InputBlue,

            cursorColor = Gold
        ),

        singleLine = true,

        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    )
}