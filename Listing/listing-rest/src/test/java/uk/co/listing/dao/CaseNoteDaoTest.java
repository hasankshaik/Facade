package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;

public class CaseNoteDaoTest extends BaseTransactionalIntegrationTest {

    @Autowired
    private CaseNoteDao caseNoteDao;
    @Autowired
    private CaseRelatedDao caseRelatedDao;
    @Autowired
    private CourtCenterDao courtCenterDao;

    @Test
    public void findNoteByCrestCaseNumberAndDescriptionTest() {
        String crestCaseNumber = "T999999";
        CourtCenter center = new CourtCenter(700L, "test-centre");
        courtCenterDao.save(center);
        CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        CaseNote note = new CaseNote();
        note.setCaseRelated(caseRelated);
        note.setNote("Viva el Betis");
        note.setCreationDate(new Date());
        caseNoteDao.save(note);
        
        CaseNote noteSaved = caseNoteDao.findNoteByCrestCaseNumberAndDescription(crestCaseNumber, note.getId());
        assertNotNull(noteSaved);
        assertEquals(noteSaved.getNote(), note.getNote());
        assertEquals(noteSaved.getCaseRelated().getCrestCaseNumber(), crestCaseNumber);
        assertEquals(noteSaved.getCreationDate(), note.getCreationDate());
    }

    
}
