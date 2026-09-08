package fr.diginamic.services;

import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.repository.DepartementRepository;
import fr.diginamic.repository.VilleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;



@SpringBootTest
@ActiveProfiles("test")
public class VilleServiceTest {

    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private VilleRepository villeRepository;

    @Test
    public void testVilleExisteDeja() {

        Departement departement = new Departement();
        departement.setCode("75");
        departement.setNom("Paris");

        departementRepository.save(departement);

        Ville premiereVille = new Ville();
        premiereVille.setNom("Paris");
        premiereVille.setPopulation(10000);
        premiereVille.setDepartement(departement);

        villeRepository.save(premiereVille);

        Ville deuxiemeVille = new Ville();
        deuxiemeVille.setNom("Paris");
        deuxiemeVille.setPopulation(20000);

        assertThrows(
                ExceptionFonctionnelle.class,
                () -> villeService.insertVille(deuxiemeVille, "75", null)
        );
    }

    @Test
    public void testAjouterVille() throws ExceptionFonctionnelle {

        Departement departement = new Departement();
        departement.setCode("13");
        departement.setNom("Bouches-du-Rhône");
        departementRepository.save(departement);

        Ville ville = new Ville();
        ville.setNom("Marseille");
        ville.setPopulation(870000);

        villeService.insertVille(ville, "13", null);

        List<Ville> villes = villeService.extractVilles();

        assertTrue(villes.stream()
                .anyMatch(v -> v.getNom().equals("Marseille")));
    }

    @Test
    public void testModifierVille() throws ExceptionFonctionnelle {

        Departement departement = new Departement();
        departement.setCode("33");
        departement.setNom("Gironde");
        departementRepository.save(departement);

        Ville ville = new Ville();
        ville.setNom("Bordeaux");
        ville.setPopulation(250000);
        ville.setDepartement(departement);
        villeRepository.save(ville);

        Ville villeModifiee = new Ville();
        villeModifiee.setNom("Bordeaux");
        villeModifiee.setPopulation(300000);

        villeService.modifierVille(ville.getId(), villeModifiee);

        Ville villeEnBase = villeRepository.findById(ville.getId()).get();

        assertEquals(300000, villeEnBase.getPopulation());
    }
    @Test
    public void testSupprimerVille() throws ExceptionFonctionnelle {

        Departement departement = new Departement();
        departement.setCode("31");
        departement.setNom("Haute-Garonne");
        departementRepository.save(departement);

        Ville ville = new Ville();
        ville.setNom("Toulouse");
        ville.setPopulation(500000);
        ville.setDepartement(departement);
        villeRepository.save(ville);

        int idVille = ville.getId();

        villeService.supprimerVille(idVille);

        assertTrue(villeRepository.findById(idVille).isEmpty());
    }
}