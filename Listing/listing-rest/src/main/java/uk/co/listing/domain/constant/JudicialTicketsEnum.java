package uk.co.listing.domain.constant;

import java.util.HashMap;
import java.util.Map;

public enum JudicialTicketsEnum {

    MURDER("Murder"), ATTEMPTEDMURDER("Attempted Murder"), SEXUALOFFENCE("Sexual Offences"), FRAUD("Fraud"), HEALTHANDSAFETY("Health and Safety");

    static final Map<String, JudicialTicketsEnum> judicialTicketsTypeMap = new HashMap<String, JudicialTicketsEnum>();
    static {
        for (final JudicialTicketsEnum judicialTicketsType : JudicialTicketsEnum.values()) {
            judicialTicketsTypeMap.put(judicialTicketsType.getDescription(), judicialTicketsType);
        }
    }

    private String description;

    private JudicialTicketsEnum(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static JudicialTicketsEnum getJudicialTicketsType(final String description) {
        return judicialTicketsTypeMap.get(description);
    }

}
