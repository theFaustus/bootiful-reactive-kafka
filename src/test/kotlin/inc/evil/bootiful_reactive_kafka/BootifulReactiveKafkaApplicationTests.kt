package inc.evil.bootiful_reactive_kafka

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class BootifulReactiveKafkaApplicationTests {

	@Test
	fun contextLoads() {
	}

}
