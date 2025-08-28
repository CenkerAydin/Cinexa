package com.cenkeraydin.composemovie.ui.bar

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Movie,
        BottomNavItem.Series,
        BottomNavItem.Person,
        BottomNavItem.Favorites,
        BottomNavItem.Settings
    )

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor =MaterialTheme.colorScheme.background
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        items.forEach { item ->
            val title = stringResource(item.titleRes)

            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = title,
                        modifier = Modifier.size(24.dp) // Tüm ikonlar için sabit boyut!

                    )
                },
                label = {
                    Text(text = title)
                },

            )
        }
    }
}
