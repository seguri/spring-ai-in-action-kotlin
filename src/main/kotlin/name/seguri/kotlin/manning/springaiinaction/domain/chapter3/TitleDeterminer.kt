package name.seguri.kotlin.manning.springaiinaction.domain.chapter3

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.document.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import reactor.core.publisher.Flux

private val logger by lazy { LoggerFactory.getLogger("TitleDeterminer") }

@Bean
fun titleDeterminer(
  builder: ChatClient.Builder,
  @Value("classpath:/promptTemplates/gameTitle.st") template: Resource,
): (Flux<List<Document>>) -> Flux<List<Document>> = { documentsFlux ->
  documentsFlux.map { documents ->
    if (documents.isEmpty()) {
      return@map documents
    }

    val chatClient = builder.build()
    val gameTitle =
      chatClient
        .prompt()
        .user { it.text(template).param("document", documents.first().content) }
        .call()
        .entity(GameTitle::class.java)

    logger.info("Determined title: ${gameTitle.title}")

    if (gameTitle.title.equals("UNKNOWN", ignoreCase = true)) {
      emptyList()
    } else {
      documents.onEach { it.metadata["title"] = gameTitle.normalizedTitle }
    }
  }
}
