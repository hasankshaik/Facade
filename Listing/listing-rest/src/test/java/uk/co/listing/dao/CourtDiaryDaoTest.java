package uk.co.listing.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;

public class CourtDiaryDaoTest extends BaseTransactionalIntegrationTest {

    private final static String centerName = "London";

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private CourtDiaryDao courtDiaryDao;

    @Test
    public void testFindAllCourtRooms() {
        courtCenterDao.save(new CourtCenter(1000L, centerName));
        CourtCenter courtCenter = courtCenterDao.findCourtCentreByName(centerName);
        courtDiaryDao.save(new CourtDiary(courtCenter));
        CourtDiary courtDiary = courtDiaryDao.findCourtDiary(centerName);
        assertTrue(courtDiary.getCenter().getCenterName().equals(centerName));

    }

}
