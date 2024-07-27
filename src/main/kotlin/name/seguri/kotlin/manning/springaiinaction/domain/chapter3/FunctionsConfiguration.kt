package name.seguri.kotlin.manning.springaiinaction.domain.chapter3

import org.springframework.ai.document.Document
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

@Configuration
class FunctionsConfiguration {

  @Bean
  fun documentReader(): (Flux<ByteArray>) -> Flux<Document> = { resourceFlux ->
    resourceFlux
      .map { TikaDocumentReader(ByteArrayResource(it)).get().first() }
      .subscribeOn(Schedulers.boundedElastic())
  }

  @Bean
  fun splitter(): (Flux<Document>) -> Flux<List<Document>> = { documentFlux ->
    documentFlux
      .map { TokenTextSplitter().apply(listOf(it)) }
      .subscribeOn(Schedulers.boundedElastic())
  }

  @Bean
  fun vectorStoreConsumer(vectorStore: VectorStore): (Flux<List<Document>>) -> Unit =
    { documentFlux ->
      // TODO Wrap this with `kotlinx.coroutines.reactor.mono`
      documentFlux.doOnNext(vectorStore::add).subscribe()
    }
}
