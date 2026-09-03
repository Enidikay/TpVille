package fr.diginamic.mapper;

import fr.diginamic.dto.VilleDto;
import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

    public VilleDto toDto(Ville ville) {
        VilleDto dto = new VilleDto();

        dto.setNom(ville.getNom());
        dto.setPopulation(ville.getPopulation());

        if (ville.getDepartement() != null) {
            dto.setCodeDepartement(ville.getDepartement().getCode());
            dto.setIdDepartement(ville.getDepartement().getId());
        }

        return dto;
    }

    public Ville toBean(VilleDto dto) {
        Ville ville = new Ville();

        ville.setNom(dto.getNom());
        ville.setPopulation(dto.getPopulation());

        return ville;
    }
}