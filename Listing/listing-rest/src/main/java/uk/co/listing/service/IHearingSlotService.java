package uk.co.listing.service;

import java.util.List;

import uk.co.listing.rest.response.RoomDateForHearing;

public interface IHearingSlotService {

    /**
     * @return top 10 hearing date slots in the next three years across all court rooms in the order of increaing overbook count degree
     */
    public abstract List<RoomDateForHearing> getHearingDateSlotList(String courtCenterName, final String hearingKey, int hearingEstimate, boolean overbook);

}