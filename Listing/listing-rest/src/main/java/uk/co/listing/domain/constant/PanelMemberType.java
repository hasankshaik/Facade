package uk.co.listing.domain.constant;

import java.util.HashMap;
import java.util.Map;

public enum PanelMemberType {

    JUDGE("judge"), MAGISTRATE("magistrate");

    static final Map<String, PanelMemberType> panelMemberTypeMap = new HashMap<String, PanelMemberType>();
    static {
        for (PanelMemberType panelMemberType : PanelMemberType.values()) {
            panelMemberTypeMap.put(panelMemberType.getDescription(), panelMemberType);
        }
    }
    
    private String description;

    private PanelMemberType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PanelMemberType getPanelMemberType(String description) {
        return panelMemberTypeMap.get(description);
    }

}
