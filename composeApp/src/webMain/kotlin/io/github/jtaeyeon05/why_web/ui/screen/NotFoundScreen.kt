package io.github.jtaeyeon05.why_web.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.github.jtaeyeon05.why_web.navigation.Screen
import io.github.jtaeyeon05.why_web.ui.foundation.LocalLayoutConstraints


@Composable
fun BoxScope.NotFoundScreen(
    screen: Screen.NotFound,
    navController: NavController,
) {
    LocalLayoutConstraints.current.run {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = screen.route,
        )
    }
}
