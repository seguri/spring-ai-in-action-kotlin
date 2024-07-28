package name.seguri.kotlin.manning.springaiinaction.boundary.rest

import name.seguri.kotlin.manning.springaiinaction.domain.chapter1.AskService
import org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux

@Controller
class AskController(private val service: AskService) {

  @GetMapping("/ask")
  fun ask(model: Model): String {
    model["pageTitle"] = "AskBot"
    model["targetControllerPath"] = "/ask-stream"
    return "bot"
  }

  @GetMapping("/ask-stream", produces = [TEXT_EVENT_STREAM_VALUE])
  @ResponseBody
  fun askStream(@RequestParam("message") message: String): Flux<String> =
    if (message.isEmpty()) Flux.empty() else service.askStream(message)
}
