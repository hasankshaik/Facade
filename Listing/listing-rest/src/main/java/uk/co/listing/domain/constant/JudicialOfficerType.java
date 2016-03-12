package uk.co.listing.domain.constant;

import java.util.HashMap;
import java.util.Map;

public enum JudicialOfficerType {

    HIGH("High Court"), CIRCUIT("Circuit"), RECORDER("Recorder"), DEPUTY("Deputy Circuit Judge");

    static final Map<String, JudicialOfficerType> judicialOfficerTypeMap = new HashMap<String, JudicialOfficerType>();
    static {
        for (JudicialOfficerType judicialOfficerType : JudicialOfficerType.values()) {
            judicialOfficerTypeMap.put(judicialOfficerType.getDescription(), judicialOfficerType);
        }
    }
    
    private String description;

    private JudicialOfficerType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static JudicialOfficerType getJudicialOfficerType(String description) {
        return judicialOfficerTypeMap.get(description);
    }

}
