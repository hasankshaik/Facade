package uk.co.listing.rest.response;

import java.util.HashSet;
import java.util.Set;

public class JudicialOfficerWeb extends RestResponse {

    private String fullname;
    private String type;
    private Boolean isQC;
    private Boolean isResident;
    private String qc;
    private String resident;
    private final Set<String> judicialTickets = new HashSet<>();

    public JudicialOfficerWeb() {
        super();
    }

    public JudicialOfficerWeb(final String fullName, final String type) {
        this.fullname = fullName;
        this.type = type;
    }

    public JudicialOfficerWeb(final String fullName, final String type, final Boolean isQC, final Boolean isResident) {
        this.fullname = fullName;
        this.type = type;
        this.isQC = isQC;
        this.isResident = isResident;
    }

    public JudicialOfficerWeb(final String errorMessage) {
        super(errorMessage);
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(final String fullname) {
        this.fullname = fullname;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Boolean getIsQC() {
        return isQC;
    }

    public void setIsQC(final Boolean isQC) {
        this.isQC = isQC;
    }

    public Boolean getIsResident() {
        return isResident;
    }

    public void setIsResident(final Boolean isResident) {
        this.isResident = isResident;
    }

    public String getQc() {
        return qc;
    }

    public void setQc(final String qc) {
        this.qc = qc;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(final String resident) {
        this.resident = resident;
    }

    public Set<String> getJudicialTickets() {
        return judicialTickets;
    }

}
