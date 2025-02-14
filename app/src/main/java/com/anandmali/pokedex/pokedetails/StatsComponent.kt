package com.anandmali.pokedex.pokedetails

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anandmali.pokedex.data.source.network.response.PokeDetailsResponse
import com.anandmali.pokedex.data.source.network.response.Stat
import kotlin.math.roundToInt

@Composable
fun PokemonBaseStats(
    pokeDetails: PokeDetailsResponse,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        pokeDetails.stats.forEach { statResponse ->
            key(statResponse.stat.name) {
                PokemonStatItem(
                    statResponse = statResponse,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

//TODO, improve the progress calculation and progress bar view.
@Composable
internal fun PokemonStatItem(
    statResponse: Stat,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember {
        Animatable(
            initialValue = 0f,
        )
    }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 8 * statResponse.baseStat,
                easing = LinearEasing
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        val progress = statResponse.baseStat.toFloat() / statResponse.maxValue.toFloat()
        val animatedProgress = progress * animationProgress.value

        val progressColor = MaterialTheme.colorScheme.primary
        val progressTrackColor = MaterialTheme.colorScheme.outline.copy(.1f)

        Text(
            text = statResponse.name,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(.3f).padding(end = 8.dp),
            maxLines = 1
        )

        Box(
            modifier = Modifier
                .weight(.7f)
                .height(20.dp)
                .drawBehind {
                    drawRoundRect(
                        color = progressTrackColor,
                        topLeft = Offset.Zero,
                        size = size,
                        cornerRadius = CornerRadius(size.height, size.height),
                    )

                    drawRoundRect(
                        color = progressColor,
                        topLeft = Offset.Zero,
                        size = Size(
                            width = (size.width * animatedProgress) - 100f,
                            height = size.height
                        ),
                        cornerRadius = CornerRadius(size.height, size.height),
                    )
                }
        ) {
            Text(
                text = "${(statResponse.baseStat * animationProgress.value).roundToInt()}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}