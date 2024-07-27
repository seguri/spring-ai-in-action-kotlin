package name.seguri.kotlin.manning.springaiinaction.domain.chapter3

import java.util.stream.Collectors.joining
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder
import org.springframework.stereotype.Service

@Service
class GameRulesService(vectorStore: VectorStore) {
  private val vectorStore = vectorStore

  fun getRulesFor(gameTitle: GameTitle, question: String): String {
    val searchRequest =
      SearchRequest.query(question)
        .withFilterExpression(
          FilterExpressionBuilder().eq("title", gameTitle.normalizedTitle).build()
        )
    val similarDocs = vectorStore.similaritySearch(searchRequest)

    return if (similarDocs.isEmpty()) {
      "Sorry, I couldn't find any rules for \"${gameTitle.title}\"."
    } else {
      similarDocs.stream().map { it.content }.collect(joining("\n"))
    }
  }
}
