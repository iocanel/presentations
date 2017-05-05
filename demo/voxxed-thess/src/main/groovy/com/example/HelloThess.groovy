package com.example

import org.springframework.web.bind.annotation.*

@RestController
class HelloThess {
  @RequestMapping("/")
  String hello() {
    "Hello Thessaloniki using Spring!"
  }
}

