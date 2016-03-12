package uk.co.listing.domain.constant;

public enum CaseNoteType {
    HIGHLIGHT("H"), CASE_NOTE("C"), LO_DIARY("D");

    private final String value;

    private CaseNoteType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CaseNoteType getCaseNoteType(final String value) {
        final CaseNoteType[] caseNoteTypeArray = CaseNoteType.values();
        for (final CaseNoteType caseNoteType : caseNoteTypeArray) {
            if (caseNoteType.getValue().equals(value)) {
                return caseNoteType;
            }
        }
        return null;
    }
}
