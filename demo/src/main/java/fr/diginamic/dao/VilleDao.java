package fr.diginamic.dao;

import fr.diginamic.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VilleDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Extrait toutes les villes présentes en base de données.
     *
     * @return la liste de toutes les villes
     */
    public List<Ville> extractAll() {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v", Ville.class);

        return query.getResultList();
    }

    /**
     * Recherche une ville à partir de son identifiant.
     *
     * @param id identifiant de la ville recherchée
     * @return la ville correspondant à l'identifiant, ou null si elle n'existe pas
     */
    public Ville findById(int id) {
        return em.find(Ville.class, id);
    }

    /**
     * Insère une nouvelle ville en base de données.
     *
     * @param ville ville à insérer
     */
    public void inserer(Ville ville) {
        em.persist(ville);
    }

    /**
     * Modifie les informations d'une ville existante.
     *
     * @param ville ville contenant les nouvelles informations
     */
    public void modifier(Ville ville) {
        Ville villeExistante = em.find(Ville.class, ville.getId());
        villeExistante.setPopulation(ville.getPopulation());
        villeExistante.setNom(ville.getNom());
    }

    /**
     * Supprime une ville de la base de données.
     *
     * @param ville ville à supprimer
     */
    public void delete(Ville ville) {
        em.remove(ville);
    }

    /**
     * Recherche les villes correspondant exactement au nom fourni.
     *
     * @param nom nom de la ville recherché
     * @return la liste des villes correspondant au nom
     */
    public List<Ville> rechercherParNom(String nom) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class);
        query.setParameter("nom", nom);

        return query.getResultList();
    }

    /**
     * Recherche les villes dont la population est supérieure au minimum fourni.
     *
     * @param min population minimale
     * @return la liste des villes dont la population est supérieure à min
     */
    public List<Ville> rechercherPopulationMin(int min) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.population > :min", Ville.class);
        query.setParameter("min", min);

        return query.getResultList();
    }

    /**
     * Recherche les villes dont la population est comprise entre min et max.
     *
     * @param min population minimale
     * @param max population maximale
     * @return la liste des villes correspondant à l'intervalle de population
     */
    public List<Ville> rechercherPopulationMinMax(int min, int max) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.population > :min AND v.population < :max", Ville.class);

        query.setParameter("min", min);
        query.setParameter("max", max);

        return query.getResultList();
    }

    /**
     * Recherche les villes dont le nom commence par le suffixe fourni.
     *
     * @param suffixe début du nom de la ville recherché
     * @return la liste des villes correspondant au suffixe
     */
    public List<Ville> rechercheNameSuffixe(String suffixe) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.nom LIKE :suffixe", Ville.class);
        query.setParameter("suffixe", suffixe + "%");

        return query.getResultList();
    }

    public List<Ville> rechercherPlusGrandesVilles(int idDepartement, int n) {
        TypedQuery<Ville> query =  em.createQuery("SELECT v FROM Ville v " + "WHERE v.departement.id = :idDepartement " + "ORDER BY v.population DESC", Ville.class).setMaxResults(n);
        query.setParameter("idDepartement", idDepartement);
        return query.getResultList();
    }

    public List<Ville> rechercherPopulationMinMaxDepartement(int min, int max, int idDepartement) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v " + "WHERE v.population BETWEEN :min AND :max " + "AND v.departement.id = :idDepartement", Ville.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        query .setParameter("idDepartement", idDepartement);
        return query.getResultList();
    }
}