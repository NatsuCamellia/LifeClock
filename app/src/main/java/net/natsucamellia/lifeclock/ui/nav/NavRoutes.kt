package net.natsucamellia.lifeclock.ui.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import net.natsucamellia.lifeclock.R

sealed class NavRoutes(
    val route: String,
    @StringRes val stringRes: Int,
    val icon: ImageVector
) {
    data object Timer : NavRoutes("timer", R.string.timer, Icons.Outlined.HourglassEmpty)
    data object Setting : NavRoutes("setting", R.string.setting, Icons.Outlined.Settings)
}