package uk.co.listing.domain.constant;

public enum TicketingRequirement {

    NON("None"), ATT("Attempted Murder"), FRA("Fraud"), MUR("Murder"), RAP("Rape");

    private String value;

    private TicketingRequirement(final String value) {
        this.value = value;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getValue() {
        return value;
    }

    public static TicketingRequirement getTicketingRequirement(final String value) {
        final TicketingRequirement[] ticketingRequirementArray = TicketingRequirement.values();
        for (final TicketingRequirement ticketingRequirement : ticketingRequirementArray) {
            if (ticketingRequirement.getValue().equals(value)) {
                return ticketingRequirement;
            }
        }
        return null;
    }

}
