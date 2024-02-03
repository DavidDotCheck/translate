package ai.hardt.translate.data.model

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data class OnboardingStep(
    val internalName: String,
    val label: String,
    val description: String,
)

