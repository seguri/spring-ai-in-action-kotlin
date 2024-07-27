package name.seguri.kotlin.manning.springaiinaction.domain.chapter3

import java.util.*

data class GameTitle(val title: String) {
  val normalizedTitle: String
    get() = title.lowercase(Locale.getDefault()).replace(" ", "_")
}
