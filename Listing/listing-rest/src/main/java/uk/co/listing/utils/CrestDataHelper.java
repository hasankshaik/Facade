package uk.co.listing.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import uk.co.listing.domain.CrestData;
import uk.co.listing.domain.CrestDefendant;
import uk.co.listing.domain.CrestLinkedCase;
import uk.co.listing.domain.CrestNonAvailable;
import uk.co.listing.domain.CrestNote;
import uk.co.listing.domain.CrestPcmh;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.RoleInCase;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.rest.response.CaseNoteWeb;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;
import uk.co.listing.rest.response.PersonInCaseWeb;

public class CrestDataHelper {

    private static final String BIRMINGHAM = "Birmingham";

    public static CourtCaseWeb caseDefendantToWeb(final CrestDefendant crestDefendant) {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final String fullname = DataTransformationHelper.getDefendantFullname(crestDefendant.getDefendantForeNameOne(), crestDefendant.getDefendantForeNameTwo(), crestDefendant.getDefendantSurname());
        if (StringUtils.isBlank(fullname) || (crestDefendant.getBailCustodyStatus() == null)) {
            return courtCaseWeb;
        }
        courtCaseWeb.setCourtCenter(BIRMINGHAM);
        courtCaseWeb.setCrestCaseNumber(crestDefendant.getCaseNumber());
        final PersonInCaseWeb personInCaseWeb = personInCaseToWeb(crestDefendant);
        courtCaseWeb.getPersonInCaseList().add(personInCaseWeb);

        courtCaseWeb.setCaseCompleted(Boolean.FALSE);
        return courtCaseWeb;
    }

    public static CourtCaseWeb caseLinkedCaseToWeb(final CrestLinkedCase crestLinkedcase) {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCourtCenter(BIRMINGHAM);
        courtCaseWeb.setCrestCaseNumber(crestLinkedcase.getCaseNumber());

        final CourtCaseWeb caseLinkedWeb = new CourtCaseWeb();
        caseLinkedWeb.setCourtCenter(BIRMINGHAM);
        caseLinkedWeb.setCrestCaseNumber(crestLinkedcase.getLinkedCases());
        courtCaseWeb.getLinkedCases().add(caseLinkedWeb);
        return courtCaseWeb;
    }

    public static NotAvailableDatesWeb caseNonAvailToWeb(final CrestNonAvailable crestNonAvail) {
        if (StringUtils.isNotEmpty(crestNonAvail.getNonAvailableDateReason()) && (crestNonAvail.getNonAvailableDateStart() != null) && (crestNonAvail.getNonAvailableDateEnd() != null)) {
            final NotAvailableDatesWeb crustNonAvailableDateWeb = new NotAvailableDatesWeb();
            crustNonAvailableDateWeb.setCrestCaseNumber(crestNonAvail.getCaseNumber());
            crustNonAvailableDateWeb.setReason(crestNonAvail.getNonAvailableDateReason());
            if (crestNonAvail.getNonAvailableDateStart() != null) {
                crustNonAvailableDateWeb.setStartDate(crestNonAvail.getNonAvailableDateStart());
            }
            if (crestNonAvail.getNonAvailableDateEnd() != null) {
                crustNonAvailableDateWeb.setEndDate(crestNonAvail.getNonAvailableDateEnd());
            }
            return crustNonAvailableDateWeb;
        }
        return null;
    }

    public static CourtCaseWeb caseNoteToWeb(final CrestNote note) {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCourtCenter(BIRMINGHAM);
        courtCaseWeb.setCrestCaseNumber(note.getCaseNumber());
        if (StringUtils.isEmpty(note.getCaseNoteText()) || (note.getCaseNoteDate() == null)) {
            return courtCaseWeb;
        }
        final CaseNoteWeb noteWeb = new CaseNoteWeb(note.getCaseNoteText(), note.getCaseNoteDiaryDate(), note.getCaseNoteDate(), note.getId() + "");
        courtCaseWeb.getNotesWeb().add(noteWeb);
        return courtCaseWeb;
    }

    public static List<CourtCaseWeb> createCourtCaseWeb(final List<CrestData> listData) {
        final List<CourtCaseWeb> listCourtCaseWeb = new ArrayList<CourtCaseWeb>();
        final Map<String, List<CrestData>> listGroupedCrestData = listData.stream().collect(Collectors.groupingBy(crestData -> crestData.getCaseNumber()));
        listGroupedCrestData.entrySet().forEach(dataRow -> {
            final List<CrestData> crestDataSet = dataRow.getValue();
            listCourtCaseWeb.add(caseRelatedToWeb(crestDataSet));
        });
        return listCourtCaseWeb;

    }

    public static HearingWeb hearingToWeb(final CrestPcmh crestPcmh) {
        final HearingWeb hearingPCMH = new HearingWeb();
        if (crestPcmh.getPCMHDate() == null) {
            return hearingPCMH;
        }
        hearingPCMH.setHearingCase(crestPcmh.getCaseNumber());
        hearingPCMH.setHearingType(HearingType.PCM.name());
        hearingPCMH.setHearingName("PCM-" + DateTimeUtils.formatToStandardPattern(crestPcmh.getPCMHDate()));
        hearingPCMH.setStartDate(crestPcmh.getPCMHDate().toString());
        hearingPCMH.setStartDateForSlot(crestPcmh.getPCMHDate());
        hearingPCMH.setActive(Boolean.TRUE);
        return hearingPCMH;
    }

    private static CourtCaseWeb caseRelatedToWeb(final List<CrestData> crestDataCollection) {
        final CrestData caseRelatedInformation = crestDataCollection.get(0);
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCourtCenter(BIRMINGHAM);
        courtCaseWeb.setFromCrest(Boolean.TRUE);
        courtCaseWeb.setCrestCaseNumber(caseRelatedInformation.getCaseNumber());
        courtCaseWeb.setMostSeriousOffence(caseRelatedInformation.getOffence());
        if (caseRelatedInformation.getTrailEstListingOfficerDuration() == null) {
            courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.HOURS.getValue());
            courtCaseWeb.setTrialEstimate(0);
        } else {
            courtCaseWeb.setTrialEstimate(caseRelatedInformation.getTrailEstListingOfficerDuration());
            if (Double.compare(courtCaseWeb.getTrialEstimate(), 0) == 0) {
                courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.HOURS.toString());
            } else {
                if (caseRelatedInformation.getTrailEstListingOfficerUnits() != null) {
                    courtCaseWeb.setTrialEstimateUnit(caseRelatedInformation.getTrailEstListingOfficerUnits().toString());
                } else {
                    courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.DAYS.toString());
                }
            }
        }

        if (caseRelatedInformation.getOffenceClass() != null) {
            courtCaseWeb.setOffenceClass(caseRelatedInformation.getOffenceClass().getValue());
        }
        if (caseRelatedInformation.getTicketingType() != null) {
            courtCaseWeb.setTicketingRequirement(caseRelatedInformation.getTicketingType().getValue());
        } else {
            courtCaseWeb.setTicketingRequirement(TicketingRequirement.NON.getValue());
        }
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setDateOfCommittal(caseRelatedInformation.getCommitalDate());
        courtCaseWeb.setDateOfSending(caseRelatedInformation.getSentForTrailDate());
        courtCaseWeb.setLeadDefendant(caseRelatedInformation.getCaseTitle());
        courtCaseWeb.setNumberOfDefendent(caseRelatedInformation.getNoOfDefendants());
        courtCaseWeb.setCaseCompleted(Boolean.FALSE);
        return courtCaseWeb;
    }

    private static PersonInCaseWeb personInCaseToWeb(final CrestDefendant personInCase) {
        final PersonInCaseWeb personInCaseWeb = new PersonInCaseWeb();
        personInCaseWeb.setRoleType(RoleInCase.DEFENDANT.getDescription());
        final String personFullname = DataTransformationHelper.getDefendantFullname(personInCase.getDefendantForeNameOne(), personInCase.getDefendantForeNameTwo(), personInCase.getDefendantSurname());
        personInCaseWeb.setFullname(personFullname);
        personInCaseWeb.setCrestURN(personInCase.getPtiURN());
        if (personInCase.getBailCustodyStatus() != null) {
            personInCaseWeb.setCustodyStatus(personInCase.getBailCustodyStatus().getValue());
            if ((personInCase.getBailCustodyStatus().getValue().equals(CustodyStatus.CUSTODY.getValue()) || personInCase.getBailCustodyStatus().getValue().equals(CustodyStatus.INCARE.getValue()))
                    && (personInCase.getBailCustodyStatus() != null) && (personInCase.getCtlExpiryDate() != null)) {
                personInCaseWeb.setCtlExpiryDate(personInCase.getCtlExpiryDate());
                String noOfDaysStr = "Expired";
                final long noOfDays = DateTimeUtils.getDaysBetween(new Date(), personInCase.getCtlExpiryDate());
                if (noOfDays >= 0) {
                    noOfDaysStr = Long.toString(noOfDays);
                }
                personInCaseWeb.setNoOfDayForCTLExpiry(noOfDaysStr);

            }
        }

        return personInCaseWeb;
    }

    private CrestDataHelper() {

    }

}
