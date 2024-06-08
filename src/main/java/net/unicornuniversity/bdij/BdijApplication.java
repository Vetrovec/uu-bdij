package net.unicornuniversity.bdij;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class BdijApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdijApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/api/{ico}")
	public String getIcoData(@PathVariable String ico) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

		String result = restTemplate.getForObject(url, String.class);
		return result;
	}
}
