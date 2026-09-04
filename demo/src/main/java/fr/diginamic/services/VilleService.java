package fr.diginamic.services;

import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.repository.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VilleService {

    private final VilleRepository villeRepository;
    private final DepartementService departementService;

    public VilleService(VilleRepository villeRepository, DepartementService departementService) {
        this.villeRepository = villeRepository;
        this.departementService = departementService;
    }

    /**
     * Extrait toutes les villes.
     * @return la liste de toutes les villes disponibles
     */
    public List<Ville> extractVilles() {
        List<Ville> villes = new ArrayList<>();
        villeRepository.findAll().forEach(villes::add);
        return villes;
    }

    /**
     * Extrait une ville à partir de son identifiant.
     * @param idVille identifiant de la ville à rechercher
     * @return la ville correspondant à l'identifiant fourni
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'identifiant fourni
     */
    public Ville extractVille(int idVille) throws ExceptionFonctionnelle {

        Optional<Ville> ville = villeRepository.findById(idVille);

        if (ville.isEmpty()) {
            throw new ExceptionFonctionnelle("La ville n'existe pas");
        }

        return ville.get();
    }

    /**
     * Extrait les villes dont le nom commence par le suffixe fourni.
     * @param suffixe début du nom de la ville à rechercher
     * @return la liste des villes correspondant au suffixe fourni
     * @throws ExceptionFonctionnelle si aucune ville ne correspond au suffixe fourni
     */
    public List<Ville> extractVilles(String suffixe) throws ExceptionFonctionnelle {
        List<Ville> villes = villeRepository.findByNomStartingWith(suffixe);

        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville avec le suffixe " + suffixe + " n'a été trouvée");
        }

        return villes;
    }

    /**
     * Insère une nouvelle ville en base de données avec son département associé.
     *
     * @param ville ville à ajouter
     * @param codeDepartement code du département associé à la ville
     * @param idDepartement identifiant du département associé à la ville
     * @throws ExceptionFonctionnelle si la ville ou le département est invalide
     */
    @Transactional
    public void insertVille(Ville ville, String codeDepartement, Integer idDepartement) throws ExceptionFonctionnelle {

        verifierVille(ville);
        Departement departement = null;

        if (idDepartement == null && (codeDepartement == null || codeDepartement.isBlank())) {
            throw new ExceptionFonctionnelle("Le code ou l'identifiant du département est obligatoire");
        }

        if (idDepartement != null) {
            departement = departementService.extractDepartementId(idDepartement);

            if (departement == null) {
                throw new ExceptionFonctionnelle("Département inconnu");
            }

        } else if (!codeDepartement.isBlank()) {
            departement = departementService.extractDepartementCode(codeDepartement);

            if (departement == null) {
                departement = new Departement();
                departement.setCode(codeDepartement);
                departementService.insertDepartement(departement);
            }
        }

        ville.setDepartement(departement);

        List<Ville> villes = extractVilles();

        for (Ville v : villes) {
            if (v.getNom().equalsIgnoreCase(ville.getNom())) {
                throw new ExceptionFonctionnelle("La ville existe déjà");
            }
        }

        villeRepository.save(ville);
    }

    /**
     * Modifie les informations d'une ville existante.
     *
     * @param idVille       identifiant de la ville à modifier
     * @param villeModifiee nouvelles informations de la ville
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'identifiant fourni
     */
    @Transactional
    public void modifierVille(int idVille, Ville villeModifiee) throws ExceptionFonctionnelle {
        verifierVille(villeModifiee);

        Ville ville = extractVille(idVille);
        ville.setNom(villeModifiee.getNom());
        ville.setPopulation(villeModifiee.getPopulation());

        villeRepository.save(ville);
    }

    /**
     * Supprime une ville à partir de son identifiant.
     *
     * @param idVille identifiant de la ville à supprimer
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'identifiant fourni
     */
    @Transactional
    public void supprimerVille(int idVille) throws ExceptionFonctionnelle {
        Ville ville = extractVille(idVille);
        villeRepository.delete(ville);
    }

    /**
     * Extrait les villes dont la population est supérieure au minimum fourni.
     * @param min nombre minimum d'habitants
     * @return la liste des villes dont la population est supérieure à min
     * @throws ExceptionFonctionnelle si aucune ville ne correspond au critère de population
     */
    public List<Ville> extractVilles(int min) throws ExceptionFonctionnelle {

        List<Ville> villesTrouves = villeRepository.findByPopulationGreaterThanOrderByPopulationDesc(min);

        if (villesTrouves.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n'a une population supérieure à " + min);
        }

        return villesTrouves;
    }

    /**
     * Extrait les villes dont la population est comprise entre min et max.
     * @param min population minimale
     * @param max population maximale
     * @return la liste des villes correspondant à l'intervalle de population
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'intervalle fourni
     */
    public List<Ville> extractVilles(int min, int max) throws ExceptionFonctionnelle {

        List<Ville> villesTrouves = villeRepository.findByPopulationBetweenOrderByPopulationDesc(min, max);

        if (villesTrouves.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n'a une population comprise entre " + min + " et " + max);
        }

        return villesTrouves;
    }

    /**
     * Vérifie que les informations d'une ville sont valides.
     * @param ville ville à vérifier
     * @throws ExceptionFonctionnelle si les informations de la ville sont invalides
     */
    private void verifierVille(Ville ville) throws ExceptionFonctionnelle {

        if (ville.getPopulation() < 10) {
            throw new ExceptionFonctionnelle("La ville doit avoir au moins 10 habitants");
        }

        if (ville.getNom() == null || ville.getNom().length() < 2) {
            throw new ExceptionFonctionnelle("Le nom de la ville doit contenir au moins 2 lettres");
        }
    }

    public List<Ville> extractPlusGrandesVilles(int idDepartement, int n) throws ExceptionFonctionnelle {

        List<Ville> villes = villeRepository.findByDepartement(idDepartement, PageRequest.of(0, n));

        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville trouvée pour ce département");
        }

        return villes;
    }

    public List<Ville> extractVilles(int min, int max, int idDepartement) throws ExceptionFonctionnelle {

        List<Ville> villes = villeRepository.findByDepartementIdAndPopulationGreaterThanAndPopulationLessThanOrderByPopulationDesc(idDepartement,min,max);

        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville ne correspond aux critères");
        }

        return villes;
    }
}