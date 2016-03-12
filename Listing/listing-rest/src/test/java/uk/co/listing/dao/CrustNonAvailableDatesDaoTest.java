package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.TicketingRequirement;

public class CrustNonAvailableDatesDaoTest extends BaseTransactionalIntegrationTest {

    private static final String TEST_CENTER = "test-center-name";

    @Autowired
    private CrustNonAvailableDatesDao crustNonAvailableDatesDao;

    @Autowired
    private CaseRelatedDao caseRelatedDao;

    @Test
    public void testFindByName() {

        final CourtCenter center = new CourtCenter(4004L, TEST_CENTER);
        caseRelatedDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber("CrustNonAvailableDatesDaoTest");
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        caseRelated.setCourtCenter(center);

        caseRelatedDao.save(caseRelated);

        final CrustNonAvailableDates crustNonAvailableDates = new CrustNonAvailableDates();
        crustNonAvailableDates.setStartDate(new Date());
        crustNonAvailableDates.setEndDate(new Date());
        crustNonAvailableDates.setCaseRelated(caseRelated);
        crustNonAvailableDates.setReason("Testing findCrestNonAvailableDatesByProperties");

        crustNonAvailableDatesDao.save(crustNonAvailableDates);

        final CrustNonAvailableDates results = crustNonAvailableDatesDao.findCrestNonAvailableDatesByProperties(crustNonAvailableDates);
        assertEquals(crustNonAvailableDates.getReason(), results.getReason());
    }

}
