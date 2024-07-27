package name.seguri.kotlin.manning.springaiinaction.boundary.rest

import name.seguri.kotlin.manning.springaiinaction.domain.chapter3.BoardGameService
import name.seguri.kotlin.manning.springaiinaction.domain.chapter3.GameAnswer
import name.seguri.kotlin.manning.springaiinaction.domain.chapter3.GameQuestion
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BoardGameController(private val boardGameService: BoardGameService) {

  @PostMapping("/game-rules", produces = ["application/json"])
  fun gameRules(@RequestBody gameQuestion: GameQuestion): GameAnswer {
    return boardGameService.askGameQuestion(gameQuestion)
  }
}
