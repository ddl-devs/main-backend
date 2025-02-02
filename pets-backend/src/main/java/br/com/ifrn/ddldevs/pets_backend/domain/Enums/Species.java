package br.com.ifrn.ddldevs.pets_backend.domain.Enums;

public enum Species {
        DOG("Cachorro"),
        CAT("Gato");

        private String description;

        private Species(String description) { this.description = description; }

        public String getDescription() { return description; }
}
