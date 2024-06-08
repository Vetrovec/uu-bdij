package net.unicornuniversity.bdij;

import net.unicornuniversity.bdij.models.IcoData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

    @GetMapping("/api/{ico}")
    public IcoData getIcoData(@PathVariable String ico) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

        IcoData result = restTemplate.getForObject(url, IcoData.class);

        return result;
    }
}