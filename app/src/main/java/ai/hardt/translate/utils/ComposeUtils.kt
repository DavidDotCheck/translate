package ai.hardt.translate.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import kotlin.reflect.KProperty

@Composable
fun <T> rememberState(initialValue: T): MutableState<T> {
    return remember { mutableStateOf(initialValue) }
}

@Composable
fun <T> saveableState(initialState: T): MutableState<T> {
    return rememberSaveable { mutableStateOf(initialState) }
}