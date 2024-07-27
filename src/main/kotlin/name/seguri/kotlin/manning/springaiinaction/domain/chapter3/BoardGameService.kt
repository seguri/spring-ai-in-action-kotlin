package name.seguri.kotlin.manning.springaiinaction.domain.chapter3

import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class BoardGameService(gameRulesService: GameRulesService, builder: ChatClient.Builder) {
  private val gameRulesService = gameRulesService
  private val chatClient: ChatClient = builder.build()

  @Value("classpath:/promptTemplates/gameQuestion.st") private lateinit var prompt: Resource

  fun askGameQuestion(gameQuestion: GameQuestion): GameAnswer {
    val gameTitle = GameTitle(gameQuestion.game)
    val gameRules = gameRulesService.getRulesFor(gameTitle, gameQuestion.question)
    val answer =
      chatClient
        .prompt()
        .system { it.text(prompt).param("game", gameTitle.title).param("rules", gameRules) }
        .user(gameQuestion.question)
        .call()
        .content()

    return GameAnswer(gameTitle.title, answer)
  }
}
