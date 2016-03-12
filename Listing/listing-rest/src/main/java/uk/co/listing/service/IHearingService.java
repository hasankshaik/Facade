package uk.co.listing.service;

import java.util.List;
import java.util.Map;

import uk.co.listing.rest.response.HearingWeb;

public interface IHearingService {
    public HearingWeb saveUpdateHearing(HearingWeb hearingWeb);

    public HearingWeb listHearing(HearingWeb hearingWeb);

    public List<HearingWeb> getUnlistedHearing();

    public HearingWeb getHearingDetail(String hearingKey);

    public void unListHearing(String hearingKey, boolean active);

    public HearingWeb reListingHearing(HearingWeb hearingWeb);

    public void autoScheduleHearing(HearingWeb hearingWeb, String courtCenterName);

    public Map<String, List<HearingWeb>> getPcmHearingForAction();

    public void updatePcmHearingStatus(HearingWeb hearingWeb);
}
