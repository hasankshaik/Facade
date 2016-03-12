package uk.co.listing.domain.constant;

public enum ReleaseDecision {

    AR("Any Recorder"), CJ("Circuit Judge"), HCJ("High Court Judge"), HCJ_RJ("High Court Judge or Resident Judge"), NJ("Named Judge"), R_QC("Recorder QC");

    private String value;

    private ReleaseDecision(final String value) {
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

    public static ReleaseDecision getReleaseDecision(final String value) {
        final ReleaseDecision[] arrray = ReleaseDecision.values();
        for (final ReleaseDecision status : arrray) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}
