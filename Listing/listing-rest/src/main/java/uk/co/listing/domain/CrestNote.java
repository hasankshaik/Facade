package uk.co.listing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

import uk.co.listing.domain.constant.CaseNotePrintType;
import uk.co.listing.domain.constant.CaseNoteType;

@Entity
@Table(name = "CrestNote")
public class CrestNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "batchId", nullable = false)
    @ForeignKey(name = "fk_CrestDataBatchJob_Id")
    private CrestDataBatchJob crestDataBatchJob;

    @Column(name = "PARENT_CASE", nullable = false)
    private String caseNumber;

    @Column(name = "NOTE_TYPE_12")
    private CaseNoteType caseNoteType;

    @Column(name = "NOTE_PRINT_15A")
    private CaseNotePrintType caseNotePrintIndicator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NOTE_DATE_15B")
    private Date caseNoteDate;

    @Column(name = "NOTE_15C")
    private String caseNoteText;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NOTE_DIARY_DATE_15D")
    private Date caseNoteDiaryDate;

    public Long getId() {
        return Id;
    }

    public void setId(final Long id) {
        Id = id;
    }

    public CrestDataBatchJob getCrestDataBatchJob() {
        return crestDataBatchJob;
    }

    public void setCrestDataBatchJob(final CrestDataBatchJob crestDataBatchJob) {
        this.crestDataBatchJob = crestDataBatchJob;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public CaseNoteType getCaseNoteType() {
        return caseNoteType;
    }

    public void setCaseNoteType(CaseNoteType caseNoteType) {
        this.caseNoteType = caseNoteType;
    }

    public CaseNotePrintType getCaseNotePrintIndicator() {
        return caseNotePrintIndicator;
    }

    public void setCaseNotePrintIndicator(CaseNotePrintType caseNotePrintIndicator) {
        this.caseNotePrintIndicator = caseNotePrintIndicator;
    }

    public Date getCaseNoteDate() {
        return caseNoteDate;
    }

    public void setCaseNoteDate(Date caseNoteDate) {
        this.caseNoteDate = caseNoteDate;
    }

    public String getCaseNoteText() {
        return caseNoteText;
    }

    public void setCaseNoteText(String caseNoteText) {
        this.caseNoteText = caseNoteText;
    }

    public Date getCaseNoteDiaryDate() {
        return caseNoteDiaryDate;
    }

    public void setCaseNoteDiaryDate(Date caseNoteDiaryDate) {
        this.caseNoteDiaryDate = caseNoteDiaryDate;
    }


}
