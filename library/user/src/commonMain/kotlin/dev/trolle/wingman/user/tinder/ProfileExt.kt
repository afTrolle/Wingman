package dev.trolle.wingman.user.tinder

import dev.trolle.wingman.user.tinder.model.ProfileResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.yearsUntil

fun ProfileResponse.Profile.profileString(): String = listOfNotNull(
    "Name: $name",
    birthDate?.run { "Age: ${yearsUntil(Clock.System.now(), TimeZone.UTC)} years" },
    userInterests.selectedInterests.takeIf { it.isNotEmpty() }
        ?.run { "Interests: ${this.joinToString { it.name }}" },
    selectedDescriptors.takeIf { it.isNotEmpty() }?.let { selectedDescriptors ->
        buildString {
            val descriptorsByName = selectedDescriptors.groupBy { it.sectionName }
            descriptorsByName.entries.forEachIndexed { index, it ->
                if (it.value.size > 1) appendLine("${it.key}:")

                it.value.forEach {
                    val line = promptLine(it) ?: nameLine(it)
                    appendLine(line)
                }
            }
        }.dropLast(1)
    },
    spotifyThemeTrack?.run { "Soundtrack of my life: $name by ${artists.joinToString { it.name }}" },
    bio?.let {
        buildString {
            appendLine("Biography:")
            append("\"")
            append(it)
            append("\"")
        }
    },
).joinToString(separator = "\n")

private fun promptLine(line: ProfileResponse.Profile.SelectedDescriptor): String? {
    return line.prompt?.let { prompt ->
        "$prompt ${line.choiceSelections.joinToString { it.name }}"
    }
}

private fun nameLine(line: ProfileResponse.Profile.SelectedDescriptor): String {
    val title = line.name ?: line.sectionName
    return "$title: ${line.choiceSelections.joinToString { it.name }}"
}
