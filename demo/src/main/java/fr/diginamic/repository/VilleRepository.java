package fr.diginamic.repository;

import fr.diginamic.entities.Ville;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// findBy + propriété + opérateur + OrderBy + propriété + tri

public interface VilleRepository extends CrudRepository<Ville, Integer> {

    /**
     * Recherche les villes dont le nom commence par le texte fourni.
     *
     * @param nom début du nom de la ville recherché
     * @return la liste des villes correspondant au nom fourni
     */
    List<Ville> findByNomStartingWith(String nom);

    /**
     * Recherche les villes dont la population est supérieure à un minimum.
     * Les villes sont triées par population décroissante.
     *
     * @param min population minimale
     * @return la liste des villes correspondant au critère
     */
    List<Ville> findByPopulationGreaterThanOrderByPopulationDesc(int min);

    /**
     * Recherche les villes dont la population est comprise entre un minimum
     * et un maximum.
     * Les villes sont triées par population décroissante.
     *
     * @param min population minimale
     * @param max population maximale
     * @return la liste des villes correspondant à l'intervalle
     */
    List<Ville> findByPopulationBetweenOrderByPopulationDesc(int min, int max);


    /**
     * Recherche les villes d'un département dont la population est supérieure
     * à un minimum et inférieure à un maximum.
     * Les villes sont triées par population décroissante.
     *
     * @param idDepartement identifiant du département concerné
     * @param min population minimale
     * @param max population maximale
     * @return la liste des villes correspondant aux critères
     */
    List<Ville> findByDepartementIdAndPopulationGreaterThanAndPopulationLessThanOrderByPopulationDesc(
            int idDepartement,
            int min,
            int max
    );

    /**
     * Recherche les villes d'un département, triées par population décroissante.
     * Le paramètre Pageable permet de limiter le nombre de villes retournées.
     *
     * @param idDepartement identifiant du département concerné
     * @param pageable paramètres de pagination permettant notamment de définir
     *                 le nombre de villes à retourner
     * @return la liste des villes du département triées par population décroissante
     */
    @Query("""
            SELECT v FROM Ville v
            WHERE v.departement.id = :idDepartement
            ORDER BY v.population DESC
            """)
    List<Ville> findByDepartement(
            @Param("idDepartement") int idDepartement,
            Pageable pageable
    );
}