package name.seguri.kotlin.manning.springaiinaction

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AskController(private val askService: AskService) {

  @PostMapping("/ask", produces = ["application/json"])
  fun ask(@RequestBody question: Question): Answer {
    return askService.ask(question)
  }
}
