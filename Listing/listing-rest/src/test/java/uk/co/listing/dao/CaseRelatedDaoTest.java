package uk.co.listing.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.TicketingRequirement;

public class CaseRelatedDaoTest extends BaseTransactionalIntegrationTest {

    private static final String TEST_CENTRE = "test-centre";

    private static final String crestCaseNumber = "test-crestCaseNumber";

    @Autowired
    private CaseRelatedDao caseRelatedDao;

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Test
    public void testFindCaseByCrestCaseNumber() {
        assertTrue(caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber) == null);

        final CourtCenter center = new CourtCenter(4004L, TEST_CENTRE);
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        caseRelated.setCourtCenter(center);

        final Set<CaseNote> listCaseNote = new HashSet<CaseNote>();
        final CaseNote caseNote = new CaseNote();
        caseNote.setCreationDate(new Date());
        caseNote.setDiaryDate(new Date());
        caseNote.setNote("note");
        listCaseNote.add(caseNote);
        caseRelated.setNotes(listCaseNote);

        caseRelatedDao.save(caseRelated);

        final CaseRelated caseRelatedUpdated = caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber);
        assertTrue(caseRelatedUpdated.getNotes().size() > 0);
        final Set<CaseNote> listCaseNoteu = caseRelatedUpdated.getNotes();
        for (final CaseNote caseNote2 : listCaseNoteu) {

            assertTrue(caseNote2.getNote().equals("note"));
        }

        caseRelatedUpdated.setNotes(new HashSet<CaseNote>());
        caseRelatedDao.save(caseRelated);

        final CaseRelated caseRelatedUpdated1 = caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber);

        assertTrue(caseRelatedUpdated1.getNotes().size() == 0);
    }

    @Test
    public void testFindCaseByCrustCaseNumber() {
        assertTrue(caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber) == null);

        final CourtCenter center = new CourtCenter(4041L, TEST_CENTRE);
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);
        final CaseRelated caseRelatedUpdated = caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber);
        assertTrue(crestCaseNumber.equals(caseRelatedUpdated.getCrestCaseNumber()));
    }

    @Test
    public void testFindCaseByCrestCaseNumberAndCenter() {
        assertTrue(caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber) == null);

        final CourtCenter center = new CourtCenter(4041L, TEST_CENTRE);
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);
        final CaseRelated caseRelatedUpdated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumber, TEST_CENTRE);
        assertTrue(crestCaseNumber.equals(caseRelatedUpdated.getCrestCaseNumber()));
    }

    @Test
    public void testFindCaseByCrestCaseNumberAndWrongCenter() {
        assertTrue(caseRelatedDao.findCaseByCrestCaseNumber(crestCaseNumber) == null);

        final CourtCenter center = new CourtCenter(4050L, "Liverpool");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);
        final CaseRelated caseRelatedUpdated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumber, TEST_CENTRE);
        assertNull(caseRelatedUpdated);
    }

}
