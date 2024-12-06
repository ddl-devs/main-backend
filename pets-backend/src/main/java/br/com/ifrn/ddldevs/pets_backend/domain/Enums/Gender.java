package br.com.ifrn.ddldevs.pets_backend.domain.Enums;

public enum Gender {
    MALE("Macho"),
    FEMALE("Fêmea");

    private String description;

    private Gender(String description) { this.description = description; }

    public String getDescription() { return description; }
}
