package uk.co.listing.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.HearingInstance;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.Person;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.HearingStatusEnum;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.PanelMemberType;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.RoleInCase;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.rest.response.CrestDataBatchJobWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.PersonInCaseWeb;

public class DataTransformationHelperTest {

    private static final String COURTROOM1 = "TEST-COURTROOM1";
    private static final String COURTROOM2 = "TEST-COURTROOM2";
    private static final String JUDGE = "TEST-ALAYA";

    @Test
    public void testGetWeekDates() {
        final Date currentDate = new Date();
        final Date firstDayOfWeek = DateTimeUtils.getFirstDateOfWeek(currentDate);
        final Date lasttDayOfWeek = DateUtils.addDays(firstDayOfWeek, 4);
        final List<Date> listCurrentWeekDate = DateTimeUtils.getWeekDates(1);
        assertTrue(DateUtils.isSameDay(firstDayOfWeek, listCurrentWeekDate.get(0)));
        assertTrue(DateUtils.isSameDay(lasttDayOfWeek, listCurrentWeekDate.get(4)));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDayOfWeek);
    }

    @Test
    public void testCourtRoomsToWeb() {
        final CourtRoom courtRooms1 = new CourtRoom(COURTROOM1);
        final CourtRoom courtRooms2 = new CourtRoom(COURTROOM2);

        final List<CourtRoom> courtRooms = new ArrayList<CourtRoom>();
        courtRooms.add(courtRooms1);
        courtRooms.add(courtRooms2);

        final List<CourtRoomWeb> courtRoomsWeb = DataTransformationHelper.courtRoomsToWeb(courtRooms);
        assertTrue(courtRoomsWeb.size() == 2);
        assertTrue(courtRoomsWeb.get(0).getCourtRoomName().equals(courtRooms1.getRoomName()));
        assertTrue(courtRoomsWeb.get(1).getCourtRoomName().equals(courtRooms2.getRoomName()));
    }

    @Test
    public void testCourtRoomAndSessionToWebNoJudge() {
        final Date now = new Date();
        final List<CourtSession> courtSessions = new ArrayList<CourtSession>();
        final CourtRoom courtRooms1 = new CourtRoom(COURTROOM1);
        final CourtSession session = new CourtSession();
        final SittingDate sitting = new SittingDate();
        sitting.setCourtRoom(courtRooms1);
        sitting.setDay(now);
        session.setSitting(sitting);
        courtSessions.add(session);
        final CourtRoomWeb courtRoomWeb = DataTransformationHelper.courtRoomAndSessionToWeb(courtRooms1, courtSessions);
        assertTrue(courtRoomWeb.getCourtRoomName().equals(courtRooms1.getRoomName()));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().size() == 1);
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getSittingDate().equals(DateTimeUtils.formatToStandardPattern(now)));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getJudgeNames().size() == 0);
    }

    @Test
    public void testCourtRoomAndSessionToWebWithJudge() {
        final Date now = new Date();
        final List<CourtSession> courtSessions = new ArrayList<CourtSession>();
        final CourtRoom courtRooms1 = new CourtRoom(COURTROOM1);
        final CourtSession session = new CourtSession();
        final Panel panel = new Panel();
        final PanelMember panelMember = new PanelMember();
        final List<PanelMember> panelMembers = new ArrayList<PanelMember>();
        final JudicialOfficer officer = new JudicialOfficer(JUDGE, JudicialOfficerType.CIRCUIT, true);
        final SittingDate sitting = new SittingDate();
        sitting.setCourtRoom(courtRooms1);
        sitting.setDay(now);
        session.setSitting(sitting);
        panelMember.setJudicialOfficer(officer);
        panelMember.setPanel(panel);
        panelMember.setPanelMemberType(PanelMemberType.JUDGE);
        panelMembers.add(panelMember);
        panel.getPanelMember().addAll(panelMembers);
        session.setPanel(panel);
        courtSessions.add(session);
        final CourtRoomWeb courtRoomWeb = DataTransformationHelper.courtRoomAndSessionToWeb(courtRooms1, courtSessions);
        assertTrue(courtRoomWeb.getCourtRoomName().equals(courtRooms1.getRoomName()));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().size() == 1);
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getSittingDate().equals(DateTimeUtils.formatToStandardPattern(now)));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getJudgeNames().get(0).equals(JUDGE));
    }

    @Test
    public void testCourtRoomAndSessionToWebWithHearings() {
        final Date now = new Date();
        final List<CourtSession> courtSessions = new ArrayList<CourtSession>();
        final CourtRoom courtRooms1 = new CourtRoom(COURTROOM1);
        final CourtSession session = new CourtSession();
        final Panel panel = new Panel();
        final PanelMember panelMember = new PanelMember();
        final List<PanelMember> panelMembers = new ArrayList<PanelMember>();
        final JudicialOfficer officer = new JudicialOfficer(JUDGE, JudicialOfficerType.CIRCUIT, true);
        final SittingDate sitting = new SittingDate();
        sitting.setCourtRoom(courtRooms1);
        sitting.setDay(now);
        session.setSitting(sitting);
        panelMember.setJudicialOfficer(officer);
        panelMember.setPanel(panel);
        panelMember.setPanelMemberType(PanelMemberType.JUDGE);
        panelMembers.add(panelMember);
        panel.getPanelMember().addAll(panelMembers);
        session.setPanel(panel);

        final SessionBlock block = new SessionBlock();
        final HearingInstance instance = new HearingInstance();
        final Hearing hearing = new Hearing();
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber("T100");
        caseRelated.setLeadDefendant("Makinavaja");
        hearing.setName("Makipoeta");
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setCaseRelated(caseRelated);
        final Set hearingInst = new HashSet<HearingInstance>();
        hearingInst.add(new HearingInstance());
        hearing.setHearingInstance(hearingInst);
        hearing.setStartDate(new Date());
        instance.setHearing(hearing);
        block.getHearings().add(instance);
        session.getSessionBlock().add(block);

        courtSessions.add(session);
        final CourtRoomWeb courtRoomWeb = DataTransformationHelper.courtRoomAndSessionToWeb(courtRooms1, courtSessions);
        assertTrue(courtRoomWeb.getCourtRoomName().equals(courtRooms1.getRoomName()));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().size() == 1);
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getSittingDate().equals(DateTimeUtils.formatToStandardPattern(now)));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getJudgeNames().get(0).equals(JUDGE));
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getSessionBlockWebList().get(0).getHearingWebList().get(0).getTotalNoDays() == 1);
        assertTrue(courtRoomWeb.getListCourtSessionWeb().get(0).getSessionBlockWebList().get(0).getHearingWebList().get(0).getTrailDay() == 1);
    }

    @Test
    public void testCaseRelatedToWebNoExtras() {
        final CaseRelated caseRelated = new CaseRelated();
        final String crestCaseNumber = "crestNumber";
        final Date now = new Date();
        final String mostSeriousOffence = "mostSeriousOffence";
        final int trialEstimate = 10;
        caseRelated.setLeadDefendant(JUDGE);
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(now);
        caseRelated.setMostSeriousOffence(mostSeriousOffence);
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(trialEstimate);
        caseRelated.setEstimationUnit(TimeEstimationUnit.WEEKS);
        final CourtCaseWeb courtCaseWeb = DataTransformationHelper.caseRelatedToWeb(caseRelated);
        assertTrue(courtCaseWeb.getCrestCaseNumber().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getLeadDefendant().equals(JUDGE));
        assertTrue(courtCaseWeb.getMostSeriousOffence().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getNumberOfDefendent() == 0);
        assertTrue(courtCaseWeb.getOffenceClass().equals(OffenceClass.CLASS1.getValue()));
        assertTrue(courtCaseWeb.getReleaseDecisionStatus().equals(ReleaseDecision.AR.getValue()));
        assertTrue(courtCaseWeb.getTicketingRequirement().equals(TicketingRequirement.FRA.getValue()));
        assertTrue(courtCaseWeb.getTrialEstimateUnit().equals(TimeEstimationUnit.WEEKS.getValue()));
        assertTrue(courtCaseWeb.getTrialEstimate() == trialEstimate);
        assertTrue(courtCaseWeb.getHearings().size() == 0);
        assertTrue(courtCaseWeb.getNotesWeb().size() == 0);
        assertTrue(courtCaseWeb.getLinkedCases().size() == 0);
        assertTrue(courtCaseWeb.getPersonInCaseList().size() == 0);
    }

    @Test
    public void testCaseRelatedToWebWithExtras() {

        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        final Hearing hearing = new Hearing();
        final CaseNote note = new CaseNote();
        final CaseRelated caseLinked = new CaseRelated();
        final PersonInCase personInCase1 = new PersonInCase();
        final PersonInCase personInCase2 = new PersonInCase();
        final PersonInCase personInCase3 = new PersonInCase();
        final Person person1 = new Person();
        final Person person2 = new Person();
        final Person person3 = new Person();
        final List<Hearing> hearings = new ArrayList<Hearing>();
        final List<CaseNote> notes = new ArrayList<CaseNote>();
        final List<CaseRelated> casesLinked = new ArrayList<CaseRelated>();
        final List<PersonInCase> defendants = new ArrayList<PersonInCase>();
        final JudicialOfficer judge = new JudicialOfficer(JUDGE, JudicialOfficerType.CIRCUIT, true);
        final String crestCaseNumber = "crestNumber";
        final String crestCaseNumber2 = "crestNumber2";
        final String mostSeriousOffence = "mostSeriousOffence";
        final String mostSeriousOffence2 = "mostSeriousOffence2";
        final String leadDefendant2 = JUDGE + "2";
        final int trialEstimate = 10;

        caseRelated.setLeadDefendant(JUDGE);
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(now);
        caseRelated.setMostSeriousOffence(mostSeriousOffence);
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(trialEstimate);
        caseRelated.setEstimationUnit(TimeEstimationUnit.DAYS);
        caseRelated.setNumberOfDefendant(2);
        caseRelated.setJudge(judge);
        final Date afterNow = new Date();
        caseRelated.setDateOfCommittal(now);

        hearing.setCaseRelated(caseRelated);
        hearing.setName(mostSeriousOffence);
        hearing.setStartDate(now);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setDaysEstimated(2.0);
        hearing.setActive(true);
        hearings.add(hearing);
        caseRelated.getHearings().addAll(hearings);

        note.setCaseRelated(caseRelated);
        note.setNote(mostSeriousOffence);
        note.setCreationDate(now);
        note.setDiaryDate(afterNow);
        notes.add(note);
        caseRelated.getNotes().addAll(notes);

        caseLinked.setCrestCaseNumber(crestCaseNumber2);
        caseLinked.setLeadDefendant(leadDefendant2);
        caseLinked.setMostSeriousOffence(mostSeriousOffence2);
        casesLinked.add(caseLinked);
        caseRelated.getLinkedCases().addAll(casesLinked);

        person1.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.CUSTODY);
        personInCase1.setCtlExpiresOn(now);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person1);
        defendants.add(personInCase1);

        person2.setPersonFullName(leadDefendant2);
        personInCase2.setCustodyStatus(CustodyStatus.BAIL);
        personInCase2.setCtlExpiresOn(now);
        personInCase2.setRoleInCase(RoleInCase.WITNESS);
        personInCase2.setPerson(person2);
        defendants.add(personInCase2);

        final String personName3 = JUDGE + "3";
        person3.setPersonFullName(personName3);
        personInCase3.setCustodyStatus(CustodyStatus.BAIL);
        personInCase3.setCtlExpiresOn(afterNow);
        personInCase3.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase3.setPerson(person3);
        defendants.add(personInCase3);
        caseRelated.getPersonInCase().addAll(defendants);

        final Set<CrustNonAvailableDates> crustNonAvailableDatesList = new HashSet<CrustNonAvailableDates>();

        final CrustNonAvailableDates crustNonAvailableDates1 = new CrustNonAvailableDates();
        crustNonAvailableDates1.setReason("reason1");
        crustNonAvailableDates1.setStartDate(new Date());
        crustNonAvailableDates1.setEndDate(DateUtils.addDays(new Date(), 2));

        final CrustNonAvailableDates crustNonAvailableDates2 = new CrustNonAvailableDates();
        crustNonAvailableDates2.setReason("reason2");
        crustNonAvailableDates2.setStartDate(new Date());
        crustNonAvailableDates2.setEndDate(DateUtils.addDays(new Date(), 2));

        crustNonAvailableDatesList.add(crustNonAvailableDates1);
        crustNonAvailableDatesList.add(crustNonAvailableDates2);

        caseRelated.setCrustNonAvailableDatesList(crustNonAvailableDatesList);

        final CourtCaseWeb courtCaseWeb = DataTransformationHelper.caseRelatedToWeb(caseRelated);
        assertTrue(courtCaseWeb.getCrestCaseNumber().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getLeadDefendant().equals(JUDGE.concat(" and 1 other")));
        assertTrue(courtCaseWeb.getMostSeriousOffence().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getOffenceClass().equals(OffenceClass.CLASS1.getValue()));
        assertTrue(courtCaseWeb.getReleaseDecisionStatus().equals(ReleaseDecision.AR.getValue()));
        assertTrue(courtCaseWeb.getTicketingRequirement().equals(TicketingRequirement.FRA.getValue()));
        assertTrue(courtCaseWeb.getTrialEstimate() == trialEstimate);
        assertTrue(courtCaseWeb.getDateOfCommittal().equals(now));
        assertTrue(courtCaseWeb.getJudicialOfficer().equals(JUDGE));

        assertTrue(courtCaseWeb.getHearings().size() == 1);
        assertTrue(courtCaseWeb.getNotesWeb().size() == 1);
        assertTrue(courtCaseWeb.getLinkedCases().size() == 1);
        assertTrue(courtCaseWeb.getPersonInCaseList().size() == 3);
        assertTrue(courtCaseWeb.getNumberOfDefendent() == 2);

        assertTrue(courtCaseWeb.getHearings().get(0).getHearingCase().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getHearings().get(0).getHearingName().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getHearings().get(0).getStartDate().equals(DateTimeUtils.formatToStandardPattern(now)));
        assertTrue(courtCaseWeb.getHearings().get(0).getHearingType().equals(HearingType.TRIAL.getDescription()));

        assertTrue(courtCaseWeb.getNotesWeb().get(0).getCreationDate().equals(now));
        assertTrue(courtCaseWeb.getNotesWeb().get(0).getDiaryDate().equals(afterNow));
        assertTrue(courtCaseWeb.getNotesWeb().get(0).getNote().equals(mostSeriousOffence));

        assertTrue(courtCaseWeb.getLinkedCases().get(0).getCrestCaseNumber().equals(crestCaseNumber2));
        assertTrue(courtCaseWeb.getLinkedCases().get(0).getLeadDefendant().equals(leadDefendant2));
        assertTrue(courtCaseWeb.getLinkedCases().get(0).getMostSeriousOffence().equals(mostSeriousOffence2));

        for (int i = 0; i < 3; i++) {
            if (courtCaseWeb.getPersonInCaseList().get(i).getFullname().equals(JUDGE)) {
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCtlExpiryDate().equals(now));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCustodyStatus().equals(CustodyStatus.CUSTODY.getValue()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getRoleType().equals(RoleInCase.DEFENDANT.getDescription()));
            } else if (courtCaseWeb.getPersonInCaseList().get(i).getFullname().equals(leadDefendant2)) {
                assertNull(courtCaseWeb.getPersonInCaseList().get(i).getCtlExpiryDate());
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCustodyStatus().equals(CustodyStatus.BAIL.getValue()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getRoleType().equals(RoleInCase.WITNESS.getDescription()));
            } else if (courtCaseWeb.getPersonInCaseList().get(i).getFullname().equals(personName3)) {
                assertNull(courtCaseWeb.getPersonInCaseList().get(i).getCtlExpiryDate());
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCustodyStatus().equals(CustodyStatus.BAIL.getValue()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getRoleType().equals(RoleInCase.DEFENDANT.getDescription()));
            } else {
                assertTrue(false);
            }
        }

        assertTrue(courtCaseWeb.getCrustNonAvailableDateList().size() == 2);
        for (int i = 0; i < 2; i++) {
            if (courtCaseWeb.getCrustNonAvailableDateList().get(i).getReason().equals(crustNonAvailableDates1.getReason())) {
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getStartDate().equals(crustNonAvailableDates1.getStartDate()));
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getEndDate().equals(crustNonAvailableDates1.getEndDate()));
            } else if (courtCaseWeb.getCrustNonAvailableDateList().get(i).getReason().equals(crustNonAvailableDates2.getReason())) {
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getStartDate().equals(crustNonAvailableDates2.getStartDate()));
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getEndDate().equals(crustNonAvailableDates2.getEndDate()));
            }
        }

    }

    @Test
    public void testCaseRelatedFalseHearings() {

        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        final Hearing hearing = new Hearing();
        final List<Hearing> hearings = new ArrayList<Hearing>();
        final String crestCaseNumber = "crestNumber";
        final String mostSeriousOffence = "mostSeriousOffence";
        final int trialEstimate = 0;

        caseRelated.setLeadDefendant(JUDGE);
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(now);
        caseRelated.setMostSeriousOffence(mostSeriousOffence);
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(trialEstimate);
        caseRelated.setDateOfCommittal(now);
        caseRelated.setDateOfSending(now);

        hearing.setCaseRelated(caseRelated);
        hearing.setName(mostSeriousOffence);
        hearing.setStartDate(now);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setDaysEstimated(2.0);
        hearing.setActive(false);
        hearings.add(hearing);
        caseRelated.getHearings().addAll(hearings);

        final CourtCaseWeb courtCaseWeb = DataTransformationHelper.caseRelatedToWeb(caseRelated);
        assertTrue(courtCaseWeb.getCrestCaseNumber().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getLeadDefendant().equals(JUDGE));
        assertTrue(courtCaseWeb.getMostSeriousOffence().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getOffenceClass().equals(OffenceClass.CLASS1.getValue()));
        assertTrue(courtCaseWeb.getReleaseDecisionStatus().equals(ReleaseDecision.AR.getValue()));
        assertTrue(courtCaseWeb.getTicketingRequirement().equals(TicketingRequirement.FRA.getValue()));
        assertTrue(courtCaseWeb.getTrialEstimate() == trialEstimate);
        assertTrue(courtCaseWeb.getTrialEstimateUnit() == TimeEstimationUnit.HOURS.getValue());

        assertTrue(courtCaseWeb.getHearings().size() == 0);

    }

    @Test
    public void testCaseRelatedNoEstimationUnit() {

        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        final Hearing hearing = new Hearing();
        final List<Hearing> hearings = new ArrayList<Hearing>();
        final String crestCaseNumber = "crestNumber";
        final String mostSeriousOffence = "mostSeriousOffence";
        final int trialEstimate = 10;

        caseRelated.setLeadDefendant(JUDGE);
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(now);
        caseRelated.setMostSeriousOffence(mostSeriousOffence);
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(trialEstimate);
        caseRelated.setDateOfCommittal(now);

        hearing.setCaseRelated(caseRelated);
        hearing.setName(mostSeriousOffence);
        hearing.setStartDate(now);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setDaysEstimated(2.0);
        hearing.setActive(false);
        hearing.setHearingStatus(HearingStatusEnum.COMPLETE);
        hearings.add(hearing);
        caseRelated.getHearings().addAll(hearings);

        final CourtCaseWeb courtCaseWeb = DataTransformationHelper.caseRelatedToWeb(caseRelated);
        assertTrue(courtCaseWeb.getCrestCaseNumber().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getLeadDefendant().equals(JUDGE));
        assertTrue(courtCaseWeb.getMostSeriousOffence().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getOffenceClass().equals(OffenceClass.CLASS1.getValue()));
        assertTrue(courtCaseWeb.getReleaseDecisionStatus().equals(ReleaseDecision.AR.getValue()));
        assertTrue(courtCaseWeb.getTicketingRequirement().equals(TicketingRequirement.FRA.getValue()));
        assertTrue(courtCaseWeb.getTrialEstimate() == trialEstimate);
        assertTrue(courtCaseWeb.getTrialEstimateUnit() == TimeEstimationUnit.DAYS.getValue());

        assertTrue(courtCaseWeb.getHearings().size() == 0);

    }

    @Test
    public void testCaseRelatedToWebWith2Defendants() {

        final Date now = new Date();
        final Date yesterday = DateUtils.addDays(now, -1);
        final CaseRelated caseRelated = new CaseRelated();
        final Hearing hearing = new Hearing();
        final CaseNote note = new CaseNote();
        final CaseRelated caseLinked = new CaseRelated();
        final PersonInCase personInCase1 = new PersonInCase();
        final PersonInCase personInCase2 = new PersonInCase();
        final PersonInCase personInCase3 = new PersonInCase();
        final Person person1 = new Person();
        final Person person2 = new Person();
        final Person person3 = new Person();
        final List<Hearing> hearings = new ArrayList<Hearing>();
        final List<CaseNote> notes = new ArrayList<CaseNote>();
        final List<CaseRelated> casesLinked = new ArrayList<CaseRelated>();
        final List<PersonInCase> defendants = new ArrayList<PersonInCase>();
        final String crestCaseNumber = "crestNumber";
        final String crestCaseNumber2 = "crestNumber2";
        final String mostSeriousOffence = "mostSeriousOffence";
        final String mostSeriousOffence2 = "mostSeriousOffence2";
        final String leadDefendant2 = JUDGE + "2";
        final int trialEstimate = 10;

        caseRelated.setLeadDefendant(JUDGE);
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(now);
        caseRelated.setMostSeriousOffence(mostSeriousOffence);
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(trialEstimate);
        caseRelated.setEstimationUnit(TimeEstimationUnit.DAYS);
        caseRelated.setNumberOfDefendant(5);
        caseRelated.setDateOfSending(now);

        hearing.setCaseRelated(caseRelated);
        hearing.setName(mostSeriousOffence);
        hearing.setStartDate(now);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setDaysEstimated(2.0);
        hearing.setActive(true);
        hearings.add(hearing);
        hearing.setHearingStatus(HearingStatusEnum.PENDING);
        caseRelated.getHearings().addAll(hearings);

        note.setCaseRelated(caseRelated);
        note.setNote(mostSeriousOffence);
        note.setCreationDate(now);
        final Date afterNow = new Date();
        note.setDiaryDate(afterNow);
        notes.add(note);
        caseRelated.getNotes().addAll(notes);

        caseLinked.setCrestCaseNumber(crestCaseNumber2);
        caseLinked.setLeadDefendant(leadDefendant2);
        caseLinked.setMostSeriousOffence(mostSeriousOffence2);
        casesLinked.add(caseLinked);
        caseRelated.getLinkedCases().addAll(casesLinked);

        person1.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.CUSTODY);
        personInCase1.setCtlExpiresOn(yesterday);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person1);
        personInCase1.setCaseUrn(mostSeriousOffence);
        defendants.add(personInCase1);

        person2.setPersonFullName(leadDefendant2);
        personInCase2.setCustodyStatus(CustodyStatus.BAIL);
        personInCase2.setCtlExpiresOn(now);
        personInCase2.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase2.setPerson(person2);
        personInCase2.setCaseUrn(mostSeriousOffence2);
        defendants.add(personInCase2);

        final String personName3 = JUDGE + "3";
        person3.setPersonFullName(personName3);
        personInCase3.setCustodyStatus(CustodyStatus.BAIL);
        personInCase3.setCtlExpiresOn(afterNow);
        personInCase3.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase3.setPerson(person3);
        defendants.add(personInCase3);
        caseRelated.getPersonInCase().addAll(defendants);

        final Set<CrustNonAvailableDates> crustNonAvailableDatesList = new HashSet<CrustNonAvailableDates>();

        final CrustNonAvailableDates crustNonAvailableDates1 = new CrustNonAvailableDates();
        crustNonAvailableDates1.setReason("reason1");
        crustNonAvailableDates1.setStartDate(new Date());
        crustNonAvailableDates1.setEndDate(DateUtils.addDays(new Date(), 2));

        final CrustNonAvailableDates crustNonAvailableDates2 = new CrustNonAvailableDates();
        crustNonAvailableDates2.setReason("reason2");
        crustNonAvailableDates2.setStartDate(new Date());
        crustNonAvailableDates2.setEndDate(DateUtils.addDays(new Date(), 2));

        crustNonAvailableDatesList.add(crustNonAvailableDates1);
        crustNonAvailableDatesList.add(crustNonAvailableDates2);

        caseRelated.setCrustNonAvailableDatesList(crustNonAvailableDatesList);

        final CourtCaseWeb courtCaseWeb = DataTransformationHelper.caseRelatedToWeb(caseRelated);
        assertTrue(courtCaseWeb.getCrestCaseNumber().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getLeadDefendant().equals(JUDGE.concat(" and 4 others")));
        assertTrue(courtCaseWeb.getMostSeriousOffence().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getOffenceClass().equals(OffenceClass.CLASS1.getValue()));
        assertTrue(courtCaseWeb.getReleaseDecisionStatus().equals(ReleaseDecision.AR.getValue()));
        assertTrue(courtCaseWeb.getTicketingRequirement().equals(TicketingRequirement.FRA.getValue()));
        assertTrue(courtCaseWeb.getTrialEstimate() == trialEstimate);

        assertTrue(courtCaseWeb.getHearings().size() == 1);
        assertTrue(courtCaseWeb.getNotesWeb().size() == 1);
        assertTrue(courtCaseWeb.getLinkedCases().size() == 1);
        assertTrue(courtCaseWeb.getPersonInCaseList().size() == 3);
        assertTrue(courtCaseWeb.getNumberOfDefendent() == 5);

        assertTrue(courtCaseWeb.getHearings().get(0).getHearingCase().equals(crestCaseNumber));
        assertTrue(courtCaseWeb.getHearings().get(0).getHearingName().equals(mostSeriousOffence));
        assertTrue(courtCaseWeb.getHearings().get(0).getStartDate().equals(DateTimeUtils.formatToStandardPattern(now)));
        assertTrue(courtCaseWeb.getHearings().get(0).getHearingType().equals(HearingType.TRIAL.getDescription()));

        assertTrue(courtCaseWeb.getNotesWeb().get(0).getCreationDate().equals(now));
        assertTrue(courtCaseWeb.getNotesWeb().get(0).getDiaryDate().equals(afterNow));
        assertTrue(courtCaseWeb.getNotesWeb().get(0).getNote().equals(mostSeriousOffence));

        assertTrue(courtCaseWeb.getLinkedCases().get(0).getCrestCaseNumber().equals(crestCaseNumber2));
        assertTrue(courtCaseWeb.getLinkedCases().get(0).getLeadDefendant().equals(leadDefendant2));
        assertTrue(courtCaseWeb.getLinkedCases().get(0).getMostSeriousOffence().equals(mostSeriousOffence2));

        for (int i = 0; i < 3; i++) {
            if (courtCaseWeb.getPersonInCaseList().get(i).getFullname().equals(JUDGE)) {
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCtlExpiryDate().equals(yesterday));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCustodyStatus().equals(CustodyStatus.CUSTODY.getValue()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getRoleType().equals(RoleInCase.DEFENDANT.getDescription()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getNoOfDayForCTLExpiry().equals("Expired"));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCrestURN().equals(mostSeriousOffence));
            } else if (courtCaseWeb.getPersonInCaseList().get(i).getFullname().equals(leadDefendant2)) {
                assertNull(courtCaseWeb.getPersonInCaseList().get(i).getCtlExpiryDate());
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCustodyStatus().equals(CustodyStatus.BAIL.getValue()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getRoleType().equals(RoleInCase.DEFENDANT.getDescription()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCrestURN().equals(mostSeriousOffence2));
            } else if (courtCaseWeb.getPersonInCaseList().get(i).getFullname().equals(personName3)) {
                assertNull(courtCaseWeb.getPersonInCaseList().get(i).getCtlExpiryDate());
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getCustodyStatus().equals(CustodyStatus.BAIL.getValue()));
                assertTrue(courtCaseWeb.getPersonInCaseList().get(i).getRoleType().equals(RoleInCase.DEFENDANT.getDescription()));
                assertNull(courtCaseWeb.getPersonInCaseList().get(i).getCrestURN());
            } else {
                assertTrue(false);
            }
        }

        assertTrue(courtCaseWeb.getCrustNonAvailableDateList().size() == 2);
        for (int i = 0; i < 2; i++) {
            if (courtCaseWeb.getCrustNonAvailableDateList().get(i).getReason().equals(crustNonAvailableDates1.getReason())) {
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getStartDate().equals(crustNonAvailableDates1.getStartDate()));
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getEndDate().equals(crustNonAvailableDates1.getEndDate()));
            } else if (courtCaseWeb.getCrustNonAvailableDateList().get(i).getReason().equals(crustNonAvailableDates2.getReason())) {
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getStartDate().equals(crustNonAvailableDates2.getStartDate()));
                assertTrue(courtCaseWeb.getCrustNonAvailableDateList().get(i).getEndDate().equals(crustNonAvailableDates2.getEndDate()));
            }
        }

    }

    @Test
    public void hearingsToWebTest() {
        final List<Hearing> hearings = new ArrayList<Hearing>();
        final Hearing hearing = new Hearing();
        final Hearing hearing2 = new Hearing();
        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        hearing.setCaseRelated(caseRelated);
        hearing.setName("Most serious offence");
        hearing.setStartDate(now);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setBookingType(BookingTypeEnum.CONFIRMED);
        hearing.setDaysEstimated(7.0);
        hearing.setActive(true);
        hearing.setHearingStatus(HearingStatusEnum.COMPLETE);
        hearings.add(hearing);

        hearing2.setCaseRelated(caseRelated);
        hearing2.setName("Most serious offence2");
        hearing2.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing2.setActive(true);
        hearing2.setDaysEstimated(7.0);
        hearings.add(hearing2);

        final List<HearingWeb> hearingsWeb = DataTransformationHelper.hearingsToWeb(hearings);

        assertTrue(hearingsWeb.size() == 2);
        final HearingWeb hearingWeb1 = hearingsWeb.get(0);
        final HearingWeb hearingWeb2 = hearingsWeb.get(1);
        assertTrue(hearing.getName().equals(hearingWeb1.getHearingName()));
        assertTrue(hearing2.getName().equals(hearingWeb2.getHearingName()));
        assertTrue(DateTimeUtils.formatToStandardPattern(hearing.getStartDate()).equals(hearingWeb1.getStartDate()));
        assertTrue(hearing.getHearingType().getDescription().equals(hearingWeb1.getHearingType()));
        assertNull(hearing2.getHearingType());
        assertTrue(hearing.getBookingStatus().getDescription().equals(hearingWeb1.getBookingStatus()));
        assertTrue(hearing2.getBookingStatus().getDescription().equals(hearingWeb2.getBookingStatus()));
        assertTrue(hearing.getHearingStatus().getDescription().equals(hearingWeb1.getHearingStatus()));
        assertTrue(hearing.getBookingType().getDescription().equals(hearingWeb1.getBookingType()));
        assertTrue(hearing.getDaysEstimated() == hearingWeb1.getTrialEstimate());
        assertTrue(hearing2.getDaysEstimated() == hearingWeb2.getTrialEstimate());
        assertTrue(hearing.getActive().equals(hearingWeb1.getActive()));
        assertTrue(hearing2.getActive().equals(hearingWeb2.getActive()));
        assertNull(hearingWeb2.getBookingType());
    }

    @Test
    public void personInCaseToWebCustodyTest() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        final Date tomorrow = DateUtils.addDays(new Date(), +1);
        person.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.CUSTODY);
        personInCase1.setCtlExpiresOn(tomorrow);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertTrue(result.getCustodyStatus().equals(CustodyStatus.CUSTODY.getValue()));
        assertTrue(result.getCtlExpiryDate().equals(tomorrow));
        assertTrue(result.getNoOfDayForCTLExpiry().equals("1"));
    }

    @Test
    public void personInCaseToWebCustodyNoDateTest() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        person.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.CUSTODY);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertTrue(result.getCustodyStatus().equals(CustodyStatus.CUSTODY.getValue()));
        assertNull(result.getCtlExpiryDate());
        assertNull(result.getNoOfDayForCTLExpiry());
    }

    @Test
    public void personInCaseToWebCustodyNoStatusTest() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        person.setPersonFullName(JUDGE);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertNull(result.getCustodyStatus());
        assertNull(result.getCtlExpiryDate());
        assertNull(result.getNoOfDayForCTLExpiry());
    }

    @Test
    public void personInCaseToWebCustodyExpiredTest() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        final Date yesterday = DateUtils.addDays(new Date(), -1);
        person.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.CUSTODY);
        personInCase1.setCtlExpiresOn(yesterday);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertTrue(result.getCustodyStatus().equals(CustodyStatus.CUSTODY.getValue()));
        assertTrue(result.getCtlExpiryDate().equals(yesterday));
        assertTrue(result.getNoOfDayForCTLExpiry().equals("Expired"));
    }

    @Test
    public void personInCaseToWebInCareTest() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        final Date tomorrow = DateUtils.addDays(new Date(), +1);
        person.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.INCARE);
        personInCase1.setCtlExpiresOn(tomorrow);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertTrue(result.getCustodyStatus().equals(CustodyStatus.INCARE.getValue()));
        assertTrue(result.getCtlExpiryDate().equals(tomorrow));
        assertTrue(result.getNoOfDayForCTLExpiry().equals("1"));
    }

    @Test
    public void personInCaseToWebNotAppTest() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        final Date tomorrow = DateUtils.addDays(new Date(), +1);
        person.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.NOTAPPLICABLE);
        personInCase1.setCtlExpiresOn(tomorrow);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertTrue(result.getCustodyStatus().equals(CustodyStatus.NOTAPPLICABLE.getValue()));
        assertNull(result.getCtlExpiryDate());
        assertNull(result.getNoOfDayForCTLExpiry());
    }

    @Test
    public void personInCaseToWebBail() {
        final PersonInCase personInCase1 = new PersonInCase();
        final Person person = new Person();
        final Date tomorrow = DateUtils.addDays(new Date(), +1);
        person.setPersonFullName(JUDGE);
        personInCase1.setCustodyStatus(CustodyStatus.BAIL);
        personInCase1.setCtlExpiresOn(tomorrow);
        personInCase1.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase1.setPerson(person);
        personInCase1.setCaseUrn("Nothing great");

        final PersonInCaseWeb result = DataTransformationHelper.personInCaseToWeb(personInCase1);
        assertTrue(result.getFullname().equals(JUDGE));
        assertTrue(result.getCrestURN().equals("Nothing great"));
        assertTrue(result.getCustodyStatus().equals(CustodyStatus.BAIL.getValue()));
        assertNull(result.getCtlExpiryDate());
        assertNull(result.getNoOfDayForCTLExpiry());
    }

    @Test
    public void crestDataBatchJobToWeb() {
        final List<CrestDataBatchJob> crestDataBatchJobList = new ArrayList<>();
        final CrestDataBatchJob crestDataBatchJob = new CrestDataBatchJob();
        crestDataBatchJob.setFileName("file1.txt");
        crestDataBatchJob.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJob.setComments("no comments");
        crestDataBatchJobList.add(crestDataBatchJob);

        final List<CrestDataBatchJobWeb> crestDataBatchJobWebList = DataTransformationHelper.crestDataBatchJobToWeb(crestDataBatchJobList);
        final CrestDataBatchJobWeb result = crestDataBatchJobWebList.get(0);
        assertTrue(result.getComments().equals("no comments"));
        assertTrue(result.getFileName().equals("file1.txt"));
        assertTrue(result.getProcessingState().equals(ProcessingStatus.AWAITING.getDescription()));
    }

    @Test
    public void testGetDefendantFullname() {

        assertEquals(DataTransformationHelper.getDefendantFullname("FirstName", "SecondName", "Surname"), "FirstName SecondName Surname");
        assertEquals(DataTransformationHelper.getDefendantFullname("FirstName", null, "Surname"), "FirstName Surname");
        assertEquals(DataTransformationHelper.getDefendantFullname(null, "SecondName", "Surname"), "SecondName Surname");
        assertEquals(DataTransformationHelper.getDefendantFullname(null, null, "Surname"), "Surname");
    }
}
