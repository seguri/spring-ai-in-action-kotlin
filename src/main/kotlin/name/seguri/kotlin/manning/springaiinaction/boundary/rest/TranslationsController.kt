package name.seguri.kotlin.manning.springaiinaction.boundary.rest

import name.seguri.kotlin.manning.springaiinaction.domain.translations.Answer
import name.seguri.kotlin.manning.springaiinaction.domain.translations.Question
import name.seguri.kotlin.manning.springaiinaction.domain.translations.TranslationService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.util.StringUtils.hasText
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux

@Controller
class TranslationsController(private val translationService: TranslationService) {

  @GetMapping("/translate")
  fun translate(model: Model): String {
    model["pageTitle"] = "TranslateBot"
    model["targetControllerPath"] = "/translate-stream/it-de"
    return "bot"
  }

  @PostMapping("/translate/de-it", produces = [APPLICATION_JSON_VALUE])
  @ResponseBody
  fun translateDeIt(@RequestBody question: Question): Answer {
    return translationService.translate(question, "de", "it")
  }

  @GetMapping("/translate-stream/it-de", produces = [TEXT_EVENT_STREAM_VALUE])
  @ResponseBody
  fun translateStreamItDe(@RequestParam("message") message: String): Flux<String> {
    if (!hasText(message)) return Flux.empty()
    return translationService.translateStream(message, "it", "de")
  }
}
