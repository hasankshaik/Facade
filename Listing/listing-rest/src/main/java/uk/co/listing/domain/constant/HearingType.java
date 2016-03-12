package uk.co.listing.domain.constant;

public enum HearingType {

    PRETRIAL("Pre trial"), PRELIMINARY("Preliminary hearing"), TRIAL("Trial"), VEREDICT("VEREDICT"), APPEAL("Appleal"), PCM("Plea and Case Management");

    private String description;

    private HearingType(final String description) {
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

    public static HearingType getHearingType(final String value) {
        final HearingType[] hearingTypeArray = HearingType.values();
        for (final HearingType hearingType : hearingTypeArray) {
            if (hearingType.getDescription().equals(value)) {
                return hearingType;
            }
        }
        return null;
    }

}
