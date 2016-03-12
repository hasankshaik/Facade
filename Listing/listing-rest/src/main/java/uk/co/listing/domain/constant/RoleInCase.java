package uk.co.listing.domain.constant;

public enum RoleInCase {

    DEFENDANT("defendant"), WITNESS("witness"), CODEFENDANT("codefendant"), ADVOCATES("advocates"), PROSECUTION("prosecution"), DEFENCE("defence");

    private String description;

    private RoleInCase(String description) {
        this.description = description;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

}
