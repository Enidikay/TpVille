package fr.diginamic.services;

import fr.diginamic.dao.DepartementDao;
import fr.diginamic.entities.Departement;
import fr.diginamic.exception.ExceptionFonctionnelle;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    private final DepartementDao departementDao;

    public DepartementService(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    /**
     * Récupère la liste de tous les départements.
     *
     * @return la liste de tous les départements
     */
    public List<Departement> extractDepartements() {
        return departementDao.extractAll();
    }

    /**
     * Récupère un département à partir de son identifiant.
     *
     * @param id identifiant du département à rechercher
     * @return le département correspondant à l'identifiant fourni,
     * ou null si aucun département ne correspond
     */
    public Departement extractDepartementId(int id) {
        return departementDao.findById(id);
    }

    /**
     * Récupère un département à partir de son code.
     *
     * @param code code du département à rechercher
     * @return le département correspondant au code fourni,
     * ou null si aucun département ne correspond
     */
    public Departement extractDepartementCode(String code) {
        return departementDao.findByCode(code);
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

        Departement departementExistant = departementDao.findByCode(departement.getCode());

        if (departementExistant != null) {
            throw new ExceptionFonctionnelle("Le département existe déjà");
        }

        departementDao.inserer(departement);
    }

    /**
     * Vérifie que les données du département sont valides.
     *
     * @param departement département à vérifier
     * @throws ExceptionFonctionnelle si le code du département est vide ou null
     */
    private void verifierDepartement(Departement departement)
            throws ExceptionFonctionnelle {

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
    public void modifierDepartement(int id, Departement departementModifie)
            throws ExceptionFonctionnelle {

        Departement departement = departementDao.findById(id);

        if (departement == null) {
            throw new ExceptionFonctionnelle("Le département n'existe pas");
        }

        verifierDepartement(departementModifie);

        departement.setCode(departementModifie.getCode());
        departement.setNom(departementModifie.getNom());

        departementDao.modifier(departement);
    }

    /**
     * Supprime un département à partir de son identifiant.
     *
     * @param id identifiant du département à supprimer
     * @throws ExceptionFonctionnelle si aucun département ne correspond à l'identifiant
     */
    @Transactional
    public void supprimerDepartement(int id)
            throws ExceptionFonctionnelle {

        Departement departement = departementDao.findById(id);

        if (departement == null) {
            throw new ExceptionFonctionnelle("Le département n'existe pas");
        }

        departementDao.delete(departement);
    }
}