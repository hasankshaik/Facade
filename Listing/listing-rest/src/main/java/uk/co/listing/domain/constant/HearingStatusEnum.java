package uk.co.listing.domain.constant;

public enum HearingStatusEnum {

    PENDING("Pending"), PREPARED("Prepared"), COMPLETE("Complete");

    private final String description;

    private HearingStatusEnum(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static HearingStatusEnum getHearingStatus(final String description) {
        final HearingStatusEnum[] hearingStausArray = HearingStatusEnum.values();
        for (final HearingStatusEnum hearingStatus : hearingStausArray) {
            if (hearingStatus.getDescription().equals(description)) {
                return hearingStatus;
            }
        }
        return null;
    }

}
