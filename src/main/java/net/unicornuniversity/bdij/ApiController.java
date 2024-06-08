package net.unicornuniversity.bdij;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

    @GetMapping("/api/{ico}")
    public String getIcoData(@PathVariable String ico) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

        String result = restTemplate.getForObject(url, String.class);
        return result;
    }
}