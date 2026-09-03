package fr.diginamic.controller;

import fr.diginamic.entities.Departement;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.services.DepartementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departements")
public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    /**
     * Récupère la liste de tous les départements.
     * @return la liste des départements
     */
    @GetMapping
    public List<Departement> extractDepartements() {
        return departementService.extractDepartements();
    }

    /**
     * Récupère un département à partir de son identifiant.
     * @param id identifiant du département à rechercher
     * @return le département correspondant à l'identifiant fourni
     */
    @GetMapping("/{id}")
    public ResponseEntity<Departement> extractDepartement(@PathVariable int id) {

        Departement departement = departementService.extractDepartementId(id);

        return ResponseEntity.ok(departement);
    }

    /**
     * Insère un nouveau département en base de données.
     * @param departement département à insérer
     * @return un message confirmant le succès de l'insertion
     * @throws ExceptionFonctionnelle si le département existe déjà
     */
    @PostMapping
    public ResponseEntity<String> insertDepartement(@RequestBody Departement departement) throws ExceptionFonctionnelle {

        departementService.insertDepartement(departement);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Département inséré avec succès");
    }

    /**
     * Modifie un département existant à partir de son identifiant.
     * @param id identifiant du département à modifier
     * @param departementModifie données du département modifié
     * @return un message confirmant le succès de la modification
     * @throws ExceptionFonctionnelle si aucun département ne correspond à l'identifiant fourni
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierDepartement(@PathVariable int id, @RequestBody Departement departementModifie) throws ExceptionFonctionnelle {

        departementService.modifierDepartement(id, departementModifie);

        return ResponseEntity.ok("Département modifié avec succès");
    }

    /**
     * Supprime un département à partir de son identifiant.
     * @param id identifiant du département à supprimer
     * @return un message confirmant le succès de la suppression
     * @throws ExceptionFonctionnelle si aucun département ne correspond à l'identifiant fourni
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerDepartement(
            @PathVariable int id)
            throws ExceptionFonctionnelle {

        departementService.supprimerDepartement(id);

        return ResponseEntity.ok("Département supprimé avec succès");
    }
}