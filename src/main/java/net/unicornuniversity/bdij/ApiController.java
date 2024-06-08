package net.unicornuniversity.bdij;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import net.unicornuniversity.bdij.entities.IcoDataEntity;
import net.unicornuniversity.bdij.models.IcoData;
import net.unicornuniversity.bdij.repositories.IcoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ObjectMapper objectMapper;

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

    @PatchMapping("/{ico}")
    public IcoDataEntity patchIcoData(@PathVariable String ico, HttpServletRequest request) throws IOException
    {
        Optional<IcoDataEntity> optionalEntity = icoDataRepository.findById(ico);
        if (optionalEntity.isEmpty())
        {
            // TODO: Not found exception with handling
            throw new RuntimeException("Not found");
        }

        IcoDataEntity entity = optionalEntity.get();
        if (entity.getDeleted())
        {
            // TODO: Operation forbidden exception with handling
            throw new RuntimeException("Operation forbidden");
        }

        BufferedReader reader = request.getReader();
        IcoDataEntity updatedEntity = objectMapper.readerForUpdating(entity).readValue(reader);
        icoDataRepository.save(updatedEntity);
        return updatedEntity;
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