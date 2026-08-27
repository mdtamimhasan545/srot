package com.srot.downloader

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.srot.downloader.ui.about.AboutScreen
import com.srot.downloader.ui.home.HomeScreen
import com.srot.downloader.ui.queue.QueueScreen
import com.srot.downloader.ui.settings.SettingsScreen
import com.srot.downloader.ui.theme.SrotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedUrl = intent?.getStringExtra(Intent.EXTRA_TEXT)
        setContent {
            val app = application as SrotApp
            SrotTheme {
                SrotRoot(sharedUrl = sharedUrl, app = app)
            }
        }
    }
}

private enum class Tab(val route: String) {
    Home("home"), Queue("queue"), Settings("settings"), About("about")
}

@Composable
private fun SrotRoot(sharedUrl: String?, app: SrotApp) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route ?: Tab.Home.route
    val vm: MainViewModel = viewModel(factory = MainViewModel.factory(app))

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = current == Tab.Home.route,
                    onClick = { nav.navigate(Tab.Home.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text(stringResource(R.string.home)) }
                )
                NavigationBarItem(
                    selected = current == Tab.Queue.route,
                    onClick = { nav.navigate(Tab.Queue.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text(stringResource(R.string.queue)) }
                )
                NavigationBarItem(
                    selected = current == Tab.Settings.route,
                    onClick = { nav.navigate(Tab.Settings.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(stringResource(R.string.settings)) }
                )
                NavigationBarItem(
                    selected = current == Tab.About.route,
                    onClick = { nav.navigate(Tab.About.route) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text(stringResource(R.string.about)) }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Tab.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Tab.Home.route) {
                HomeScreen(vm = vm, initialUrl = sharedUrl)
            }
            composable(Tab.Queue.route) {
                QueueScreen(vm = vm)
            }
            composable(Tab.Settings.route) {
                SettingsScreen(vm = vm)
            }
            composable(Tab.About.route) {
                AboutScreen()
            }
        }
    }
}
