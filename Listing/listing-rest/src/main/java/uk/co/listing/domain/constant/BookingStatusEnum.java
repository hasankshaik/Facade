package uk.co.listing.domain.constant;

public enum BookingStatusEnum {

    NOTBOOKED("Unscheduled"), BOOKED("Scheduled");

    private final String description;

    private BookingStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static BookingStatusEnum getBookinsStatus(String description) {
        BookingStatusEnum[] bookingStausArray = BookingStatusEnum.values();
        for (BookingStatusEnum bookingStatus : bookingStausArray) {
            if (bookingStatus.getDescription().equals(description)) {
                return bookingStatus;
            }
        }
        return null;
    }

}
