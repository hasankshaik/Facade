package uk.co.listing.domain.constant;

public enum ProcessingStatus {

    AWAITING("Awaiting"), PROCESSING("Processing"), PROCESSED("Processed"), ERROR("Error");

    private final String description;

    private ProcessingStatus(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ProcessingStatus getBookinsStatus(final String description) {
        final ProcessingStatus[] bookingStausArray = ProcessingStatus.values();
        for (final ProcessingStatus bookingStatus : bookingStausArray) {
            if (bookingStatus.getDescription().equals(description)) {
                return bookingStatus;
            }
        }
        return null;
    }

}
