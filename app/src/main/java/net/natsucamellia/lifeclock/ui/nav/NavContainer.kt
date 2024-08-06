package net.natsucamellia.lifeclock.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.natsucamellia.lifeclock.ui.model.LifeClockModel
import net.natsucamellia.lifeclock.ui.screens.SettingScreen
import net.natsucamellia.lifeclock.ui.screens.TimerScreen


@Composable
fun NavContainer(
    initialTab: NavRoutes = NavRoutes.Timer,
    lifeClockModel: LifeClockModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = initialTab.route,
    ) {
        composable(NavRoutes.Timer.route) {
            TimerScreen(lifeClockModel) { navController.navigate(NavRoutes.Setting.route) }
        }
        composable(NavRoutes.Setting.route) {
            SettingScreen(lifeClockModel, navController::navigateUp)
        }
    }
}