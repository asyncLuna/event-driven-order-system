package dev.asyncluna.orders;

import org.springframework.boot.SpringApplication;

public class TestEventDrivenOrderSystemApplication {

  public static void main(String[] args) {
    SpringApplication.from(EventDrivenOrderSystemApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
