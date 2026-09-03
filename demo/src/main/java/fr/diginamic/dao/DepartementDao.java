package fr.diginamic.dao;

import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartementDao {

    @PersistenceContext
    private EntityManager em;

    public List<Departement> extractAll() {
        TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d", Departement.class);
        return query.getResultList();
    }

    public Departement findById(int id){
        return em.find(Departement.class,id);
    }

    public Departement findByCode(String code){
        TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d WHERE d.code = :code", Departement.class);
        query.setParameter("code", code);
        List<Departement> resultats = query.getResultList();

        return resultats.isEmpty() ? null : resultats.getFirst();
    }


    public void inserer(Departement departement) {
        em.persist(departement);
    }

    public void modifier(Departement departement) {
        em.merge(departement);
    }

    public void delete(Departement departement) {
        em.remove(departement);
    }
}
