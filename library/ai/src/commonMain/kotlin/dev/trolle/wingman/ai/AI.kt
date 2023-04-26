package dev.trolle.wingman.ai

import com.aallam.openai.api.chat.ChatChoice
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.logging.Logger
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import dev.trolle.wingman.common.ext.runCatchingCancelable
import io.github.aakira.napier.Napier


interface AI {
    // TODO Wrap response
    suspend fun prompt(context: List<String>, texts: List<String>): String?
}


internal fun ai(
    openAiApiToken: String,
) = object : AI {

    private val modelV4 = ModelId("gpt-4")
    private val modelV3 = ModelId("gpt-3.5-turbo")

    val openAI: OpenAI = OpenAI(
        OpenAIConfig(
            token = openAiApiToken,
            logLevel = LogLevel.Info,
            logger = Logger.Default,
        ),
    )

    override suspend fun prompt(
        context: List<String>,
        texts: List<String>,
    ): String? {
        if (openAiApiToken.isEmpty()) return null
        val creativity = 1.0 // how wild answers can be, between 0.1 - 2
        val n = 1 // number of choices
        val model = modelV3
        return runCatchingCancelable {
            openAI.chatCompletion(
                ChatCompletionRequest(
                    model = model,
                    temperature = creativity,
                    n = n,
                    maxTokens = 160,
                    messages = buildList {
                        context.forEach {
                            add(ChatMessage(role = ChatRole.System, content = it))
                        }
                        texts.forEach {
                            add(ChatMessage(role = ChatRole.User, content = it))
                        }
                    },
                ),
            ).choices.first()
        }.onFailure {
            Napier.e("ai", it)
        }.getOrThrow()?.message?.content
    }
}
