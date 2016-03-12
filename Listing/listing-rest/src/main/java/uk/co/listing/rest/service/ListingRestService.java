package uk.co.listing.rest.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;
import static uk.co.listing.rest.response.ComplexResponse.buildBadRequestResponse;
import static uk.co.listing.rest.response.ComplexResponse.buildOkResponse;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.listing.rest.response.ComplexResponse;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;
import uk.co.listing.rest.response.RoomDateForHearing;
import uk.co.listing.service.ICourtCaseService;
import uk.co.listing.service.ICourtListingService;
import uk.co.listing.service.ICrestDataImportService;
import uk.co.listing.service.IEnumService;
import uk.co.listing.service.IHearingService;
import uk.co.listing.service.IHearingSlotService;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Component
@Path("/listing")
public class ListingRestService {

    @Autowired
    ICourtListingService courtListingService;

    @Autowired
    ICourtCaseService courtCaseService;

    @Autowired
    IHearingService hearingService;

    @Autowired
    IHearingSlotService hearingSlotService;

    @Autowired
    ICrestDataImportService crestDataImportService;

    @Autowired
    IEnumService enumService;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") final InputStream uploadedInputStream, @FormDataParam("file") final FormDataContentDisposition fileDetail) {
        final String uploadedFileLocation = "c://temp/" + fileDetail.getFileName();
        crestDataImportService.importCrestData(uploadedInputStream, fileDetail.getFileName());
        final String output = "File uploaded to : " + uploadedFileLocation;
        return buildOkResponse(output);
    }

    @GET
    @Path("/crestdata/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCrestDataProcessingStatus() {
        return ComplexResponse.buildOkResponse(crestDataImportService.getCrestDataProcessingStatus());
    }

    @GET
    @Path("/courtcenters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourtCenters() {
        return ComplexResponse.buildOkResponse(courtListingService.getListOfCourtCenters());
    }

    @GET
    @Path("/courtrooms/centername/{courtCenterName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourtRooms(@PathParam("courtCenterName") final String courtCenterName) {
        return ComplexResponse.buildOkResponse(courtListingService.getListOfCourtRooms(courtCenterName));
    }

    @GET
    @Path("/planner/{courtCenterName}/daterange/{startdate}/{enddate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourtRoomSitting(@PathParam("courtCenterName") final String courtCenterName, @PathParam("startdate") final Date startDate, @PathParam("enddate") final Date endDate) {
        return ComplexResponse.buildOkResponse(courtListingService.getListOfCourtRoomSitting(courtCenterName, startDate, endDate));
    }

    @GET
    @Path("/case/{crestCaseNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourtCase(@PathParam("crestCaseNumber") final String crestCaseNumber) {
        return ComplexResponse.buildOkResponse(courtCaseService.getCaseDetailsByCrestNumber(crestCaseNumber));
    }

    @POST
    @Path("/casesRelated")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUpdateCaseRelated(final CourtCaseWeb courtCaseWeb) {
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/casesRelated/Defendent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDefendentsForCase(final CourtCaseWeb courtCaseWeb) {
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/casesRelated/casenote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCaseNoteForCase(final CourtCaseWeb courtCaseWeb) {
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/casesRelated/linkedcase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLinkedCaseForCase(final CourtCaseWeb courtCaseWeb) {
        courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/casesRelated/notavailabledate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNotAvailableDateForCase(final NotAvailableDatesWeb notAvailableDatesWeb) {
        courtCaseService.saveUpdateNotAvailableDateForCase(notAvailableDatesWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/casesRelated/allocatejudge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response allocatejudge(final CourtCaseWeb courtCaseWeb) {
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/casesRelated/deallocatejudge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deallocatejudge(final CourtCaseWeb courtCaseWeb) {
        courtCaseService.deallocateJudgeForCase(courtCaseWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @DELETE
    @Path("/casesRelated/notavailabledate/{notAvailableDateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteNotAvailableDateForCase(@PathParam("notAvailableDateId") final Long notAvailableDateId) {
        courtCaseService.deleteNotAvailableDateForCase(notAvailableDateId);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/hearings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUpdateHearing(final HearingWeb hearingWeb) {
        hearingService.saveUpdateHearing(hearingWeb);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @POST
    @Path("/hearings/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listHearing(final HearingWeb hearingWeb) {
        return ComplexResponse.buildOkResponse(hearingService.listHearing(hearingWeb));
    }

    @POST
    @Path("/hearings/relist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reListingHearing(final HearingWeb hearingWeb) {
        return ComplexResponse.buildOkResponse(hearingService.reListingHearing(hearingWeb));
    }

    @GET
    @Path("/hearings/unlisted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHearing() {
        return ComplexResponse.buildOkResponse(hearingService.getUnlistedHearing());
    }

    @DELETE
    @Path("/hearings/{hearingKey}/{hearingStatus}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unListingHearing(@PathParam("hearingKey") final String hearingKey, @PathParam("hearingStatus") final boolean active) {
        hearingService.unListHearing(hearingKey, active);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @GET
    @Path("/hearings/{hearingKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHearingDetail(@PathParam("hearingKey") final String hearingKey) {
        return ComplexResponse.buildOkResponse(hearingService.getHearingDetail(hearingKey));
    }

    @GET
    @Path("/hearings/foraction")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPcmHearingForAction() {
        return ComplexResponse.buildOkResponse(hearingService.getPcmHearingForAction());
    }

    @POST
    @Path("/hearings/pcmhaction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePcmHearingStatus(final HearingWeb hearingWeb) {
        hearingService.updatePcmHearingStatus(hearingWeb);
        return ComplexResponse.buildOkResponseNoContent();

    }

    /**
     * call this to get available slot dates together with the number of
     * existing hearings per court-room
     *
     * @param courtCentreName
     * @param hearingEstimate
     * @return
     */
    @GET
    @Path("/hearing/slots/{courtCentreName}/{hearingKey}/{hearingEstimate}/{overbook}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHearingSlotDates(@PathParam("courtCentreName") final String courtCentreName, @PathParam("hearingKey") final String hearingKey,
            @PathParam("hearingEstimate") final Integer hearingEstimate, @PathParam("overbook") final boolean overbook) {
        if (StringUtils.isBlank(courtCentreName)) {
            return buildBadRequestResponse(getMessage("court.centre.empty"));
        }
        if ((hearingEstimate == null) || (hearingEstimate.intValue() == 0)) {
            return buildBadRequestResponse(getMessage("hearing.estimate.empty"));
        }
        final List<RoomDateForHearing> hearingDateSlotList = hearingSlotService.getHearingDateSlotList(courtCentreName, hearingKey, hearingEstimate, overbook);
        return buildOkResponse(hearingDateSlotList);
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_HTML)
    public String getLogoutUrl() {
        return courtListingService.getLogoutUrl();
    }

    @DELETE
    @Path("/casesRelated/personInCase/{crestCaseNumber}/{personFullName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public Response removeDefendantForCase(@PathParam("crestCaseNumber") final String crestCaseNumber, @PathParam("personFullName") final String personFullName) {
        courtCaseService.removeDefendantForCase(crestCaseNumber, personFullName);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @DELETE
    @Path("/casesRelated/casenote/{crestCaseNumber}/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCaseNoteForCase(@PathParam("crestCaseNumber") final String crestCaseNumber, @PathParam("description") final String description) {
        courtCaseService.removeCaseNoteForCase(crestCaseNumber, description);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @DELETE
    @Path("/casesRelated/linkedcase/{centerName}/{crestCaseNumber}/{crestLinkedNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeLinkedCaseForCase(@PathParam("centerName") final String centerName, @PathParam("crestCaseNumber") final String crestCaseNumber,
            @PathParam("crestLinkedNumber") final String crestLinkedNumber) {
        courtCaseService.removeLinkedCaseForCase(centerName, crestCaseNumber, crestLinkedNumber);
        return ComplexResponse.buildOkResponseNoContent();
    }

    @GET
    @Path("/enums/offenceclass")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOffenceClass() {
        return ComplexResponse.buildOkResponse(enumService.getOffenceClass());
    }

    @GET
    @Path("/enums/ticketingrequirement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketingRequirement() {
        return ComplexResponse.buildOkResponse(enumService.getTicketingRequirement());
    }

    @GET
    @Path("/enums/releasedecision")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReleaseDecision() {
        return ComplexResponse.buildOkResponse(enumService.getReleaseDecision());
    }

    @GET
    @Path("/enums/custodystatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustodyStatus() {
        return ComplexResponse.buildOkResponse(enumService.getCustodyStatus());
    }

    @GET
    @Path("/enums/timeunits")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimeUnits() {
        return ComplexResponse.buildOkResponse(enumService.getTimeUnits());
    }

    @GET
    @Path("/enums/processingstatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProcessingStatus() {
        return ComplexResponse.buildOkResponse(enumService.getProcessingStatus());
    }

    @GET
    @Path("/enums/sessionblocktype")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSessionBlockType() {
        return ComplexResponse.buildOkResponse(enumService.getSessionBlockType());
    }

    @GET
    @Path("/enums/judicialofficertype")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJudicialOfficerType() {
        return ComplexResponse.buildOkResponse(enumService.getJudicialOfficerType());
    }

    @GET
    @Path("/enums/judicialtickets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJudicialTickets() {
        return ComplexResponse.buildOkResponse(enumService.getJudicialTickets());
    }

}