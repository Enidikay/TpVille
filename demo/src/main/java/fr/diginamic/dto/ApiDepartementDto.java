package fr.diginamic.dto;

public class ApiDepartementDto {
    private String nom;
    private String code;
    private String codeRegion;


    @Override
    public String toString() {
        return "ApiDepartementDto{" +
                "nom='" + nom + '\'' +
                ", code='" + code + '\'' +
                ", codeRegion='" + codeRegion + '\'' +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeRegion() {
        return codeRegion;
    }

    public void setCodeRegion(String codeRegion) {
        this.codeRegion = codeRegion;
    }
}
