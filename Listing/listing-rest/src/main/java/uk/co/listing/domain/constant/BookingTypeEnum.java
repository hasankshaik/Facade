package uk.co.listing.domain.constant;

public enum BookingTypeEnum {

    PROVISIONAL("Provisional"), CONFIRMED("Confirmed");

    private String description;

    private BookingTypeEnum(String description) {
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
