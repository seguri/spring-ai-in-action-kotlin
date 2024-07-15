package name.seguri.kotlin.manning.springaiinaction

import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service

@Service
class AskService(builder: ChatClient.Builder) {

  private val chatClient: ChatClient = builder.build()

  fun ask(question: Question): Answer {
    return Answer(chatClient.prompt().user(question.question).call().content())
  }
}
