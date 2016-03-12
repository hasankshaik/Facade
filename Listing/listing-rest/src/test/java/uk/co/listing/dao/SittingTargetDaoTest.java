package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.MonthlyTarget;
import uk.co.listing.domain.SittingTarget;

public class SittingTargetDaoTest extends BaseTransactionalIntegrationTest {

	private static final String TEST_CENTER = "test-center-name";

	@Autowired
	private SittingTargetDao sittingTargetDao;

	@Autowired
	private CourtCenterDao courtCenterDao;
	
	@Test
	public void shoudlSaveAndGetAnnualSittingsCount() {
		CourtCenter courtCenter = new CourtCenter(1000L, TEST_CENTER);
		courtCenterDao.save(courtCenter);
		
		SittingTarget target = new SittingTarget(courtCenter, "2015-2016", new Long(800));
		sittingTargetDao.save(target);
		
		assertNotNull(sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2015-2016"));
		assertEquals(new Long(800), sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2015-2016"));
	}

	@Test
	public void getAnnualSittingsCountWhenNoSittings() {
		CourtCenter courtCenter = new CourtCenter(1000L, TEST_CENTER);
		courtCenterDao.save(courtCenter);
		
		assertNotNull(sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2090-2091"));
		assertEquals(new Long(0), sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2090-2091"));
	}
	
	@Test
	public void shoudlSaveAndGetAnnualSittingsCountWithMultipleSittings() {
		CourtCenter courtCenter = new CourtCenter(1000L, TEST_CENTER);
		courtCenterDao.save(courtCenter);

		SittingTarget target = new SittingTarget(courtCenter, "2014-2015", new Long(100));
		sittingTargetDao.save(target);
		
		target = new SittingTarget(courtCenter, "2015-2016", new Long(200));
		sittingTargetDao.save(target);

		target = new SittingTarget(courtCenter, "2016-2017", new Long(300));
		sittingTargetDao.save(target);
		
		assertNotNull(sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2015-2016"));
		assertEquals(new Long(200), sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2015-2016"));
	}
	
	@Test
	public void shouldSaveMonthlyTargetsWithSittings() {
		CourtCenter courtCenter = new CourtCenter(1000L, TEST_CENTER);
		courtCenterDao.save(courtCenter);
		
		SittingTarget target = new SittingTarget(courtCenter, "2015-2016", new Long(800));
		HashSet<MonthlyTarget> monthlyTargets = new HashSet<MonthlyTarget>();
		
		MonthlyTarget monthlyTarget = new MonthlyTarget();
		monthlyTarget.setMonth("Jan");
		monthlyTarget.setSitting(400L);
		monthlyTargets.add(monthlyTarget);
		
		target.setMonthlyTargets(monthlyTargets);
		sittingTargetDao.save(target);
		
		assertEquals(new Long(800), sittingTargetDao.getAnnualSittingTarget(TEST_CENTER, "2015-2016"));
		SittingTarget findSittingResult = sittingTargetDao.findSittingTarget(TEST_CENTER, "2015-2016");
		assertNotNull(findSittingResult);
		Set<MonthlyTarget> monthlyTargetSetResult = findSittingResult.getMonthlyTargets();
		assertNotNull(monthlyTargetSetResult);
		MonthlyTarget[] array = monthlyTargetSetResult.toArray(new MonthlyTarget[0]);
		assertEquals("Jan", array[0].getMonth());
		assertEquals(400L, array[0].getSitting(), 0.0);
	}
    
    @Test
    public void shouldSaveAnnualSittings() {
        CourtCenter courtCenter = new CourtCenter(10000L, TEST_CENTER);
        HashSet<SittingTarget> sittingTargets = new HashSet<SittingTarget>();
        SittingTarget target = new SittingTarget(courtCenter, "2015/16", new Long(800));
        sittingTargets.add(target);
        courtCenter.setSittingTargets(sittingTargets);
        courtCenterDao.save(courtCenter);
        
        CourtCenter courtCenter_ret = courtCenterDao.findCourtCentreByName(TEST_CENTER);
        Set<SittingTarget> sittingTargets_ret = courtCenter_ret.getSittingTargets();
        assertFalse(sittingTargets_ret.isEmpty());
        assertEquals(1, sittingTargets_ret.size());
    }
}
