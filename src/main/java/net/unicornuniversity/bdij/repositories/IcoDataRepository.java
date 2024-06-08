package net.unicornuniversity.bdij.repositories;

import net.unicornuniversity.bdij.entities.IcoDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcoDataRepository extends JpaRepository<IcoDataEntity, String> {
}