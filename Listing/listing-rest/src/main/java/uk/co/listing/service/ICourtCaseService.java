package uk.co.listing.service;

import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;

public interface ICourtCaseService {
    public CourtCaseWeb getCaseDetailsByCrestNumber(String crestCaseNumber);

    public void saveUpdateCaseRelated(CourtCaseWeb courtCaseWeb);

    public void saveUpdateDefendantForCase(CourtCaseWeb courtCaseWeb);

    public void saveUpdateCaseNoteForCase(CourtCaseWeb courtCaseWeb);

    public void saveUpdateLinkedCaseForCase(CourtCaseWeb courtCaseWeb);

    public void saveUpdateNotAvailableDateForCase(NotAvailableDatesWeb notAvailableDatesWeb);
    
    public void allocateJudgeForCase(CourtCaseWeb courtCaseWeb);

    public void deallocateJudgeForCase(CourtCaseWeb courtCaseWeb);

    public void removeDefendantForCase(String crestCaseNumber, String personFullName);

    public void removeCaseNoteForCase(String crestCaseNumber, String description);

    public void removeLinkedCaseForCase(String centerName, String crestCaseNumber, String crestLinkedNumber);

    public void deleteNotAvailableDateForCase(Long notAvailableDate);

}
