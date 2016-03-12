package uk.co.listing.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;

import uk.co.listing.BaseMockingUnitTest;

public class EnumServiceTest extends BaseMockingUnitTest {

    @InjectMocks
    EnumService enumService;

    @Test
    public void getOffenceClassTest() {
        final List<String> offenceClasses = new ArrayList<String>();
        offenceClasses.add("1");
        offenceClasses.add("2");
        offenceClasses.add("3");
        offenceClasses.add("4");
        offenceClasses.add("None");
        final List<String> result = enumService.getOffenceClass();
        assertTrue(result.containsAll(offenceClasses));
        assertTrue(offenceClasses.containsAll(result));
    }

    @Test
    public void getTicketingRequirementTest() {
        final List<String> tickets = new ArrayList<String>();
        tickets.add("None");
        tickets.add("Attempted Murder");
        tickets.add("Fraud");
        tickets.add("Murder");
        tickets.add("Rape");
        final List<String> result = enumService.getTicketingRequirement();
        assertTrue(result.containsAll(tickets));
        assertTrue(tickets.containsAll(result));
    }

    @Test
    public void getReleaseDecisionTest() {
        final List<String> decisions = new ArrayList<String>();
        decisions.add("Any Recorder");
        decisions.add("Circuit Judge");
        decisions.add("High Court Judge");
        decisions.add("High Court Judge or Resident Judge");
        decisions.add("Named Judge");
        decisions.add("Recorder QC");
        final List<String> result = enumService.getReleaseDecision();
        assertTrue(result.containsAll(decisions));
        assertTrue(decisions.containsAll(result));
    }

    @Test
    public void getCustodyStatusTest() {
        final List<String> statuses = new ArrayList<String>();
        statuses.add("Bail");
        statuses.add("Custody");
        statuses.add("In Care");
        statuses.add("Not Applicable");
        final List<String> result = enumService.getCustodyStatus();
        assertTrue(result.containsAll(statuses));
        assertTrue(statuses.containsAll(result));
    }

    @Test
    public void getTimeUnitsTest() {
        final List<String> units = new ArrayList<String>();
        units.add("Hours");
        units.add("Days");
        units.add("Weeks");
        final List<String> result = enumService.getTimeUnits();
        assertTrue(result.containsAll(units));
        assertTrue(units.containsAll(result));
    }

    @Test
    public void getProcessingStatusTest() {
        final List<String> statuses = new ArrayList<String>();
        statuses.add("Awaiting");
        statuses.add("Processing");
        statuses.add("Processed");
        statuses.add("Error");
        final List<String> result = enumService.getProcessingStatus();
        assertTrue(result.containsAll(statuses));
        assertTrue(statuses.containsAll(result));
    }

    @Test
    public void getSessionBlockTypeTest() {
        final List<String> sessionBlocks = new ArrayList<String>();
        sessionBlocks.add("Short Work");
        sessionBlocks.add("Case Management");
        sessionBlocks.add("Trial");
        sessionBlocks.add("Sentence");
        sessionBlocks.add("Appeal against Conviction");
        sessionBlocks.add("Appeal against Sentence");
        sessionBlocks.add("PCM");
        final List<String> result = enumService.getSessionBlockType();
        assertTrue(result.containsAll(sessionBlocks));
        assertTrue(sessionBlocks.containsAll(result));
    }

    @Test
    public void getJudicialOfficerTypeTest() {
        final List<String> judicialTypes = new ArrayList<String>();
        judicialTypes.add("High Court");
        judicialTypes.add("Circuit");
        judicialTypes.add("Recorder");
        judicialTypes.add("Deputy Circuit Judge");
        final List<String> result = enumService.getJudicialOfficerType();
        assertTrue(result.containsAll(judicialTypes));
        assertTrue(judicialTypes.containsAll(result));
    }

    @Test
    public void getJudicialTicketsTest() {
        final List<String> judicialTickets = new ArrayList<String>();
        judicialTickets.add("Murder");
        judicialTickets.add("Attempted Murder");
        judicialTickets.add("Sexual Offences");
        judicialTickets.add("Fraud");
        judicialTickets.add("Health and Safety");
        final List<String> result = enumService.getJudicialTickets();
        assertTrue(result.containsAll(judicialTickets));
        assertTrue(judicialTickets.containsAll(result));
    }

}
