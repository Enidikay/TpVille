package fr.diginamic.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity

public class Ville {
    private int id;

    @NotBlank
    @Size(min = 2 , message = "la ville doit avoir minimum 2 carractères")
    private String nom;

    @Min(value = 1, message = "la population doit être de minimum de 1 habitant")
    private int population;

    private static int localId = 0;

    public Ville(String nom, int population) {
        localId++;
        this.id = localId;
        this.nom = nom;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
