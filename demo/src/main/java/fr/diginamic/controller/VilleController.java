package fr.diginamic.controller;

import fr.diginamic.dto.VilleDto;
import fr.diginamic.entities.Ville;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.mapper.VilleMapper;
import fr.diginamic.services.VilleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private final VilleService villeService;
    private final VilleMapper villeMapper;

    public VilleController(VilleService villeService, VilleMapper villeMapper) {
        this.villeService = villeService;
        this.villeMapper = villeMapper;
    }

    /**
     * Récupère la liste de toutes les villes.
     * @return la liste des villes
     */
    @GetMapping
    public List<VilleDto> extractVilles() {
        List<Ville> villes = villeService.extractVilles();
        List<VilleDto> dtos = new ArrayList<>();

        for (Ville ville : villes) {
            dtos.add(villeMapper.toDto(ville));
        }

        return dtos;
    }

    /**
     * Récupère une ville à partir de son identifiant.
     * @param id identifiant de la ville à rechercher
     * @return la ville correspondant à l'identifiant fourni
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'identifiant fourni
     */
    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> extractVille(@PathVariable int id) throws ExceptionFonctionnelle {
        Ville ville = villeService.extractVille(id);
        VilleDto dto = villeMapper.toDto(ville);

        return ResponseEntity.ok(dto);
    }

    /**
     * Récupère les villes dont le nom commence par le suffixe fourni.
     * @param suffixe début du nom de la ville à rechercher
     * @return la liste des villes correspondant au suffixe fourni
     * @throws ExceptionFonctionnelle si aucune ville ne correspond au suffixe fourni
     */
    @GetMapping("/recherche/nom")
    public List<VilleDto> extractVilles(@RequestParam String suffixe) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.extractVilles(suffixe);
        List<VilleDto> dtos = new ArrayList<>();

        for(Ville ville : villes){
            dtos.add(villeMapper.toDto(ville));
        }

        return dtos;
    }

    /**
     * Récupère les villes dont la population est supérieure au minimum fourni.
     * @param min nombre minimum d'habitants
     * @return la liste des villes dont la population est supérieure à min
     * @throws ExceptionFonctionnelle si aucune ville ne correspond au critère de population
     */
    @GetMapping("/recherche/population/min")
    public List<VilleDto> extractVilles(@RequestParam int min) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.extractVilles(min);
        List<VilleDto> dtos = new ArrayList<>();

        for(Ville ville : villes){
            dtos.add(villeMapper.toDto(ville));
        }

        return dtos;
    }

    /**
     * Récupère les villes dont la population est comprise entre min et max.
     * @param min population minimale
     * @param max population maximale
     * @return la liste des villes dont la population est comprise entre min et max
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'intervalle fourni
     */
    @GetMapping("/recherche/population/minmax")
    public List<VilleDto> extractVilles(@RequestParam int min, @RequestParam int max) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.extractVilles(min, max);
        List<VilleDto> dtos = new ArrayList<>();

        for(Ville ville : villes){
            dtos.add(villeMapper.toDto(ville));
        }

        return dtos;
    }

    /**
     * Insère une nouvelle ville en base de données.
     * @param ville ville à insérer
     * @return un message confirmant le succès de l'insertion
     * @throws ExceptionFonctionnelle si la ville existe déjà dans la base de données
     */
    @PostMapping
    public ResponseEntity<String> insertVille(@Valid @RequestBody VilleDto ville) throws ExceptionFonctionnelle {
        Ville villeBean = villeMapper.toBean(ville);
        villeService.insertVille(villeBean,ville.getCodeDepartement(), ville.getIdDepartement());
        return ResponseEntity.status(HttpStatus.OK).body("Ville insérée avec succès");
    }

    /**
     * Modifie une ville existante à partir de son identifiant.
     * @param id identifiant de la ville à modifier
     * @param villeModifiee données de la ville modifiée
     * @return un message confirmant le succès de la modification
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'identifiant fourni
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody VilleDto villeModifiee) throws ExceptionFonctionnelle {

        Ville ville = villeMapper.toBean(villeModifiee);
        villeService.modifierVille(id, ville);
        return ResponseEntity.ok("Ville modifiée avec succès");
    }

    /**
     * Supprime une ville à partir de son identifiant.
     * @param id identifiant de la ville à supprimer
     * @return un message confirmant le succès de la suppression
     * @throws ExceptionFonctionnelle si aucune ville ne correspond à l'identifiant fourni
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) throws ExceptionFonctionnelle {
        villeService.supprimerVille(id);
        return ResponseEntity.ok("Ville supprimée avec succès");
    }


    /**
     * Récupère les N plus grandes villes d'un département.
     *
     * @param idDepartement identifiant du département
     * @param n nombre de villes à récupérer
     * @return la liste des N plus grandes villes du département
     * @throws ExceptionFonctionnelle si aucune ville ne correspond au département
     */
    @GetMapping("/recherche/departement/grandes")
    public List<VilleDto> extractPlusGrandesVilles(@RequestParam int idDepartement, @RequestParam int n) throws ExceptionFonctionnelle {

        List<Ville> villes = villeService.extractPlusGrandesVilles(idDepartement, n);

        List<VilleDto> dtos = new ArrayList<>();

        for (Ville ville : villes) {
            dtos.add(villeMapper.toDto(ville));
        }

        return dtos;
    }

    /**
     * Récupère les villes d'un département dont la population
     * est comprise entre un minimum et un maximum.
     *
     * @param min population minimale
     * @param max population maximale
     * @param idDepartement identifiant du département
     * @return la liste des villes correspondant aux critères
     * @throws ExceptionFonctionnelle si aucune ville ne correspond aux critères
     */
    @GetMapping("/recherche/departement/population")
    public List<VilleDto> extractVilles(@RequestParam int min, @RequestParam int max, @RequestParam int idDepartement) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.extractVilles(min, max, idDepartement);
        List<VilleDto> dtos = new ArrayList<>();

        for (Ville ville : villes) {
            dtos.add(villeMapper.toDto(ville));
        }

        return dtos;
    }

    /**
     * Récupère les villes à partir d'un min donné et les regroupe dans un fichier csv
     * @param min population minimale
     * @param response fichier vierge à éditer dans la méthode
     * @throws IOException si l'opération a échoué
     * @throws ExceptionFonctionnelle si on n'arrive pas à extraire les données des villes
     */
    @GetMapping("/export/csv")
    public void ficheVille( @RequestParam int min, HttpServletResponse response) throws IOException, ExceptionFonctionnelle {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"villes.csv\"");

        List<Ville> villes = villeService.extractVilles(min);

        //le writer sert à écrire dans le document
        PrintWriter writer = response.getWriter();

        writer.println("Nom;Population;Code departement;Nom departement");

        for (Ville ville : villes) {
            writer.println(ville.getNom() + ";" + ville.getPopulation() + ";" + ville.getDepartement().getCode() + ";" + ville.getDepartement().getNom());
        }

        writer.flush();
    }








}