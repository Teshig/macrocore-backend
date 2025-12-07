package org.grimjo.macrocore.infrastructure.api;

import lombok.Builder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Builder
@RestController
public class StatisticController {

  @GetMapping("/")
  public String statistic() {
    return "Hello from Fly.io StatisticController!";
  }
}
