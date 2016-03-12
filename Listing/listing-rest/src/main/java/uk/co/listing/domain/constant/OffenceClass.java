package uk.co.listing.domain.constant;

public enum OffenceClass {

    CLASS1("1"), CLASS2("2"), CLASS3("3"), CLASS4("4"), NONE("None");

    private String value;

    private OffenceClass(final String value) {
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

    public static OffenceClass getOffenceClass(final String value) {
        final OffenceClass[] arrray = OffenceClass.values();
        for (final OffenceClass status : arrray) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}
