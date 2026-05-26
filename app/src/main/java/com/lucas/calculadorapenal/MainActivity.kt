package com.lucas.calculadorapenal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucas.calculadorapenal.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

data class ResultadoCalculo(
    val erro: String? = null,
    val dataSemiaberto: String? = null,
    val dataAberto: String? = null,
    val dataLivramento: String? = null,
    val dataTermino: String? = null,
    val percentualProgressao: String = "",
    val percentualAberto: String = "",
    val percentualLivramento: String = "",
    val avisoLivramento: String? = null
)

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
    val context = LocalContext.current

    var anos by remember { mutableStateOf("") }
    var meses by remember { mutableStateOf("") }
    var dias by remember { mutableStateOf("") }

    var dataInicio by remember { mutableStateOf("") }
    var regimeInicial by remember { mutableStateOf("Fechado") }
    var tipoCrime by remember { mutableStateOf("Comum") }
    var violenciaGraveAmeaca by remember { mutableStateOf("Não") }
    var resultadoMorte by remember { mutableStateOf("Não") }
    var statusApenado by remember { mutableStateOf("Primário") }

    var detracaoAnos by remember { mutableStateOf("") }
    var detracaoMeses by remember { mutableStateOf("") }
    var detracaoDias by remember { mutableStateOf("") }

    var diasTrabalhados by remember { mutableStateOf("") }
    var horasEstudo by remember { mutableStateOf("") }

    var resultadoVisivel by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var resultadoCalculo by remember { mutableStateOf<ResultadoCalculo?>(null) }

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

            regimeInicial = regimeInicial,
            onRegimeInicialChange = { regimeInicial = it },

            tipoCrime = tipoCrime,
            onTipoCrimeChange = { tipoCrime = it },

            violenciaGraveAmeaca = violenciaGraveAmeaca,
            onViolenciaChange = { violenciaGraveAmeaca = it },

            resultadoMorte = resultadoMorte,
            onResultadoMorteChange = { resultadoMorte = it },

            statusApenado = statusApenado,
            onStatusChange = { statusApenado = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CardDetracaoPena(
            anos = detracaoAnos,
            meses = detracaoMeses,
            dias = detracaoDias,
            onAnosChange = { detracaoAnos = it },
            onMesesChange = { detracaoMeses = it },
            onDiasChange = { detracaoDias = it }
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

                resultadoCalculo = calcularResultados(
                    anos = anos,
                    meses = meses,
                    dias = dias,
                    dataInicio = dataInicio,
                    regimeInicial = regimeInicial,
                    tipoCrime = tipoCrime,
                    violenciaGraveAmeaca = violenciaGraveAmeaca,
                    resultadoMorte = resultadoMorte,
                    statusApenado = statusApenado,
                    diasTrabalhados = diasTrabalhados,
                    horasEstudo = horasEstudo,
                    detracaoAnos = detracaoAnos,
                    detracaoMeses = detracaoMeses,
                    detracaoDias = detracaoDias
                )

                resultadoVisivel = true
                carregando = false
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold,
                contentColor = TextWhite
            ),
            shape = RoundedCornerShape(18.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
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

        if (resultadoVisivel && resultadoCalculo != null) {
            Spacer(modifier = Modifier.height(18.dp))
            CardResultado(resultado = resultadoCalculo!!)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val numero = "5511989498044"
                val mensagem = "Olá, gostaria de tirar uma dúvida sobre execução penal."
                val url = "https://api.whatsapp.com/send?phone=$numero&text=${Uri.encode(mensagem)}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF25D366),
                contentColor = TextWhite
            ),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .width(280.dp)
                .height(52.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.whatsapp_logo),
                    contentDescription = "WhatsApp",
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Falar com Advogado",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

fun calcularResultados(
    anos: String,
    meses: String,
    dias: String,
    dataInicio: String,
    regimeInicial: String,
    tipoCrime: String,
    violenciaGraveAmeaca: String,
    resultadoMorte: String,
    statusApenado: String,
    diasTrabalhados: String,
    horasEstudo: String,
    detracaoAnos: String,
    detracaoMeses: String,
    detracaoDias: String
): ResultadoCalculo {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val anosInt = anos.toIntOrNull() ?: 0
    val mesesInt = meses.toIntOrNull() ?: 0
    val diasInt = dias.toIntOrNull() ?: 0

    val totalDiasPena = (anosInt * 365) + (mesesInt * 30) + diasInt

    val detracaoTotalDias =
        ((detracaoAnos.toIntOrNull() ?: 0) * 365) +
                ((detracaoMeses.toIntOrNull() ?: 0) * 30) +
                (detracaoDias.toIntOrNull() ?: 0)

    if (totalDiasPena <= 0) {
        return ResultadoCalculo(
            erro = "Informe a duração da pena para realizar o cálculo."
        )
    }

    if (detracaoTotalDias > totalDiasPena) {
        return ResultadoCalculo(
            erro = "O tempo de detração não pode ser maior que a pena total."
        )
    }

    if (dataInicio.length != 10) {
        return ResultadoCalculo(
            erro = "Selecione a data de início da pena."
        )
    }

    val dataBase = try {
        LocalDate.parse(dataInicio, formatter)
    } catch (e: Exception) {
        return ResultadoCalculo(
            erro = "Data inválida. Selecione a data novamente."
        )
    }

    val percentualProgressao = calcularPercentualProgressao(
        tipoCrime = tipoCrime,
        violenciaGraveAmeaca = violenciaGraveAmeaca,
        resultadoMorte = resultadoMorte,
        statusApenado = statusApenado
    )

    val percentualAberto = (percentualProgressao + 0.10).coerceAtMost(1.0)

    val percentualLivramento = calcularPercentualLivramento(
        tipoCrime = tipoCrime,
        resultadoMorte = resultadoMorte,
        statusApenado = statusApenado
    )

    val remicao = calcularRemicao(
        diasTrabalhados = diasTrabalhados,
        horasEstudo = horasEstudo
    )

    fun calcularData(percentual: Double): String {
        val diasNecessarios = ceil(totalDiasPena * percentual).toLong()
        val diasComDescontos =
            (diasNecessarios - remicao - detracaoTotalDias).coerceAtLeast(0)

        return dataBase.plusDays(diasComDescontos).format(formatter)
    }

    val termino = dataBase
        .plusDays((totalDiasPena.toLong() - remicao - detracaoTotalDias).coerceAtLeast(0))
        .format(formatter)

    val livramentoVedado = tipoCrime == "Hediondo" && resultadoMorte == "Sim"

    return ResultadoCalculo(
        dataSemiaberto = if (regimeInicial == "Fechado") calcularData(percentualProgressao) else null,
        dataAberto = if (regimeInicial == "Fechado" || regimeInicial == "Semiaberto") calcularData(percentualAberto) else null,
        dataLivramento = if (!livramentoVedado) calcularData(percentualLivramento) else null,
        dataTermino = termino,
        percentualProgressao = "${(percentualProgressao * 100).toInt()}%",
        percentualAberto = "${(percentualAberto * 100).toInt()}%",
        percentualLivramento = "${(percentualLivramento * 100).toInt()}%",
        avisoLivramento = if (livramentoVedado) {
            "Livramento condicional vedado para crime hediondo com resultado morte."
        } else null
    )
}

fun calcularPercentualProgressao(
    tipoCrime: String,
    violenciaGraveAmeaca: String,
    resultadoMorte: String,
    statusApenado: String
): Double {
    val reincidente = statusApenado == "Reincidente"
    val violencia = violenciaGraveAmeaca == "Sim"
    val morte = resultadoMorte == "Sim"

    return when {
        tipoCrime == "Hediondo" && morte && reincidente -> 0.85
        tipoCrime == "Hediondo" && morte && !reincidente -> 0.75
        tipoCrime == "Hediondo" && reincidente -> 0.80
        tipoCrime == "Hediondo" && !reincidente -> 0.70

        tipoCrime == "Comum" && violencia && reincidente -> 0.30
        tipoCrime == "Comum" && violencia && !reincidente -> 0.25
        tipoCrime == "Comum" && !violencia && reincidente -> 0.20
        else -> 0.16
    }
}

fun calcularPercentualLivramento(
    tipoCrime: String,
    resultadoMorte: String,
    statusApenado: String
): Double {
    val reincidente = statusApenado == "Reincidente"

    return when {
        tipoCrime == "Hediondo" && resultadoMorte == "Sim" -> 1.0
        tipoCrime == "Hediondo" -> 2.0 / 3.0
        reincidente -> 0.50
        else -> 1.0 / 3.0
    }
}

fun calcularRemicao(
    diasTrabalhados: String,
    horasEstudo: String
): Long {
    val trabalho = diasTrabalhados.toLongOrNull() ?: 0L
    val estudo = horasEstudo.toLongOrNull() ?: 0L

    val remicaoTrabalho = trabalho / 3
    val remicaoEstudo = estudo / 12

    return remicaoTrabalho + remicaoEstudo
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

    regimeInicial: String,
    onRegimeInicialChange: (String) -> Unit,

    tipoCrime: String,
    onTipoCrimeChange: (String) -> Unit,

    violenciaGraveAmeaca: String,
    onViolenciaChange: (String) -> Unit,

    resultadoMorte: String,
    onResultadoMorteChange: (String) -> Unit,

    statusApenado: String,
    onStatusChange: (String) -> Unit
) {
    CardBase {
        TituloCard(
            titulo = "Características do Crime",
            subtitulo = "Regime e tipo de delito"
        )

        Spacer(modifier = Modifier.height(18.dp))

        CampoDataPenal(
            valor = dataInicio,
            aoSelecionarData = onDataInicioChange,
            label = "Selecionar data de início da pena"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Regime Inicial", color = TextGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LinhaOpcoes(
            opcaoSelecionada = regimeInicial,
            opcoes = listOf("Fechado", "Semiaberto", "Aberto"),
            aoSelecionar = onRegimeInicialChange
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

        Text("Violência ou grave ameaça?", color = TextGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LinhaOpcoes(
            opcaoSelecionada = violenciaGraveAmeaca,
            opcoes = listOf("Não", "Sim"),
            aoSelecionar = onViolenciaChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Resultou em morte?", color = TextGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LinhaOpcoes(
            opcaoSelecionada = resultadoMorte,
            opcoes = listOf("Não", "Sim"),
            aoSelecionar = onResultadoMorteChange
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
fun CardDetracaoPena(
    anos: String,
    meses: String,
    dias: String,
    onAnosChange: (String) -> Unit,
    onMesesChange: (String) -> Unit,
    onDiasChange: (String) -> Unit
) {
    CardBase {
        TituloCard(
            titulo = "Tempo de Detração",
            subtitulo = "Tempo já cumprido ou abatido da pena"
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
fun CardResultado(resultado: ResultadoCalculo) {
    CardBase {
        Text(
            text = "Progressões e Benefícios",
            color = Gold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Datas calculadas conforme parâmetros informados",
            color = TextGray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (resultado.erro != null) {
            Text(
                text = resultado.erro,
                color = Gold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            return@CardBase
        }

        resultado.dataSemiaberto?.let {
            ItemResultado(
                titulo = "Progressão para Regime Semiaberto",
                descricao = "Após cumprir ${resultado.percentualProgressao} da pena, com eventual remição e detração aplicadas.",
                data = it,
                porcentagem = resultado.percentualProgressao
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        resultado.dataAberto?.let {
            ItemResultado(
                titulo = "Progressão para Regime Aberto",
                descricao = "Estimativa de nova etapa de progressão.",
                data = it,
                porcentagem = resultado.percentualAberto
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        resultado.dataLivramento?.let {
            ItemResultado(
                titulo = "Livramento Condicional",
                descricao = "Estimativa conforme os requisitos legais gerais.",
                data = it,
                porcentagem = resultado.percentualLivramento
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        resultado.avisoLivramento?.let {
            Text(
                text = it,
                color = Gold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        resultado.dataTermino?.let {
            ItemResultado(
                titulo = "Término da Pena",
                descricao = "Data estimada para cumprimento integral da pena.",
                data = it,
                porcentagem = "100%"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Importante: esta calculadora fornece apenas estimativas educativas. Os cálculos podem variar conforme decisões judiciais, faltas graves, detração, remição e análise do caso concreto.",
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
        colors = CardDefaults.cardColors(containerColor = DarkBlue),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    Text(text = "⏳", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
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
                        .background(Gold, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = BorderBlue.copy(alpha = 0.5f))

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
                Text(
                    text = opcao,
                    fontSize = 11.sp,
                    maxLines = 1
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDataPenal(
    valor: String,
    aoSelecionarData: (String) -> Unit,
    label: String
) {
    var mostrarCalendario by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Button(
        onClick = { mostrarCalendario = true },
        colors = ButtonDefaults.buttonColors(
            containerColor = InputBlue,
            contentColor = TextWhite
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(
            text = if (valor.isBlank()) label else valor,
            color = if (valor.isBlank()) TextGray else TextWhite,
            fontSize = 14.sp
        )
    }

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis

                        if (millis != null) {
                            val data = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()

                            val dataFormatada =
                                "%02d/%02d/%04d".format(
                                    data.dayOfMonth,
                                    data.monthValue,
                                    data.year
                                )

                            aoSelecionarData(dataFormatada)
                        }

                        mostrarCalendario = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarCalendario = false }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}