package uk.co.listing.rest.response;

import java.util.Date;

public class CaseNoteWeb extends RestResponse {

    private String note;
    private Date diaryDate;
    private Date creationDate;
    private String description;

    public CaseNoteWeb() {
        super();
    }

    public CaseNoteWeb(final String note, final Date diaryDate, final Date creationDate, final String description) {
        super();
        this.note = note;
        this.diaryDate = diaryDate;
        this.creationDate = creationDate;
        this.description = description;
    }

    public CaseNoteWeb(final String errorMessage) {
        super(errorMessage);
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(final Date diaryDate) {
        this.diaryDate = diaryDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
