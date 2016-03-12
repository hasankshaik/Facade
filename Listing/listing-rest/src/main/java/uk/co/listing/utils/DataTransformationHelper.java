package uk.co.listing.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.HearingInstance;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.PanelMemberType;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.rest.response.CaseNoteWeb;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.rest.response.CourtSessionWeb;
import uk.co.listing.rest.response.CrestDataBatchJobWeb;
import uk.co.listing.rest.response.HearingPlannerWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;
import uk.co.listing.rest.response.PersonInCaseWeb;
import uk.co.listing.rest.response.SessionBlockWeb;

public class DataTransformationHelper {

    private static final int KPI_INCRIMENT = 182;

    public static CourtCaseWeb caseRelatedToWeb(final CaseRelated caseRelated) {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(caseRelated.getCrestCaseNumber());
        courtCaseWeb.setMostSeriousOffence(caseRelated.getMostSeriousOffence());
        courtCaseWeb.setTrialEstimate(caseRelated.getTrialEstimate());
        handleTrialEstimate(caseRelated, courtCaseWeb);

        courtCaseWeb.setOffenceClass(caseRelated.getOffenceClass().getValue());
        courtCaseWeb.setReleaseDecisionStatus(caseRelated.getReleaseDecisionStatus().getValue());
        courtCaseWeb.setTicketingRequirement(caseRelated.getTicketingRequirement().getValue());
        courtCaseWeb.setDateOfCommittal(caseRelated.getDateOfCommittal());
        courtCaseWeb.setDateOfSending(caseRelated.getDateOfSending());
        if(caseRelated.getJudge()!=null) {
            courtCaseWeb.setJudicialOfficer(caseRelated.getJudge().getFullName());
        }

        caseRelated.getHearings().forEach(hearing -> {
            if (hearing.getActive()) {
                final HearingWeb hearingWeb = hearingToWeb(hearing);
                courtCaseWeb.getHearings().add(hearingWeb);
            }
        });

        for (final CaseNote note : caseRelated.getNotes()) {
            final CaseNoteWeb noteWeb = new CaseNoteWeb(note.getNote(), note.getDiaryDate(), note.getCreationDate(), note.getId() + "");
            courtCaseWeb.getNotesWeb().add(noteWeb);
        }

        for (final CaseRelated caseLinked : caseRelated.getLinkedCases()) {
            final CourtCaseWeb caseLinkedWeb = new CourtCaseWeb();
            caseLinkedWeb.setLeadDefendant(caseLinked.getLeadDefendant());
            caseLinkedWeb.setMostSeriousOffence(caseLinked.getMostSeriousOffence());
            caseLinkedWeb.setCrestCaseNumber(caseLinked.getCrestCaseNumber());
            courtCaseWeb.getLinkedCases().add(caseLinkedWeb);
        }

        for (final PersonInCase personInCase : caseRelated.getPersonInCase()) {
            final PersonInCaseWeb personInCaseWeb = personInCaseToWeb(personInCase);
            courtCaseWeb.getPersonInCaseList().add(personInCaseWeb);
        }

        final String leadDefendant = caseRelated.getLeadDefendant();
        if (caseRelated.getNumberOfDefendant() > 2) {
            courtCaseWeb.setLeadDefendant(leadDefendant.concat(" and ").concat("" + (caseRelated.getNumberOfDefendant() - 1)).concat(" others"));
        } else if (caseRelated.getNumberOfDefendant() == 2) {
            courtCaseWeb.setLeadDefendant(leadDefendant.concat(" and 1 other"));
        } else {
            courtCaseWeb.setLeadDefendant(leadDefendant);
        }
        courtCaseWeb.setNumberOfDefendent(caseRelated.getNumberOfDefendant());

        caseRelated.getCrustNonAvailableDatesList().forEach(crustNonAvailableDates -> {
            final NotAvailableDatesWeb crustNonAvailableDateWeb = new NotAvailableDatesWeb();
            crustNonAvailableDateWeb.setReason(crustNonAvailableDates.getReason());
            crustNonAvailableDateWeb.setId(crustNonAvailableDates.getId());
            if (crustNonAvailableDates.getStartDate() != null) {
                crustNonAvailableDateWeb.setStartDate(crustNonAvailableDates.getStartDate());
            }
            if (crustNonAvailableDates.getEndDate() != null) {
                crustNonAvailableDateWeb.setEndDate(crustNonAvailableDates.getEndDate());
            }
            courtCaseWeb.getCrustNonAvailableDateList().add(crustNonAvailableDateWeb);

        });
        courtCaseWeb.setCaseCompleted(caseRelated.getCaseClosed());
        return courtCaseWeb;
    }

    public static CourtRoomWeb courtRoomAndSessionToWeb(final CourtRoom courtRoom, final List<CourtSession> courtSessions) {
        final CourtRoomWeb courtRoomWeb = new CourtRoomWeb();
        courtRoomWeb.setCourtRoomName(courtRoom.getRoomName());
        for (final CourtSession session : courtSessions) {
            final CourtSessionWeb courtSessionWeb = new CourtSessionWeb();
            if ((session.getPanel() != null) && !session.getPanel().getPanelMember().isEmpty()) {
                populateJudge(session, courtSessionWeb);
            }
            courtSessionWeb.setSittingDate(DateTimeUtils.formatToStandardPattern(session.getSitting().getDay()));
            courtSessionWeb.setSittingDate(DateTimeUtils.formatToStandardPattern(session.getSitting().getDay()));
            courtSessionWeb.setIsClosed(session.getIsClosed());
            courtRoomWeb.getListCourtSessionWeb().add(courtSessionWeb);
            for (final SessionBlock sessionBlock : session.getSessionBlock()) {
                populateCourtSession(courtSessionWeb, sessionBlock);
            }
        }

        return courtRoomWeb;

    }

    public static List<CourtRoomWeb> courtRoomsToWeb(final List<CourtRoom> courtRooms) {
        final List<CourtRoomWeb> listCourtCenterWeb = new ArrayList<CourtRoomWeb>();
        for (final CourtRoom courtRoom : courtRooms) {
            final CourtRoomWeb courtRoomWeb = new CourtRoomWeb();
            courtRoomWeb.setCourtRoomName(courtRoom.getRoomName());
            listCourtCenterWeb.add(courtRoomWeb);
        }
        return listCourtCenterWeb;
    }

    public static List<CrestDataBatchJobWeb> crestDataBatchJobToWeb(final List<CrestDataBatchJob> crestDataBatchJobList) {
        final List<CrestDataBatchJobWeb> crestDataBatchJobWebList = new ArrayList<>();
        for (final CrestDataBatchJob crestDataBatchJob : crestDataBatchJobList) {
            final CrestDataBatchJobWeb crestDataBatchJobWeb = new CrestDataBatchJobWeb();
            crestDataBatchJobWeb.setComments(crestDataBatchJob.getComments());
            crestDataBatchJobWeb.setProcessingState(crestDataBatchJob.getProcessingState().getDescription());
            crestDataBatchJobWeb.setFileName(crestDataBatchJob.getFileName());
            crestDataBatchJobWeb.setLastModifiedOn(crestDataBatchJob.getLastModifiedOn());
            crestDataBatchJobWebList.add(crestDataBatchJobWeb);
        }
        return crestDataBatchJobWebList;
    }

    public static String getDefendantFullname(final String forenameOne, final String forenameTwo, final String surname) {
        final StringBuilder stb = new StringBuilder();
        final String space = " ";
        if (StringUtils.isNotBlank(forenameOne)) {
            stb.append(forenameOne);
            stb.append(space);
        }
        if (StringUtils.isNotBlank(forenameTwo)) {
            stb.append(forenameTwo);
            stb.append(space);
        }
        if (StringUtils.isNotBlank(surname)) {
            stb.append(surname);
        }
        return stb.toString();

    }

    public static Date getHearingKPIDateForTrial(final Hearing hearing) {
        Date result = new Date();
        if (hearing.getCaseRelated().getDateOfSending() != null) {
            result = DateUtils.addDays(hearing.getCaseRelated().getDateOfSending(), KPI_INCRIMENT);
        } else if (hearing.getCaseRelated().getDateOfCommittal() != null) {
            result = DateUtils.addDays(hearing.getCaseRelated().getDateOfCommittal(), KPI_INCRIMENT);
        }
        return result;
    }

    public static List<HearingWeb> hearingsToWeb(final List<Hearing> hearings) {
        final List<HearingWeb> hearingsWeb = new ArrayList<>();
        hearings.forEach(hearing -> {
            final HearingWeb hearingWeb = hearingToWeb(hearing);
            hearingsWeb.add(hearingWeb);
        });
        return hearingsWeb;
    }

    public static HearingWeb hearingToWeb(final Hearing hearing) {
        final HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setHearingName(hearing.getName());
        hearingWeb.setHearingKey(hearing.getHearingKey());
        hearingWeb.setHearingCase(hearing.getCaseRelated().getCrestCaseNumber());
        hearingWeb.setTrialEstimate(hearing.getDaysEstimated());
        hearingWeb.setBookingStatus(hearing.getBookingStatus().getDescription());
        hearingWeb.setCourtRoomName(hearing.getBookedCourtRoomName());
        hearingWeb.setActive(hearing.getActive());
        if (hearing.getHearingType() != null) {
            hearingWeb.setHearingType(hearing.getHearingType().getDescription());
        }
        if (hearing.getStartDate() != null) {
            hearingWeb.setStartDate(DateTimeUtils.formatToStandardPattern(hearing.getStartDate()));
            hearingWeb.setStartDateForSlot(hearing.getStartDate());
        }
        if (hearing.getBookingType() != null) {
            hearingWeb.setBookingType(hearing.getBookingType().getDescription());
        }
        hearingWeb.setHearingNote(hearing.getHearingNote());
        if (hearing.getHearingStatus() != null) {
            hearingWeb.setHearingStatus(hearing.getHearingStatus().getDescription());
        }
        hearingWeb.setTrialKPIDate(getHearingKPIDateForTrial(hearing));
        return hearingWeb;
    }

    public static PersonInCaseWeb personInCaseToWeb(final PersonInCase personInCase) {
        final PersonInCaseWeb personInCaseWeb = new PersonInCaseWeb();
        personInCaseWeb.setRoleType(personInCase.getRoleInCase().getDescription());
        personInCaseWeb.setFullname(personInCase.getPerson().getPersonFullName());
        personInCaseWeb.setOriginalFullname(personInCase.getPerson().getPersonFullName());
        personInCaseWeb.setCrestURN(personInCase.getCaseUrn());
        if (personInCase.getCustodyStatus() != null) {
            personInCaseWeb.setCustodyStatus(personInCase.getCustodyStatus().getValue());
            if ((personInCase.getCustodyStatus().getValue().equals(CustodyStatus.CUSTODY.getValue()) || personInCase.getCustodyStatus().getValue().equals(CustodyStatus.INCARE.getValue()))
                    && (personInCase.getCtlExpiresOn() != null)) {
                personInCaseWeb.setCtlExpiryDate(personInCase.getCtlExpiresOn());
                String noOfDaysStr = "Expired";
                final long noOfDays = DateTimeUtils.getDaysBetween(new Date(), personInCase.getCtlExpiresOn());
                if (noOfDays >= 0) {
                    noOfDaysStr = Long.toString(noOfDays);
                }
                personInCaseWeb.setNoOfDayForCTLExpiry(noOfDaysStr);

            }
        }

        return personInCaseWeb;
    }

    private static void handleTrialEstimate(final CaseRelated caseRelated, final CourtCaseWeb courtCaseWeb) {
        if (Double.compare(caseRelated.getTrialEstimate(), 0) == 0) {
            courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.HOURS.getValue());
        } else {
            if (caseRelated.getEstimationUnit() != null) {
                courtCaseWeb.setTrialEstimateUnit(caseRelated.getEstimationUnit().getValue());
            } else {
                courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.DAYS.getValue());
            }
        }
    }

    private static void populateCourtSession(final CourtSessionWeb courtSessionWeb, final SessionBlock sessionBlock) {
        final SessionBlockWeb sessionBlockWeb = new SessionBlockWeb();
        if (sessionBlock.getBlockType() == null) {
            sessionBlock.setBlockType(SessionBlockType.TRIAL);
        }
        sessionBlockWeb.setBlockType(sessionBlock.getBlockType().getDescription());
        sessionBlockWeb.setEndDate(sessionBlock.getEndDate());
        sessionBlockWeb.setStartDate(sessionBlock.getStartDate());
        sessionBlockWeb.setBlockShortCode(sessionBlock.getBlockType().getCode());
        for (final HearingInstance hearingInst : sessionBlock.getHearings()) {
            final Hearing hearing = hearingInst.getHearing();
            final Long daysBetween= DateTimeUtils.getWorkingDaysBetween(hearing.getStartDate(), DateTimeUtils.parseDate(courtSessionWeb.getSittingDate()));
            final int totalNoDays = hearing.getHearingInstance().size();

            final HearingPlannerWeb hearingWeb = new HearingPlannerWeb();
            hearingWeb.setHearingName(hearing.getName());
            hearingWeb.setHearingKey(hearing.getHearingKey());
            hearingWeb.setHearingCase(hearing.getCaseRelated().getCrestCaseNumber());
            hearingWeb.setLeadDefendant(hearing.getCaseRelated().getLeadDefendant());
            hearingWeb.setTrialKPIDate(getHearingKPIDateForTrial(hearing));
            hearingWeb.setTrailDay(daysBetween.intValue()+1);
            hearingWeb.setTotalNoDays(totalNoDays);
            sessionBlockWeb.getHearingWebList().add(hearingWeb);
        }
        courtSessionWeb.getSessionBlockWebList().add(sessionBlockWeb);
    }

    private static void populateJudge(final CourtSession session, final CourtSessionWeb courtSessionWeb) {
        JudicialOfficer officer = new JudicialOfficer();
        for (final PanelMember pm : session.getPanel().getPanelMember()) {
            if (pm.getPanelMemberType().equals(PanelMemberType.JUDGE)) {
                officer = pm.getJudicialOfficer();
                courtSessionWeb.getJudgeNames().add(officer.getFullName());
            }
        }
    }

    private DataTransformationHelper() {
    }

}