package net.unicornuniversity.bdij.repositories;

import net.unicornuniversity.bdij.entities.IcoDataEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcoDataRepository extends JpaRepository<IcoDataEntity, String> {
    @Query("SELECT i FROM IcoDataEntity i WHERE LOWER(i.obchodniJmeno) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<IcoDataEntity> searchByFullName(@Param("name") String name, Pageable pageable);
}