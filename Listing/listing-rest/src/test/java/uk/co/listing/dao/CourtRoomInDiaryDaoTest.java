package uk.co.listing.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtRoomInDiary;

public class CourtRoomInDiaryDaoTest extends BaseTransactionalIntegrationTest {

    private static final String centerName = "test-center-1";
    private static final String roomName = "test-room-1";

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private CourtRoomDao courtRoomDao;

    @Autowired
    private CourtDiaryDao courtDiaryDao;

    @Autowired
    private CourtRoomInDiaryDao courtRoomInDiaryDao;

    @Test
    public void testFindCourtRooms() {

        courtCenterDao.save(new CourtCenter(1000L, centerName));
        CourtCenter courtCenter = courtCenterDao.findCourtCentreByName(centerName);
        courtDiaryDao.save(new CourtDiary(courtCenter));
        CourtDiary courtDiary = courtDiaryDao.findCourtDiary(centerName);
        courtRoomDao.save(new CourtRoom(roomName));
        CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(roomName);

        courtRoomInDiaryDao.save(new CourtRoomInDiary(courtRoom, courtDiary));
    }

}
