package uk.co.listing.rest.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.listing.domain.MonthlyTarget;
import uk.co.listing.domain.SittingTarget;
import uk.co.listing.rest.response.ComplexResponse;
import uk.co.listing.rest.response.SittingTargetWeb;
import uk.co.listing.service.ICourtListingService;
import uk.co.listing.service.ISittingsService;

@Component
@Path("/sitting")
public class SittingsRestService {

    @Autowired
    ISittingsService sittingsService;

    @Autowired
    ICourtListingService courtListingService;

    @Autowired
    ISittingsService sittingService;

    @PUT
    @Path("/annual-sitting-target")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setAnnualSittingTarget(final SittingTargetWeb target) {
        sittingsService.saveAnnualSittingTarget(target);
        return ComplexResponse.buildCreatedResponse(target.getCourtCenter() + "/" + target.getFinancialYear());
    }

    @GET
    @Path("/annual-sitting-target/{courtCenter}/{financialYear}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnualSittingTarget(@PathParam("courtCenter") final String courtCenter, @PathParam("financialYear") final String year) {
        final SittingTarget sittingsTarget = sittingsService.getAnnualSittingsTarget(courtCenter, year);
        SittingTargetWeb sittingTargetWeb;
        if (sittingsTarget == null) {
            sittingTargetWeb = new SittingTargetWeb(courtCenter, year, 0L, new HashMap<String, Long>());
        } else {
            final Map<String, Long> monthlyTargetMap = new HashMap<String, Long>();
            final Set<MonthlyTarget> monthlyTargets = sittingsTarget.getMonthlyTargets();
            for (final MonthlyTarget monthlyTarget : monthlyTargets) {
                monthlyTargetMap.put(monthlyTarget.getMonth(), monthlyTarget.getSitting());
            }
            sittingTargetWeb = new SittingTargetWeb(sittingsTarget.getCourtCenter().getCenterName(), sittingsTarget.getYear(), sittingsTarget.getSittings(), monthlyTargetMap);
        }
        return ComplexResponse.buildOkResponse(sittingTargetWeb);
    }

    /**
     * @param courtCenterName
     *            String name
     * @param financialYear
     *            String as a string to be in form of yyyy-yyyy (e.g. 2015-2016)
     * @return
     */
    @GET
    @Path("/annual-actual-sitting-days/{courtCenterName}/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnualActualSittingDays(@PathParam("courtCenterName") final String courtCenterName, @PathParam("year") final String financialYear) {
        return ComplexResponse.buildOkResponse(sittingService.getAnnualActualSittingDays(courtCenterName, financialYear));
    }

    /**
     * @param courtCenterName
     *            String name
     * @param financialYear
     *            String as a string to be in form of yyyy-yyyy (e.g. 2015-2016)
     * @return
     */
    @GET
    @Path("/annual-actual-sitting-days/{courtCenterName}/date/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnualActualSittingDays(@PathParam("courtCenterName") final String courtCenterName, @PathParam("date") final Date scopedDate) {
        return ComplexResponse.buildOkResponse(sittingService.getAnnualActualSittingDays(courtCenterName, scopedDate));
    }

    /**
     * @param courtCenterName
     *            String name
     * @param financialYear
     *            String as a string to be in form of yyyy-yyyy (e.g. 2015-2016)
     * @return
     */
    @GET
    @Path("/monthly-actual-sitting-days/{courtCenterName}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyActualSittingDays(@PathParam("courtCenterName") final String courtCenterName, @PathParam("year") final String financialYear) {
        Map<String, Long> monthlyActualSittingDays = sittingService.getMonthlyActualSittingDays(courtCenterName, financialYear);
        return ComplexResponse.buildOkResponse(monthlyActualSittingDays);
    }

}