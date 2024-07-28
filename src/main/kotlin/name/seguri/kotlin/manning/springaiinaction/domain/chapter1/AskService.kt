package name.seguri.kotlin.manning.springaiinaction.domain.chapter1

import name.seguri.kotlin.manning.springaiinaction.domain.translations.contentWithWhitespace
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AskService(builder: ChatClient.Builder) {

  private val chatClient: ChatClient = builder.build()

  fun ask(question: Question): Answer {
    return Answer(chatClient.prompt().user(question.question).call().content())
  }

  fun askStream(message: String): Flux<String> =
    chatClient.prompt().user(message).stream().contentWithWhitespace()
}
