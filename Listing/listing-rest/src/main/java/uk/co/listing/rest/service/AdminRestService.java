package uk.co.listing.rest.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.listing.rest.response.ComplexResponse;
import uk.co.listing.rest.response.CourtCenterWeb;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.rest.response.CourtSessionSaveWeb;
import uk.co.listing.rest.response.JudicialOfficerWeb;
import uk.co.listing.rest.response.ManageSessionAction;
import uk.co.listing.service.ICourtListingService;
import uk.co.listing.service.IListingAdminService;

@Component
@Path("/adminservice")
public class AdminRestService {

    @Autowired
    private IListingAdminService listingAdminService;

    @Autowired
    private ICourtListingService courtListingService;

    @POST
    @Path("/judicialofficers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveJudicialOfficer(final JudicialOfficerWeb judicialOfficerWeb) {
        listingAdminService.saveJudicialOfficer(judicialOfficerWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @GET
    @Path("/judicialofficers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllJudicialOfficers() {
        return ComplexResponse.buildOkResponse(listingAdminService.findAllJudicialOfficers());
    }

    @POST
    @Path("/manageblocks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveSessionBlocks(final CourtSessionSaveWeb sessionWeb) {
        final CourtSessionSaveWeb sessionBlocks = listingAdminService.setSessionBlockWithType(sessionWeb, false);
        return ComplexResponse.buildOkResponse(sessionBlocks);
    }

    @DELETE
    @Path("/manageblocks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSessionBlocks(final CourtSessionSaveWeb sessionWeb) {
        final CourtSessionSaveWeb sessionBlocks = listingAdminService.setSessionBlockWithType(sessionWeb, true);
        return ComplexResponse.buildOkResponse(sessionBlocks);
    }

    @POST
    @Path("/managesessions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response manageCourtRoom(final ManageSessionAction manageRoomAction) {
        return ComplexResponse.buildOkResponse(listingAdminService.manageCourtSession(manageRoomAction));
    }


    @POST
    @Path("/allocatejudgeinroom")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCourtSession(final CourtSessionSaveWeb sessionWeb) {
        final CourtSessionSaveWeb allocateJudgeToCourtRoom = listingAdminService.allocateJudgeToCourtRoom(sessionWeb);
        return ComplexResponse.buildOkResponse(allocateJudgeToCourtRoom);
    }

    @POST
    @Path("/deallocatejudgeinroom")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJudgeFromCourtSession(final CourtSessionSaveWeb sessionWeb) {
        final CourtSessionSaveWeb courtCaseWeb = listingAdminService.deallocateJudgeFromCourtRoom(sessionWeb);
        return ComplexResponse.buildOkResponse(courtCaseWeb);
    }

    @POST
    @Path("/courtrooms")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveCourtRoom(final CourtRoomWeb courtRoomWeb) {
        listingAdminService.saveCourtRoom(courtRoomWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @PUT
    @Path("/courtcentre")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourtCentre(final CourtCenterWeb courtCentre) {
        courtListingService.addCourtCentreAndDiary(courtCentre);
        return ComplexResponse.buildCreatedResponse();
    }

    @GET
    @Path("/courtcentres")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourtCentre() {
        final List<CourtCenterWeb> courtCenterList = courtListingService.getListOfCourtCenters();
        return ComplexResponse.buildOkResponse(courtCenterList);
    }
}