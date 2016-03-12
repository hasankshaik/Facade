package uk.co.listing.service;

import java.util.Date;
import java.util.List;

import uk.co.listing.rest.response.CourtCenterWeb;
import uk.co.listing.rest.response.CourtRoomWeb;

public interface ICourtListingService {

    public List<CourtCenterWeb> getListOfCourtCenters();

    public void addCourtCentreAndDiary(CourtCenterWeb courtCenterWeb);

    public List<CourtRoomWeb> getListOfCourtRooms(String courtCenterName);

    public List<CourtRoomWeb> getListOfCourtRoomSitting(String courtCenterName, Date startDate, Date endDate);

    public String getLogoutUrl();

}