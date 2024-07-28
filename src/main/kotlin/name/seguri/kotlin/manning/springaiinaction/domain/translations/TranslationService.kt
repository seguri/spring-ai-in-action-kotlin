package name.seguri.kotlin.manning.springaiinaction.domain.translations

import java.io.IOException
import java.nio.charset.Charset
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TranslationService(builder: ChatClient.Builder) {
  private val chatClient: ChatClient = builder.build()
  private val logger = LoggerFactory.getLogger(this::class.java)

  fun translate(question: Question, from: String, to: String): Answer =
    chatClient
      .prompt()
      .system(getTranslationTemplate(from, to))
      .user(question.question)
      .call()
      .entity(Answer::class.java)

  fun translateStream(message: String, from: String, to: String): Flux<String> =
    chatClient
      .prompt()
      .user{ it.text(getTranslationTemplate(from, to)).param("message", message) }
      .stream()
      .contentWithWhitespace()

  private fun getTranslationTemplate(from: String, to: String): String =
    try {
      DefaultResourceLoader()
        .getResource("classpath:/promptTemplates/${from}-${to}.st")
        .inputStream
        .bufferedReader(Charset.defaultCharset())
        .use { it.readText() }
    } catch (e: IOException) {
      logger.error("Could not find template for $from-$to")
      ""
    }
}
