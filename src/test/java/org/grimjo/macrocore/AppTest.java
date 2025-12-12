package org.grimjo.macrocore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AppTest {
  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoads() {
    assertThat(context).isNotNull();
  }

  @Test
  void main() {
    App.main(new String[] {});
  }
}
