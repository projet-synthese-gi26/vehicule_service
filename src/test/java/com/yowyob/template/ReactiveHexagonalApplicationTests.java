package com.yowyob.template; // Assure-toi que c'est bien ce package

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// 👇 Cette ligne connecte ton test à ta classe principale
@SpringBootTest(classes = TemplateApplication.class)
class ReactiveHexagonalApplicationTests {

	@Test
	void contextLoads() {
		// Le test va démarrer TemplateApplication.
		// Si ça passe, tout est bon.
	}

}