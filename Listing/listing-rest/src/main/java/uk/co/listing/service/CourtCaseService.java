package uk.co.listing.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CaseNoteDao;
import uk.co.listing.dao.CaseRelatedDao;
import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.CrustNonAvailableDatesDao;
import uk.co.listing.dao.JudicialOfficerDao;
import uk.co.listing.dao.PersonDao;
import uk.co.listing.dao.PersonInCaseDao;
import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Person;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.RoleInCase;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CaseNoteWeb;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;
import uk.co.listing.rest.response.PersonInCaseWeb;
import uk.co.listing.utils.DataTransformationHelper;
import uk.co.listing.utils.DateTimeUtils;

@Service("CourtCaseService")
@Transactional(readOnly = true)
public class CourtCaseService implements ICourtCaseService {

    private static final Logger LOGGER = Logger.getLogger(CourtCaseService.class);

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private CaseRelatedDao caseRelatedDao;

    @Autowired
    private PersonInCaseDao personInCaseDao;

    @Autowired
    private CaseNoteDao caseNoteDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private CrustNonAvailableDatesDao notAvailableDateDao;

    @Autowired
    private JudicialOfficerDao judicialOfficerDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void allocateJudgeForCase(final CourtCaseWeb courtCaseWeb) {

        findCentre(courtCaseWeb);

        final CaseRelated caseRelated = findCaseByCentreAndNo(courtCaseWeb);

        final JudicialOfficer judge = findJudge(courtCaseWeb);

        caseRelated.setJudge(judge);
        caseRelatedDao.save(caseRelated);
        LOGGER.info("Judge with the name " + courtCaseWeb.getJudicialOfficer() + " allocated for the case : " + courtCaseWeb.getCrestCaseNumber());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deallocateJudgeForCase(final CourtCaseWeb courtCaseWeb) {

        findCentre(courtCaseWeb);

        final CaseRelated caseRelated = findCaseByCentreAndNo(courtCaseWeb);

        caseRelated.setJudge(null);
        caseRelatedDao.save(caseRelated);
        LOGGER.info("Judge deallocated for the case : " + courtCaseWeb.getCrestCaseNumber());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteNotAvailableDateForCase(final Long notAvailableDateId) {
        final CrustNonAvailableDates notAvailableDatedb = notAvailableDateDao.findById(notAvailableDateId);
        if (notAvailableDatedb != null) {
            notAvailableDateDao.delete(notAvailableDatedb);
        } else {
            throw new CcsException(getMessage("ENTITY_DOES_NOT_EXSIST"));
        }
        LOGGER.info("Not available date is deleted : " + notAvailableDateId);
    }

    @Override
    public CourtCaseWeb getCaseDetailsByCrestNumber(final String crestCaseNumber) {
        CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CaseRelated caseRelated = findCaseByNo(crestCaseNumber);
        courtCaseWeb = DataTransformationHelper.caseRelatedToWeb(caseRelated);
        return courtCaseWeb;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeCaseNoteForCase(final String crestCaseNumber, final String description) {
        if (StringUtils.isNotBlank(crestCaseNumber) && StringUtils.isNotBlank(description)) {
            final Long desc = new Long(description);
            final CaseNote caseNote = caseNoteDao.findNoteByCrestCaseNumberAndDescription(crestCaseNumber, desc);
            if (caseNote != null) {
                caseNoteDao.delete(caseNote);
            }
        } else {
            throw new CcsException(getMessage("THE_INFORMATION_TO_REMOVE_THE_NOTE_IS_NOT_COMPLETE"));
        }
        LOGGER.info("Case note has been removed from case " + crestCaseNumber);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeDefendantForCase(final String crestCaseNumber, final String personFullName) {

        if (StringUtils.isNotBlank(crestCaseNumber) && StringUtils.isNotBlank(personFullName)) {
            final PersonInCase personInCase = personInCaseDao.findPersonInCaseByCrestCaseNumberAndPersonName(crestCaseNumber, personFullName);
            personInCaseDao.delete(personInCase);
        }
        LOGGER.info("Defendant " + personFullName + " has been removed from case " + crestCaseNumber);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeLinkedCaseForCase(final String centerName, final String crestCaseNumber, final String crestLinkedNumber) {
        if (StringUtils.isNotBlank(crestCaseNumber) && StringUtils.isNotBlank(crestLinkedNumber) && StringUtils.isNotBlank(centerName)) {

            final CourtCenter courtcenter = courtCenterDao.findCourtCentreByName(centerName);
            if (courtcenter == null) {
                throw new CcsException(getMessage("ERROR_COURT_CENTER_NOT_EXIST"));
            }

            final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumber, centerName);
            if (caseRelated == null) {
                throw new CcsException(getMessage("CASE_CREST_NUMBER_NOT_EXIST"));
            }
            removeCaseRelated(crestLinkedNumber, caseRelated);

        } else {
            throw new CcsException(getMessage("THE_INFORMATION_TO_UNLINK_THE_CASE_IS_NOT_COMPLETE"));
        }
        LOGGER.info("Linked case " + crestLinkedNumber + "has been removed from case " + crestCaseNumber);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUpdateCaseNoteForCase(final CourtCaseWeb courtCaseWeb) {

        findCentre(courtCaseWeb);

        final CaseRelated caseRelated = findCaseByCentreAndNo(courtCaseWeb);
        if (courtCaseWeb.getNotesWeb().isEmpty()) {
            throw new CcsException(getMessage("PLEASE_PROVIDE_THE_NOTE_INFORMATION"));
        }

        for (final CaseNoteWeb caseNoteWeb : courtCaseWeb.getNotesWeb()) {
            CaseNote caseNote = new CaseNote();
            if (StringUtils.isNotBlank(caseNoteWeb.getNote())) {
                caseNote = setCaseNote(caseRelated, caseNoteWeb);
                caseNoteDao.save(caseNote);
            } else {
                throw new CcsException(getMessage("PLEASE_PROVIDE_THE_NOTE_INFORMATION"));
            }
        }
        LOGGER.info("Notes created for the case : " + courtCaseWeb.getCrestCaseNumber());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUpdateCaseRelated(final CourtCaseWeb courtCaseWeb) {
        validaReleaseDecision(courtCaseWeb);
        final CourtCenter courtcenter = findCentre(courtCaseWeb);

        CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(courtCaseWeb.getCrestCaseNumber(), courtCaseWeb.getCourtCenter());
        if (caseRelated == null) {
            caseRelated = new CaseRelated();
        }

        caseRelated.setCourtCenter(courtcenter);

        if (courtCaseWeb.checkValidObject()) {
            saveCase(courtCaseWeb, caseRelated);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUpdateDefendantForCase(final CourtCaseWeb courtCaseWeb) {

        findCentre(courtCaseWeb);

        final CaseRelated caseRelated = findCaseByCentreAndNo(courtCaseWeb);
        if (courtCaseWeb.getPersonInCaseList().isEmpty()) {
            throw new CcsException(getMessage("PLEASE_PROVIDE_DEFENDANT_INFORMATION"));
        }

        for (final PersonInCaseWeb personInCaseWeb : courtCaseWeb.getPersonInCaseList()) {
            saveDefendant(courtCaseWeb, caseRelated, personInCaseWeb);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUpdateLinkedCaseForCase(final CourtCaseWeb courtCaseWeb) {

        findCentre(courtCaseWeb);

        final CaseRelated caseRelated = findCaseByCentreAndNo(courtCaseWeb);
        if (courtCaseWeb.getLinkedCases().isEmpty()) {
            throw new CcsException(getMessage("PLEASE_PROVIDE_THE_CREST_NUMBER_OF_THE_LINKED_CASE"));
        }

        for (final CourtCaseWeb linkedCaseWeb : courtCaseWeb.getLinkedCases()) {
            final CaseRelated linkedCase = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(linkedCaseWeb.getCrestCaseNumber(), courtCaseWeb.getCourtCenter());
            if (linkedCase == null) {
                throw new CcsException("Case " + linkedCaseWeb.getCrestCaseNumber() + " does not exist");
            }
            caseRelated.getLinkedCases().add(linkedCase);
            caseRelatedDao.save(caseRelated);
            LOGGER.info("Linked case " + linkedCaseWeb.getCrestCaseNumber() + " added for the case : " + courtCaseWeb.getCrestCaseNumber());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUpdateNotAvailableDateForCase(final NotAvailableDatesWeb notAvailableDatesWeb) {

        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumber(notAvailableDatesWeb.getCrestCaseNumber());
        if (caseRelated == null) {
            throw new CcsException(getMessage("CASE_CREST_NUMBER_NOT_EXIST"));
        }

        CrustNonAvailableDates notAvailableDate = new CrustNonAvailableDates();

        if (notAvailableDatesWeb.getId() != null) {
            final CrustNonAvailableDates notAvailableDatedb = notAvailableDateDao.findById(notAvailableDatesWeb.getId());
            if (notAvailableDatedb != null) {
                notAvailableDate = notAvailableDatedb;
            }
        }

        validateNotAvailableDate(notAvailableDatesWeb, caseRelated, notAvailableDate);
        LOGGER.info("Not available date created for the case : " + notAvailableDatesWeb.getCrestCaseNumber() + ", with the reason " + notAvailableDatesWeb.getReason());
    }

    private CaseRelated findCaseByCentreAndNo(final CourtCaseWeb courtCaseWeb) {
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(courtCaseWeb.getCrestCaseNumber(), courtCaseWeb.getCourtCenter());
        if (caseRelated == null) {
            throw new CcsException(getMessage("CASE_CREST_NUMBER_NOT_EXIST"));
        }
        return caseRelated;
    }

    private CaseRelated findCaseByNo(final String crestCaseNumber) {
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber);
        if (caseRelated == null) {
            throw new CcsException(getMessage("CASE_NOT_FOUND_BY_CREST_CASE_NUMBER") + crestCaseNumber);
        }
        return caseRelated;
    }

    private CourtCenter findCentre(final CourtCaseWeb courtCaseWeb) {
        final CourtCenter courtcenter = courtCenterDao.findCourtCentreByName(courtCaseWeb.getCourtCenter());
        if (courtcenter == null) {
            throw new CcsException(getMessage("ERROR_COURT_CENTER_NOT_EXIST"));
        }
        return courtcenter;
    }

    private JudicialOfficer findJudge(final CourtCaseWeb courtCaseWeb) {
        final JudicialOfficer judge = judicialOfficerDao.findJudicialOfficerByName(courtCaseWeb.getJudicialOfficer());

        if (judge == null) {
            throw new CcsException(getMessage("JUDGE_NAME_NOT_EXIST"));
        }
        return judge;
    }

    private void removeCaseRelated(final String crestLinkedNumber, final CaseRelated caseRelated) {
        boolean toSave = false;
        final Iterator<CaseRelated> it = caseRelated.getLinkedCases().iterator();
        while (it.hasNext()) {
            final CaseRelated linkedCase = it.next();
            if (linkedCase.getCrestCaseNumber().equals(crestLinkedNumber)) {
                it.remove();
                toSave = true;
            }
        }
        if (toSave) {
            caseRelatedDao.save(caseRelated);
        }
    }

    private void saveCase(final CourtCaseWeb courtCaseWeb, final CaseRelated caseRelated) {
        caseRelated.setCrestCaseNumber(courtCaseWeb.getCrestCaseNumber());
        caseRelated.setNumberOfDefendant(courtCaseWeb.getNumberOfDefendent());
        caseRelated.setTrialEstimate(courtCaseWeb.getTrialEstimate());
        if (Double.compare(courtCaseWeb.getTrialEstimate(), 0) == 0) {
            caseRelated.setEstimationUnit(TimeEstimationUnit.HOURS);
        } else {
            if (courtCaseWeb.getTrialEstimateUnit() != null) {
                caseRelated.setEstimationUnit(TimeEstimationUnit.valueOf(courtCaseWeb.getTrialEstimateUnit().toUpperCase()));
            } else {
                caseRelated.setEstimationUnit(TimeEstimationUnit.DAYS);
            }
        }
        caseRelated.setOffenceClass(OffenceClass.getOffenceClass(courtCaseWeb.getOffenceClass()));
        if (courtCaseWeb.getLeadDefendant().contains("and")) {
            caseRelated.setLeadDefendant(courtCaseWeb.getLeadDefendant().substring(0, courtCaseWeb.getLeadDefendant().indexOf("and")).trim());
        } else {
            caseRelated.setLeadDefendant(courtCaseWeb.getLeadDefendant());
        }
        caseRelated.setMostSeriousOffence(courtCaseWeb.getMostSeriousOffence());
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.getReleaseDecision(courtCaseWeb.getReleaseDecisionStatus()));
        caseRelated.setTicketingRequirement(TicketingRequirement.valueOf(courtCaseWeb.getTicketingRequirement().substring(0, 3).toUpperCase()));
        caseRelated.setDateOfSending(courtCaseWeb.getDateOfSending());
        caseRelated.setDateOfCommittal(courtCaseWeb.getDateOfCommittal());
        if ((courtCaseWeb.getFromCrest() != null) && courtCaseWeb.getFromCrest().equals(Boolean.TRUE)) {
            caseRelated.setFromCrest(courtCaseWeb.getFromCrest());
        }
        if (courtCaseWeb.getCaseCompleted() != null) {
            caseRelated.setCaseClosed(courtCaseWeb.getCaseCompleted());
        }
        JudicialOfficer judge = null;
        if (courtCaseWeb.getJudicialOfficer() != null) {
            judge = judicialOfficerDao.findJudicialOfficerByName(courtCaseWeb.getJudicialOfficer());
        }

        caseRelated.setJudge(judge);

        caseRelatedDao.save(caseRelated);

    }

    private void saveDefendant(final CourtCaseWeb courtCaseWeb, final CaseRelated caseRelated, final PersonInCaseWeb personInCaseWeb) {
        PersonInCase personInCase = null;
        if (StringUtils.isNotBlank(personInCaseWeb.getOriginalFullname())) {
            // Update personInCase
            personInCase = personInCaseDao.findPersonInCaseByCrestCaseNumberAndPersonName(courtCaseWeb.getCrestCaseNumber(), personInCaseWeb.getOriginalFullname());
            if (personInCase == null) {
                throw new CcsException(getMessage("DEFENDANT_CANNOT_BE_EMPTY"));
            } else {
                personInCase.getPerson().setPersonFullName(personInCaseWeb.getFullname());
            }

        } else {
            personInCase = new PersonInCase();
            final Person person = new Person();
            person.setPersonFullName(personInCaseWeb.getFullname());
            personDao.save(person);
            personInCase.setPerson(person);
        }
        personInCase.setCaseRelated(caseRelated);
        personInCase.setCtlExpiresOn(personInCaseWeb.getCtlExpiryDate());
        personInCase.setRoleInCase(RoleInCase.DEFENDANT);
        personInCase.setCaseUrn(personInCaseWeb.getCrestURN());
        personInCase.setCustodyStatus(CustodyStatus.valueOf(StringUtils.deleteWhitespace(personInCaseWeb.getCustodyStatus().toUpperCase())));

        personInCaseDao.save(personInCase);
    }

    private CaseNote setCaseNote(final CaseRelated caseRelated, final CaseNoteWeb caseNoteWeb) {
        final CaseNote result = new CaseNote();
        if (caseNoteWeb.getNote().length() > 3069) {
            throw new CcsException(getMessage("MAXIMUM_FIELD_SIZE_IS_3069_CHARACTERS"));
        } else {
            result.setNote(caseNoteWeb.getNote());
            if (caseNoteWeb.getCreationDate() != null) {
                result.setCreationDate(caseNoteWeb.getCreationDate());
            } else {
                result.setCreationDate(new Date());
            }
            result.setCaseRelated(caseRelated);
            result.setDiaryDate(caseNoteWeb.getDiaryDate());
        }
        return result;
    }

    private void validaReleaseDecision(final CourtCaseWeb courtCaseWeb) {
        if (ReleaseDecision.NJ.getValue().equals(courtCaseWeb.getReleaseDecisionStatus())
                && ((courtCaseWeb.getJudicialOfficer() == null) || "No Judge".equalsIgnoreCase(courtCaseWeb.getJudicialOfficer()))) {
            throw new CcsException(getMessage("MISSING_JUDGE_ON_RELEASE_DECISION"));
        }
        LOGGER.info("case saved with the crest case number : " + courtCaseWeb.getCrestCaseNumber() + " and the center " + courtCaseWeb.getCourtCenter());
    }

    private void validateNotAvailableDate(final NotAvailableDatesWeb notAvailableDatesWeb, final CaseRelated caseRelated, final CrustNonAvailableDates notAvailableDate) {
        if (StringUtils.isNotBlank(notAvailableDatesWeb.getReason()) && (notAvailableDatesWeb.getStartDate() != null) && (notAvailableDatesWeb.getEndDate() != null)) {
            if (notAvailableDatesWeb.getReason().length() > 100) {
                throw new CcsException(getMessage("MAXIMUM_FIELD_SIZE_IS_100_CHARACTERS"));
            }
            final Date startDate = DateTimeUtils.getBeginningOfTheDay(notAvailableDatesWeb.getStartDate());
            final Date endDate = DateTimeUtils.getBeginningOfTheDay(notAvailableDatesWeb.getEndDate());
            if (startDate.after(endDate)) {
                throw new CcsException(getMessage("START_DAY_BEFORE_ENDDATE", new String[] {}, new String[] { DateTimeUtils.formatToStandardPattern(startDate) }));
            }
            notAvailableDate.setReason(notAvailableDatesWeb.getReason());
            notAvailableDate.setStartDate(startDate);
            notAvailableDate.setEndDate(endDate);
            notAvailableDate.setCaseRelated(caseRelated);
            notAvailableDateDao.save(notAvailableDate);
        } else {
            throw new CcsException(getMessage("MANDATORY_INFO_NOT_AVAILABLE_DATES"));
        }

    }

}
