package com.mackenzie.waifuviewer.ui.splash


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.nav.NavItem
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import kotlinx.coroutines.delay


@Composable
fun SplashScreenRoute(navController: NavController) {

    val scaleAnimation: Animatable<Float, AnimationVector1D> = remember { Animatable(initialValue = 0f) }

    AnimationSplashContent(
        scaleAnimation = scaleAnimation,
        navController = navController,
        durationMillisAnimation = 3000,
        delayScreen = 10000L
    )

    SplashScreenContent(
        imagePainter = painterResource(id =
        R.drawable.ic_waifu_gemini),
        scaleAnimation = scaleAnimation
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    scaleAnimation: Animatable<Float, AnimationVector1D>
) {

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(143, 244, 235, 255),
            Color(246, 248, 250, 255),
            Color(223, 116, 241, 255),
        ),
        // start = Offset(0.0f, 50.0f),
        // end = Offset(1000f, 1000f)
    )

    // val shapePainter: Painter = painterResource(id = R.drawable.ic_splash_fondo)

    Box(
        modifier = if (isNavigationBarVisible()) { modifier.fillMaxSize().navigationBarsPadding().background(gradientBrush) }
                    else { modifier.fillMaxSize().background(gradientBrush) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Logotipo Splash Screen",
                modifier = modifier
                    .size(400.dp)
                    .scale(scale = scaleAnimation.value),
            )

            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.Black,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = modifier.scale(scale =
                (scaleAnimation.value * 2))
            )

            Spacer(modifier = modifier.size(40.dp))

            Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 5.dp,
                        modifier = modifier.size(50.dp)
                    )
                    Spacer(modifier = modifier.size(25.dp))
                    Text(text = loadingText(), color = Color.White, fontSize = 20.sp)
                }
                // Text(text = loadingText(), color = Color.White, fontSize = 20.sp)
            }

            /*Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 5.dp,
                    modifier = modifier.size(50.dp)
                )
                Spacer(modifier = modifier.size(25.dp))
                Text(text = loadingText(), color = Color.White, fontSize = 20.sp)
            }*/

            // Text(text = loadingText(), color = Color.White, fontSize = 20.sp)
        }
    }

    /*Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Text(text = loadingText(), color = Color.White, fontSize = 20.sp)
    }*/

}

@Composable
fun loadingText(): String {
    var dotCount by remember { mutableStateOf(1) }
    val ctx = LocalContext.current
    LaunchedEffect(Unit) {
        while (true) {
            dotCount = if (dotCount < 3) dotCount + 1 else 1
            delay(500)
        }
    }
    val loadingText = getString(ctx, R.string.waifus_loading) + ".".repeat(dotCount)
    return loadingText
}

@Composable
fun AnimationSplashContent(
    scaleAnimation: Animatable<Float, AnimationVector1D>,
    navController: NavController,
    durationMillisAnimation: Int,
    delayScreen: Long
) {

    LaunchedEffect(key1 = true) {
        scaleAnimation.animateTo(
            targetValue = 0.5F,
            animationSpec = tween(
                durationMillis = durationMillisAnimation,
                easing = {
                    OvershootInterpolator(3F).getInterpolation(it)
                }
            )
        )

        delay(timeMillis = delayScreen)

        navController.navigate(route = NavItem.SelectorScreen.baseRoute) {
            popUpTo(route =
            NavItem.SplashScreen.baseRoute) {
                inclusive = true
            }
        }
    }
}