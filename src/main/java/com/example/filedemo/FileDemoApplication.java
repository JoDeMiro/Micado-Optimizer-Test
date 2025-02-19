package com.example.filedemo;

import com.example.filedemo.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

/** Main class
 * @author JoDeMiro
 * @author https://github.com/JoDeMiro/Micado-Optimizer-Test
 * @version 1.0
 * @since 1.0
 */

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
@EnableAsync
public class FileDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileDemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
