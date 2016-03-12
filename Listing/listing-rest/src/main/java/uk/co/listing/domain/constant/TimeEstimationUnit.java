package uk.co.listing.domain.constant;

public enum TimeEstimationUnit {

    HOURS("Hours"), DAYS("Days"), WEEKS("Weeks");

    private String value;

    private TimeEstimationUnit(final String value) {
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

    public static TimeEstimationUnit getTimeEstimationUnit(final String value) {
        final TimeEstimationUnit[] timeEstimationUnitArray = TimeEstimationUnit.values();
        for (final TimeEstimationUnit timeEstimationUnit : timeEstimationUnitArray) {
            if (timeEstimationUnit.getValue().equals(value)) {
                return timeEstimationUnit;
            }
        }
        return null;
    }

    public static TimeEstimationUnit getTimeEstimationUnitForCrest(final String value) {
        switch (value) {
        case "H":
            return HOURS;
        case "D":
            return DAYS;
        case "W":
            return WEEKS;
        default:
            return null;
        }
    }
}
