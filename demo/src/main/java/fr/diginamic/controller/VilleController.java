package fr.diginamic.controller;

import fr.diginamic.entities.Ville;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private final VilleService villeService;

    public VilleController(VilleService villeService) {
        this.villeService = villeService;
    }

    @GetMapping
    public List<Ville> listeVilles() {
        return villeService.listeVilles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ville> retrouverVille(@PathVariable int id) throws ExceptionFonctionnelle {
        return ResponseEntity.ok(villeService.retrouverVille(id));
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody Ville ville, BindingResult result) throws ExceptionFonctionnelle {
        if (result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            throw new ExceptionFonctionnelle(errors.get(0).getField()+" "+errors.get(0).getDefaultMessage());
        }
        villeService.ajouterVille(ville);
        return ResponseEntity.status(HttpStatus.OK).body("Ville insérée avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody Ville nouvelleVille, BindingResult result) throws ExceptionFonctionnelle {
        if (result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            throw new ExceptionFonctionnelle(errors.get(0).getField()+" "+errors.get(0).getDefaultMessage());
        }
        villeService.modifierVille(id, nouvelleVille);

        return ResponseEntity.ok("Ville modifiée avec succès");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) throws ExceptionFonctionnelle {

        villeService.supprimerVille(id);

        return ResponseEntity.ok("Ville supprimée avec succès");
    }

    @GetMapping("/recherche/nom")
    public List<Ville> rechercherVilles(@RequestParam String nom) throws ExceptionFonctionnelle {
        return villeService.rechercherParNom(nom);
    }

    @GetMapping("/recherche/population/min")
    public List<Ville> rechercherVillesMin(@RequestParam int min) throws ExceptionFonctionnelle {
        return villeService.rechercherPopulationMin(min);
    }

    @GetMapping("/recherche/population/minmax")
    public List<Ville> rechercherVillesMinMax(@RequestParam int min, @RequestParam int max) throws ExceptionFonctionnelle {
        return villeService.rechercherPopulationMinMax(min, max);
    }
}