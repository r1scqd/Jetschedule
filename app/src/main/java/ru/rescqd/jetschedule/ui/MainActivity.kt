package ru.rescqd.jetschedule.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.rescqd.jetschedule.ui.theme.JetscheduleTheme
import ru.rescqd.jetschedule.ui.theme.ThemeViewModel
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val DEBUG = "MAIN_ACTIVITY"
    }

    @Composable
    fun LoadingTheme(themeViewModel: ThemeViewModel, content: @Composable () -> Unit) {
        Log.d(DEBUG, "Loading Theme started ${LocalDateTime.now()}")
        val appTheme = themeViewModel.getAppThemeFlow()
            .collectAsState(initial = themeViewModel.getAppThemeValue())
        val colorScheme = themeViewModel.getColorSchemeFlow()
            .collectAsState(initial = themeViewModel.getColorSchemeValue())
        val typography = themeViewModel.getThemeTypographyFlow()
            .collectAsState(initial = themeViewModel.getThemeTypographyValue())
        val audienceColorScheme = themeViewModel.getThemeAudienceColorSchemeFlow()
            .collectAsState(initial = themeViewModel.getThemeAudienceColorSchemeValue())
        Log.d(DEBUG, "Loading Theme stopped ${LocalDateTime.now()}")
        JetscheduleTheme(
            appTheme = appTheme.value,
            colorSchemeEnum = colorScheme.value,
            typographyEnum = typography.value,
            audienceColorSchemeEnum = audienceColorScheme.value,
            content = content,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(DEBUG, "activity create in ${LocalDateTime.now()}")
        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            LoadingTheme(themeViewModel) {
                JetscheduleApp(themeViewModel)
            }
        }
    }
}
