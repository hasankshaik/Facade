package uk.co.listing.domain.constant;

public enum CustodyStatus {

    BAIL("Bail"), CUSTODY("Custody"), INCARE("In Care"), NOTAPPLICABLE("Not Applicable");

    private String value;

    private CustodyStatus(final String value) {
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

    public static CustodyStatus getCustodyStatus(final String value) {
        final CustodyStatus[] arrray = CustodyStatus.values();
        for (final CustodyStatus status : arrray) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    public static CustodyStatus getCustodyStatusForCrest(final String value) {
        switch (value) {
        case "B":
            return BAIL;
        case "C":
            return CUSTODY;
        case "J":
            return INCARE;
        case "N":
            return NOTAPPLICABLE;
        default:
            return null;
        }
    }
}
