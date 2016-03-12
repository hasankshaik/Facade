package uk.co.listing.service;

import java.util.List;

public interface IEnumService {
    public List<String> getOffenceClass();

    public List<String> getTicketingRequirement();

    public List<String> getReleaseDecision();

    public List<String> getCustodyStatus();

    public List<String> getTimeUnits();

    public List<String> getProcessingStatus();

    public List<String> getSessionBlockType();

    public List<String> getJudicialOfficerType();

    public List<String> getJudicialTickets();

}
