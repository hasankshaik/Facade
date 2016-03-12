package uk.co.listing.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.SittingDao;
import uk.co.listing.dao.SittingTargetDao;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.SittingTarget;
import uk.co.listing.rest.response.SittingTargetWeb;
import uk.co.listing.utils.DateTimeUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateTimeUtils.class)
public class SittingServiceTest extends BaseMockingUnitTest {

	@Mock
	SittingTargetDao mockSittingTargetDao;

	@Mock
	CourtCenterDao mockCourtCenterDao;

	@Mock
	private SittingDao sittingDaoMock;

	@InjectMocks
	SittingService sittingService;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(DateTimeUtils.class);
		when(mockCourtCenterDao.findCourtCentreByNameOrCode("test-court", null)).thenReturn(new CourtCenter(1000L, "test-court"));
	}

	@Test
	public void testGetAnnualSitting() {
		sittingService.getAnnualSittingsTarget("courtCenter", "2015-2016");
		verify(mockSittingTargetDao).findSittingTarget("courtCenter", "2015-2016");
	}

	@Test
	public void testSaveAnnualSittingWhenThereisNoExistingSitting() {
		when(mockSittingTargetDao.findSittingTarget("test-court", "2015-2016")).thenReturn(null);

		SittingTargetWeb annualSittingWeb = new SittingTargetWeb();
		annualSittingWeb.setCourtCenter("test-court");
		
		sittingService.saveAnnualSittingTarget(annualSittingWeb);

		verify(mockCourtCenterDao).findCourtCentreByName("test-court");
		verify(mockSittingTargetDao).save(any(SittingTarget.class));
	}

	@Test
	public void testSaveAnnualSittingWhenThereisAnExistingSitting() {
		SittingTarget sittingTarget = new SittingTarget(new CourtCenter(1000L, "test-court"), "2015-2016", new Long(500));
		when(mockSittingTargetDao.findSittingTarget("test-court", "2015-2016")).thenReturn(sittingTarget);

		SittingTargetWeb annualSittingWeb = new SittingTargetWeb();
		annualSittingWeb.setCourtCenter("test-court");
		annualSittingWeb.setFinancialYear("2015-2016");
		sittingService.saveAnnualSittingTarget(annualSittingWeb);

		verifyZeroInteractions(mockCourtCenterDao);
		verify(mockSittingTargetDao).save(sittingTarget);
	}
	

    @Test
	public void getAnnualActualSittingDays() {
        when(sittingDaoMock.countActualSittingDaysBetweenDates(Mockito.anyString(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(0L);
		sittingService.getAnnualActualSittingDays("Birmingham", "2015-2016");

		PowerMockito.verifyStatic();
		DateTimeUtils.getFinancialStartDateForYear("2015");

		PowerMockito.verifyStatic();
		DateTimeUtils.getFinancialEndDateForYear("2015");
		
        verify(sittingDaoMock, times(1)).countActualSittingDaysBetweenDates(Mockito.anyString(), Mockito.any(Date.class), Mockito.any(Date.class));
    }
    
    @Test
    public void getAnnualActualSittingDaysTest(){
        Date now = new Date();
        String court = "test-centre";
        Mockito.when(sittingDaoMock.countActualSittingDaysBetweenDates(Mockito.anyString(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(5L);
        Long result = sittingService.getAnnualActualSittingDays(court, now);
        assertTrue(result== 5L);
    }

    @Test
	public void shouldGetMonthlyActualSittings() {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		startCalendar.set(Calendar.MONTH, 3);
		startCalendar.set(Calendar.YEAR, 2015);
		startCalendar.set(Calendar.HOUR_OF_DAY, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		Date startDate = startCalendar.getTime();
		log.info("startdate:" +startDate);
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Calendar.DAY_OF_MONTH, 30);
		endCalendar.set(Calendar.MONTH, 3);
		endCalendar.set(Calendar.YEAR, 2015);
		endCalendar.set(Calendar.HOUR_OF_DAY, 0);
		endCalendar.set(Calendar.MINUTE, 0);
		endCalendar.set(Calendar.SECOND, 0);
		Date endDate = endCalendar.getTime();
		log.info("enddate:" +endDate);
		
		Mockito.when(DateTimeUtils.getFinancialStartDateForYear("2015")).thenReturn(startCalendar.getTime());
		Mockito.when(DateTimeUtils.getFinancialEndDateForYear("2015")).thenReturn(endCalendar.getTime());
		
		sittingService.getMonthlyActualSittingDays("test-centre", "2015-2016");
		
		PowerMockito.verifyStatic();
		DateTimeUtils.getFinancialStartDateForYear("2015");
		PowerMockito.verifyStatic();
		DateTimeUtils.getFinancialEndDateForYear("2015");
		verify(sittingDaoMock).countActualSittingDaysBetweenDates(eq("test-centre"), any(Date.class), any(Date.class));
	}

}
