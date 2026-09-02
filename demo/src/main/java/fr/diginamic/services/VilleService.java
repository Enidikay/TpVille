package fr.diginamic.services;

import fr.diginamic.entities.Ville;
import fr.diginamic.exception.ExceptionFonctionnelle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VilleService {

    private List<Ville> villes = new ArrayList<>();

    public VilleService() {
        villes.add(new Ville("Paris", 2161000));
        villes.add(new Ville("Marseille", 873000));
        villes.add(new Ville("Lyon", 522000));
        villes.add(new Ville("Toulouse", 504000));
        villes.add(new Ville("Nice", 342000));
    }

    public List<Ville> listeVilles() {
        return villes;
    }

    public Ville retrouverVille(int id) throws ExceptionFonctionnelle {
        for (Ville ville : villes) {
            if (ville.getId() == id) {
                return ville;
            }
        }

        throw new ExceptionFonctionnelle("La ville n'existe pas");
    }

    public void ajouterVille(Ville ville) throws ExceptionFonctionnelle {

        verifierVille(ville);

        for (Ville v : villes) {
            if (v.getNom().equalsIgnoreCase(ville.getNom())) {
                throw new ExceptionFonctionnelle("La ville existe déjà");
            }
        }

        villes.add(ville);
    }

    public void modifierVille(int id, Ville nouvelleVille) throws ExceptionFonctionnelle {

        verifierVille(nouvelleVille);

        Ville ville = retrouverVille(id);

        ville.setNom(nouvelleVille.getNom());
        ville.setPopulation(nouvelleVille.getPopulation());
    }

    public void supprimerVille(int id) throws ExceptionFonctionnelle {

        Ville ville = retrouverVille(id);

        villes.remove(ville);
    }

    public List<Ville> rechercherParNom(String nom) throws ExceptionFonctionnelle {

        List<Ville> villesTrouvees = new ArrayList<>();

        for (Ville ville : villes) {
            if (ville.getNom().startsWith(nom)) {
                villesTrouvees.add(ville);
            }
        }

        if (villesTrouvees.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville commençant par " + nom + " n’a été trouvée");
        }

        return villesTrouvees;
    }

    public List<Ville> rechercherPopulationMin(int min) throws ExceptionFonctionnelle {

        List<Ville> villesTrouvees = new ArrayList<>();

        for (Ville ville : villes) {
            if (ville.getPopulation() > min) {
                villesTrouvees.add(ville);
            }
        }

        if (villesTrouvees.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n’a une population supérieure à " + min);
        }

        return villesTrouvees;
    }

    public List<Ville> rechercherPopulationMinMax(int min, int max) throws ExceptionFonctionnelle {

        List<Ville> villesTrouvees = new ArrayList<>();

        for (Ville ville : villes) {
            if (ville.getPopulation() > min
                    && ville.getPopulation() < max) {
                villesTrouvees.add(ville);
            }
        }

        if (villesTrouvees.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n’a une population comprise entre " + min + " et " + max);
        }

        return villesTrouvees;
    }

    private void verifierVille(Ville ville) throws ExceptionFonctionnelle {

        if (ville.getPopulation() < 10) {
            throw new ExceptionFonctionnelle("La ville doit avoir au moins 10 habitants");
        }

        if (ville.getNom() == null || ville.getNom().length() < 2) {
            throw new ExceptionFonctionnelle("Le nom de la ville doit contenir au moins 2 lettres");
        }
    }
}