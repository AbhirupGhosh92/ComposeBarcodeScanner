package com.app.gauntlet.core.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable

@Composable
fun NavSlideUpDown(shouldShow : Boolean,content : @Composable AnimatedVisibilityScope.() -> Unit)
{
    AnimatedVisibility(visible = shouldShow,
        content = content)
}