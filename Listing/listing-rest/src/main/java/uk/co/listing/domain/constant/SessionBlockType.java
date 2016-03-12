package uk.co.listing.domain.constant;

public enum SessionBlockType {

    SHORTWORK("Short Work", "SW"), CASEMANAGEMENT("Case Management", "CM"), TRIAL("Trial", "T"), SENTENCE("Sentence", "S"), APPEALCONVICTION("Appeal against Conviction", "AC"), APPEALSENTENCE(
            "Appeal against Sentence", "AS"), PCM("PCM", "P");

    private String description;
    private String code;

    private SessionBlockType(final String description, final String code) {
        this.description = description;
        this.code = code;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    public static SessionBlockType getSessionBlockType(final String description) {
        final SessionBlockType[] sessionBlockTypeArray = SessionBlockType.values();
        for (final SessionBlockType sessionBlockType : sessionBlockTypeArray) {
            if (sessionBlockType.getDescription().equals(description)) {
                return sessionBlockType;
            }
        }
        return null;
    }
}
