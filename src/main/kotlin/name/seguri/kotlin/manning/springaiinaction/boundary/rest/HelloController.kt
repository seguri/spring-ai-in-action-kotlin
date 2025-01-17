package name.seguri.kotlin.manning.springaiinaction.boundary.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

  @GetMapping("/hello")
  fun hello(): String {
    return "Hello, World!"
  }
}
