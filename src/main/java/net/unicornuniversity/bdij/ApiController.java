package net.unicornuniversity.bdij;

import net.unicornuniversity.bdij.dtos.UpdateIcoDto;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private IcoDataRepository icoDataRepository;

    @GetMapping("/{ico}")
    public IcoData getIcoData(@PathVariable String ico, @RequestParam(required = false, defaultValue = "false") boolean forceLoad) throws InvalidIcoException, NotFoundException, UnknownErrorException {
        IcoDataEntity entity = getIcoDataEntityOrThrow(ico, forceLoad);
        return IcoData.from(entity);
    }

    @PostMapping()
    public IcoData saveIcoData(@RequestBody IcoData icoData) throws InvalidIcoException, UnknownErrorException {

        Optional<IcoDataEntity> optionalEntity = icoDataRepository.findById(icoData.getIco());
        if (optionalEntity.isPresent()) {
            throw new OperationForbiddenException("ICO: " + icoData.getIco() + " already exists");
        }

        String ico = icoData.getIco();
        if (!ico.matches("\\d{8}")) {
            throw new InvalidIcoException("ICO must be an 8-digit number: " + ico);
        }

        // ideally we would validate other fields here as well

        try {
            IcoDataEntity entity = IcoDataEntity.from(icoData);
            icoDataRepository.save(entity);
            return icoData;
        } catch (Exception e) {
            throw new UnknownErrorException("An error occurred while saving data for ICO: " + ico, e);
        }
    }

    @DeleteMapping("/{ico}")
    public void deleteIcoData(@PathVariable String ico) throws InvalidIcoException, NotFoundException, UnknownErrorException, OperationForbiddenException {

        IcoDataEntity entity = getIcoDataEntityOrThrow(ico);

        entity.setDeleted(true);
        icoDataRepository.save(entity);
    }

    @PatchMapping("/{ico}")
    public IcoData patchIcoData(@PathVariable String ico, @RequestBody UpdateIcoDto body) throws InvalidIcoException, NotFoundException, UnknownErrorException {
        try {
            IcoDataEntity icoDataEntity = getIcoDataEntityOrThrow(ico);

            String obchodniJmeno = body.getObchodniJmeno();
            if (obchodniJmeno != null) {
                icoDataEntity.setObchodniJmeno(obchodniJmeno);
            }

            String financniUrad = body.getFinancniUrad();
            if (financniUrad != null) {
                icoDataEntity.setFinancniUrad(financniUrad);
            }

            String radekAdresy1 = body.getRadekAdresy1();
            if (radekAdresy1 != null) {
                icoDataEntity.setRadekAdresy1(radekAdresy1);
            }

            String radekAdresy2 = body.getRadekAdresy2();
            if (radekAdresy2 != null) {
                icoDataEntity.setRadekAdresy2(radekAdresy2);
            }

            String radekAdresy3 = body.getRadekAdresy3();
            if (radekAdresy3 != null) {
                icoDataEntity.setRadekAdresy3(radekAdresy3);
            }

            icoDataRepository.save(icoDataEntity);

            return IcoData.from(icoDataEntity);
        } catch (Exception e) {
            throw new UnknownErrorException("An error occurred while updating data for ICO: " + ico, e);
        }
    }

    @GetMapping("/search")
    public List<IcoDataEntity> searchByName(@RequestParam String name) throws UnknownErrorException {
        try {
            Pageable limitPageable = PageRequest.of(0, 20);

            return icoDataRepository.searchByFullName(name, limitPageable);
        } catch (Exception e) {
            throw new UnknownErrorException("An error occurred while searching for:" + name, e);
        }
    }
    private IcoDataEntity getIcoDataEntityOrThrow(String ico) throws InvalidIcoException, NotFoundException, UnknownErrorException {
        return getIcoDataEntityOrThrow(ico, false);
    }

    private IcoDataEntity getIcoDataEntityOrThrow(String ico, boolean forceLoad) throws InvalidIcoException, NotFoundException, UnknownErrorException {
        if (!ico.matches("\\d{8}")) {
            throw new InvalidIcoException("ICO must be an 8-digit number: " + ico);
        }

        if (!forceLoad) {
            Optional<IcoDataEntity> optionalEntity = icoDataRepository.findById(ico);
            if (optionalEntity.isPresent()) {
                if (optionalEntity.get().getDeleted()) {
                    throw new NotFoundException("Data not found for ICO: " + ico);
                }

                return optionalEntity.get();
            }
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ares.gov.cz/ekonomicke-subjekty-v-be/rest/ekonomicke-subjekty/" + ico;

        try {
            IcoData aresResult = restTemplate.getForObject(url, IcoData.class);
            assert aresResult != null;

            IcoDataEntity entity = IcoDataEntity.from(aresResult);
            icoDataRepository.save(entity);

            return entity;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("Data not found for ICO: " + ico);
            } else {
                throw new UnknownErrorException("An error occurred while retrieving data for ICO: " + ico, e);
            }
        }
    }
}