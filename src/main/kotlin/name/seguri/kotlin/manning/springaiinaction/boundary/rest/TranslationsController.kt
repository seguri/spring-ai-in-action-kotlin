package name.seguri.kotlin.manning.springaiinaction.boundary.rest

import name.seguri.kotlin.manning.springaiinaction.domain.translations.Answer
import name.seguri.kotlin.manning.springaiinaction.domain.translations.Question
import name.seguri.kotlin.manning.springaiinaction.domain.translations.TranslationService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE
import org.springframework.util.StringUtils.hasText
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class TranslationsController(private val translationService: TranslationService) {

  @PostMapping("/translate/de-it", produces = [APPLICATION_JSON_VALUE])
  fun translateDeIt(@RequestBody question: Question): Answer {
    return translationService.translate(question, "de", "it")
  }

  @GetMapping("/translate-stream/it-de", produces = [TEXT_EVENT_STREAM_VALUE])
  fun translateStreamItDe(@RequestParam("message") message: String): Flux<String> {
    if (!hasText(message)) return Flux.empty()
    return translationService.translateStream(message, "it", "de")
  }
}
