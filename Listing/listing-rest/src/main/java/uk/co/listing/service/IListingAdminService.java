package uk.co.listing.service;

import java.util.List;

import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.rest.response.CourtSessionSaveWeb;
import uk.co.listing.rest.response.JudicialOfficerWeb;
import uk.co.listing.rest.response.ManageSessionAction;

public interface IListingAdminService {

    public JudicialOfficerWeb findJudicialOfficerByName(String name);

    public void saveJudicialOfficer(JudicialOfficerWeb judicialOfficerWeb);

    public List<JudicialOfficerWeb> findAllJudicialOfficers();

    public CourtSessionSaveWeb allocateJudgeToCourtRoom(CourtSessionSaveWeb sessionWeb);

    public CourtSessionSaveWeb deallocateJudgeFromCourtRoom(CourtSessionSaveWeb sessionWeb);

    public void saveCourtRoom(CourtRoomWeb courtRoomWeb);

    public CourtSessionSaveWeb setSessionBlockWithType(CourtSessionSaveWeb courtSessionWeb, boolean deleteBlocks);

    public ManageSessionAction manageCourtSession(ManageSessionAction manageRoomAction);

}