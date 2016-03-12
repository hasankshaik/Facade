package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CourtCenter;

public class CourtCenterDaoTest extends BaseTransactionalIntegrationTest {

	private static final String TEST_CENTER = "test-center-name";

	@Autowired
	private CourtCenterDao courtCenterDao;

	@Test
	public void testFindByName() {
        courtCenterDao.save(new CourtCenter(1000L, TEST_CENTER));
	    CourtCenter findCourtCenter = courtCenterDao.findCourtCentreByNameOrCode(TEST_CENTER, null);
        assertEquals(TEST_CENTER, findCourtCenter.getCenterName());
        assertEquals(1000L, findCourtCenter.getCode().longValue());
	}

	@Test
	public void testFindByCode() {
	    courtCenterDao.save(new CourtCenter(1000L, TEST_CENTER));
	    CourtCenter findCourtCenter = courtCenterDao.findCourtCentreByNameOrCode(null, 1000L);
	    assertEquals(TEST_CENTER, findCourtCenter.getCenterName());
	    assertEquals(1000L, findCourtCenter.getCode().longValue());
	}
	
	@Test
	public void testSave() {
		assertTrue(courtCenterDao.findCourtCentreByNameOrCode(TEST_CENTER, null) == null);
		courtCenterDao.save(new CourtCenter(1000L, TEST_CENTER));
		CourtCenter courtCenter = courtCenterDao.findCourtCentreByNameOrCode(TEST_CENTER, null);
		assertTrue(TEST_CENTER.equals(courtCenter.getCenterName()));
	}

	@Test
	public void testFindAllCourtCenter() {
		int preSize = courtCenterDao.findAllCourtCenters().size();
		for (int i = 10; i < 14; i++) {
			courtCenterDao.save(new CourtCenter(2000L+i, TEST_CENTER+i));
		}
		int postSize = courtCenterDao.findAllCourtCenters().size();
		assertEquals(preSize + 4, postSize);
	}
}
