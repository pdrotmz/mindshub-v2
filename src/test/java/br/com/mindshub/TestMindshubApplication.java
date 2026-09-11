package br.com.mindshub;

import org.springframework.boot.SpringApplication;

public class TestMindshubApplication {

	public static void main(String[] args) {
		SpringApplication.from(MindshubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
