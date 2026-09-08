package fr.diginamic.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.dto.ApiDepartementDto;
import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.repository.DepartementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    private final DepartementRepository departementRepository;

    @Value("${application.init}")
    private boolean init;


    public DepartementService(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    /**
     * Récupère la liste de tous les départements.
     *
     * @return la liste de tous les départements
     */
    public List<Departement> extractDepartements() {

        List<Departement> departements = new ArrayList<>();

        departementRepository.findAll().forEach(departements::add);

        return departements;
    }

    /**
     * Récupère un département à partir de son identifiant.
     *
     * @param id identifiant du département à rechercher
     * @return le département correspondant à l'identifiant fourni,
     * ou null si aucun département ne correspond
     */
    public Departement extractDepartementId(int id) {
        Optional<Departement> departement = departementRepository.findById(id);

        return departement.orElse(null);
    }

    /**
     * Récupère un département à partir de son code.
     *
     * @param code code du département à rechercher
     * @return le département correspondant au code fourni,
     * ou null si aucun département ne correspond
     */
    public Departement extractDepartementCode(String code) {

        Optional<Departement> departement = departementRepository.findByCode(code);

        return departement.orElse(null);

    }

    /**
     * Insère un nouveau département en base de données.
     *
     * @param departement département à insérer
     * @throws ExceptionFonctionnelle si le code du département est invalide
     * ou si le département existe déjà
     */
    @Transactional
    public void insertDepartement(Departement departement) throws ExceptionFonctionnelle {

        verifierDepartement(departement);

        Optional<Departement> departementExistant =
                departementRepository.findByCode(departement.getCode());

        if (departementExistant.isPresent()) {
            throw new ExceptionFonctionnelle("Le département existe déjà");
        }

        departementRepository.save(departement);
    }

    /**
     * Vérifie que les données du département sont valides.
     *
     * @param departement département à vérifier
     * @throws ExceptionFonctionnelle si le code du département est vide ou null
     */
    private void verifierDepartement(Departement departement) throws ExceptionFonctionnelle {

        if (departement.getCode() == null || departement.getCode().isBlank()) {
            throw new ExceptionFonctionnelle("Le code du département est obligatoire");
        }
    }

    /**
     * Modifie un département existant à partir de son identifiant.
     *
     * @param id identifiant du département à modifier
     * @param departementModifie nouvelles données du département
     * @throws ExceptionFonctionnelle si aucun département ne correspond à l'identifiant
     * ou si les données du département sont invalides
     */
    @Transactional
    public void modifierDepartement(int id, Departement departementModifie) throws ExceptionFonctionnelle {

        Departement departement = extractDepartementId(id);

        if (departement == null) {
            throw new ExceptionFonctionnelle(
                    "Le département n'existe pas"
            );
        }

        verifierDepartement(departementModifie);

        departement.setCode(departementModifie.getCode());
        departement.setNom(departementModifie.getNom());

        departementRepository.save(departement);
    }

    /**
     * Supprime un département à partir de son identifiant.
     *
     * @param id identifiant du département à supprimer
     * @throws ExceptionFonctionnelle si aucun département ne correspond à l'identifiant
     */
    @Transactional
    public void supprimerDepartement(int id) throws ExceptionFonctionnelle {

        Departement departement = extractDepartementId(id);

        if (departement == null) {
            throw new ExceptionFonctionnelle("Le département n'existe pas");
        }

        departementRepository.delete(departement);
    }


    @PostConstruct
    public void initData() {

        if (!init) {
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiDepartementDto[]> response = restTemplate.getForEntity("https://geo.api.gouv.fr/departements", ApiDepartementDto[].class);

        ApiDepartementDto[] departements = response.getBody();

        for (ApiDepartementDto departementDto : departements) {

            String code = departementDto.getCode();
            String nom = departementDto.getNom();

            Departement departement = extractDepartementCode(code);

            if (departement != null) {
                departement.setNom(nom);
                departementRepository.save(departement);
            }
        }
    }


}