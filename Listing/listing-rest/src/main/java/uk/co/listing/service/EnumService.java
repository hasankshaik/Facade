package uk.co.listing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.JudicialTicketsEnum;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;

@Service("enumService")
public class EnumService implements IEnumService {

    @Override
    public List<String> getOffenceClass() {
        final List<String> offencesClass = new ArrayList<String>();
        for (final OffenceClass ofence : OffenceClass.values()) {
            offencesClass.add(ofence.getValue());
        }
        return offencesClass;
    }

    @Override
    public List<String> getTicketingRequirement() {
        final List<String> tickets = new ArrayList<String>();
        for (final TicketingRequirement ticket : TicketingRequirement.values()) {
            tickets.add(ticket.getValue());
        }
        return tickets;
    }

    @Override
    public List<String> getReleaseDecision() {
        final List<String> decisions = new ArrayList<String>();
        for (final ReleaseDecision decision : ReleaseDecision.values()) {
            decisions.add(decision.getValue());
        }
        return decisions;
    }

    @Override
    public List<String> getCustodyStatus() {
        final List<String> statuses = new ArrayList<String>();
        for (final CustodyStatus status : CustodyStatus.values()) {
            statuses.add(status.getValue());
        }
        return statuses;
    }

    @Override
    public List<String> getTimeUnits() {
        final List<String> units = new ArrayList<String>();
        for (final TimeEstimationUnit unit : TimeEstimationUnit.values()) {
            units.add(unit.getValue());
        }
        return units;
    }

    @Override
    public List<String> getProcessingStatus() {
        final List<String> statuses = new ArrayList<String>();
        for (final ProcessingStatus status : ProcessingStatus.values()) {
            statuses.add(status.getDescription());
        }
        return statuses;
    }

    @Override
    public List<String> getSessionBlockType() {
        final List<String> blockType = new ArrayList<String>();
        for (final SessionBlockType type : SessionBlockType.values()) {
            blockType.add(type.getDescription());
        }
        return blockType;
    }

    @Override
    public List<String> getJudicialOfficerType() {
        final List<String> judicialOfficerType = new ArrayList<String>();
        for (final JudicialOfficerType type : JudicialOfficerType.values()) {
            judicialOfficerType.add(type.getDescription());
        }
        return judicialOfficerType;
    }

    @Override
    public List<String> getJudicialTickets() {
        final List<String> judicialTickets = new ArrayList<String>();
        for (final JudicialTicketsEnum type : JudicialTicketsEnum.values()) {
            judicialTickets.add(type.getDescription());
        }
        return judicialTickets;
    }

}
