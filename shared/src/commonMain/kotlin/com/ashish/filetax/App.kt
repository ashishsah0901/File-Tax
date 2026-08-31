package com.ashish.filetax

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashish.filetax.onboarding.OnboardingViewModel
import com.ashish.filetax.onboarding.data.OnboardingRepository
import com.ashish.filetax.onboarding.data.keyValueStore
import com.ashish.filetax.onboarding.ui.OnboardingScreen
import com.ashish.filetax.ui.theme.FileTaxTheme

@Composable
fun App() {
    FileTaxTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val viewModel = viewModel { OnboardingViewModel(OnboardingRepository(keyValueStore())) }
            OnboardingScreen(viewModel)
        }
    }
}