package net.unicornuniversity.bdij;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import net.unicornuniversity.bdij.entities.IcoDataEntity;
import net.unicornuniversity.bdij.exceptions.InvalidIcoException;
import net.unicornuniversity.bdij.exceptions.NotFoundException;
import net.unicornuniversity.bdij.exceptions.OperationForbiddenException;
import net.unicornuniversity.bdij.exceptions.UnknownErrorException;
import net.unicornuniversity.bdij.models.IcoData;
import net.unicornuniversity.bdij.repositories.IcoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IcoDataRepository icoDataRepository;

    @GetMapping("/{ico}")
    public IcoData getIcoData(@PathVariable String ico) throws InvalidIcoException, NotFoundException, UnknownErrorException {

        if (!ico.matches("\\d{8}")) {
            throw new InvalidIcoException("ICO must be an 8-digit number: " + ico);
        }

        Optional<IcoDataEntity> optionalEntity = icoDataRepository.findById(ico);
        if (optionalEntity.isPresent()) {
            return IcoData.from(optionalEntity.get());
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

        try {
            IcoData aresResult = restTemplate.getForObject(url, IcoData.class);
            assert aresResult != null;

            IcoDataEntity entity = IcoDataEntity.from(aresResult);
            icoDataRepository.save(entity);

            return aresResult;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("Data not found for ICO: " + ico);
            } else {
                throw new UnknownErrorException("An error occurred while retrieving data for ICO: " + ico, e);
            }
        }
    }

    @PatchMapping("/{ico}")
    public IcoDataEntity patchIcoData(@PathVariable String ico, HttpServletRequest request) throws IOException    {
        IcoDataEntity entity = getEntityOrThrow(ico);

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

    private IcoDataEntity getEntityOrThrow(String ico) {
        Optional<IcoDataEntity> optionalEntity = icoDataRepository.findById(ico);
        if (optionalEntity.isEmpty()) {
            throw new NotFoundException("Entity not found");
        }

        IcoDataEntity entity = optionalEntity.get();
        if (entity.getDeleted()) {
            throw new OperationForbiddenException("Operation forbidden on deleted entity");
        }

        return entity;
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

    @GetMapping("/search")
    public List<IcoDataEntity> searchByName(@RequestParam String name)
    {
        Pageable limitPageable = PageRequest.of(0, 20);
        List<IcoDataEntity> entities = icoDataRepository.searchByFullName(name, limitPageable);
        return entities;
    }
}