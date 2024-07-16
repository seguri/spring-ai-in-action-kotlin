package name.seguri.kotlin.manning.springaiinaction.domain.translations

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.ollama.api.OllamaApi.Message
import reactor.core.publisher.Flux

/**
 * There is a potential issue where the content returned by {@link
 * ChatClient.ChatClientRequest.StreamResponseSpec#content()} is left-trimmed once it is sent back
 * by the Controller. This function increases the minimum number of left spaces to two.
 *
 * <p>See <a
 * href="https://html.spec.whatwg.org/multipage/server-sent-events.html#event-stream-interpretation">9.2.6
 * Interpreting an event stream</a>, especially the part about "the space after the colon is ignored
 * if present".
 */
fun ChatClient.ChatClientRequest.StreamResponseSpec.contentWithWhitespace(): Flux<String> =
  chatResponse().map {
    val content = it.result?.output?.content ?: ""
    if (content.startsWith(" ")) " $content" else content
  }

/**
 * This other solution wraps the content in a {@link Message} object, which preserves the leading
 * space.
 */
fun ChatClient.ChatClientRequest.StreamResponseSpec.toMessage(): Flux<Message> =
  chatResponse().mapNotNull {
    it?.result?.output?.let { outPut ->
      if (outPut.content.isNullOrEmpty()) return@mapNotNull null
      Message(Message.Role.ASSISTANT, outPut.content, emptyList())
    }
  }
