package fr.diginamic.repository;

import fr.diginamic.entities.Departement;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartementRepository extends CrudRepository<Departement, Integer> {
    Optional<Departement> findByCode(String code);
}
