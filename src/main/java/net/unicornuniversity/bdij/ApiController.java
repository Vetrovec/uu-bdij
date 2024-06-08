package net.unicornuniversity.bdij;

import net.unicornuniversity.bdij.entities.IcoDataEntity;
import net.unicornuniversity.bdij.models.IcoData;
import net.unicornuniversity.bdij.repositories.IcoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private IcoDataRepository icoDataRepository;

    @GetMapping("/{ico}")
    public IcoData getIcoData(@PathVariable String ico) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

        IcoData result = restTemplate.getForObject(url, IcoData.class);

        // Convert IcoData to IcoDataEntity and save to database
        IcoDataEntity entity = IcoDataEntity.from(result);
        icoDataRepository.save(entity);

        return result;
    }

    @PostMapping()
    public IcoData saveIcoData(@RequestBody IcoData icoData) {
        IcoDataEntity entity = IcoDataEntity.from(icoData);

        icoDataRepository.save(entity);
        return icoData;
    }

    @DeleteMapping("/{ico}")
    public void deleteIcoData(@PathVariable String ico)
    {
        Optional<IcoDataEntity> optionalEntity = icoDataRepository.findById(ico);
        if (optionalEntity.isPresent())
        {
            IcoDataEntity entity = optionalEntity.get();

            if (entity.getDeleted())
            {
                // TODO: Operation forbidden exception with handling
                throw new RuntimeException("Operation forbidden");
            }

            entity.setDeleted(true);
            icoDataRepository.save(entity);
            return;
        }

        // TODO: Remove repeating code
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;
        IcoData data = restTemplate.getForObject(url, IcoData.class);

        if (data == null)
        {
            // TODO: Not found exception with handling
            throw new RuntimeException("Not found");
        }

        IcoDataEntity entity = IcoDataEntity.from(data);
        entity.setDeleted(true);
        icoDataRepository.save(entity);
    }
}