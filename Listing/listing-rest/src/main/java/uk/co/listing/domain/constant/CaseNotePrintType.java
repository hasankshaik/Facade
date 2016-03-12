package uk.co.listing.domain.constant;

public enum CaseNotePrintType {
    PRIORITY("P"), STANDARD("S"), RESTRICTED("R");

    private final String value;

    private CaseNotePrintType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CaseNotePrintType getCaseNotePrintType(final String value) {
        final CaseNotePrintType[] caseNotePrintTypeArray = CaseNotePrintType.values();
        for (final CaseNotePrintType caseNotePrintType : caseNotePrintTypeArray) {
            if (caseNotePrintType.getValue().equals(value)) {
                return caseNotePrintType;
            }
        }
        return null;
    }
}
