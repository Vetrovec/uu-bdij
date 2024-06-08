package net.unicornuniversity.bdij;

import net.unicornuniversity.bdij.entities.IcoDataEntity;
import net.unicornuniversity.bdij.models.IcoData;
import net.unicornuniversity.bdij.repositories.IcoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

    @Autowired
    private IcoDataRepository icoDataRepository;

    @GetMapping("/api/{ico}")
    public IcoData getIcoData(@PathVariable String ico) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

        IcoData result = restTemplate.getForObject(url, IcoData.class);

        // Convert IcoData to IcoDataEntity and save to database
        IcoDataEntity entity = IcoDataEntity.from(result);
        icoDataRepository.save(entity);

        return result;
    }
}