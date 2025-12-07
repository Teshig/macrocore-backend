package org.grimjo.macrocore.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.grimjo.macrocore.infrastructure.api.StatisticController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class StatisticControllerTest {
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    StatisticController controller = StatisticController.builder().build();
    this.mvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void statistic() throws Exception {
    mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello from Fly.io StatisticController!"));
  }
}
