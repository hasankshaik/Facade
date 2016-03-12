package uk.co.listing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CaseNote")
public class CaseNote extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CaseNote_Seq_Gen")
    @SequenceGenerator(name = "CaseNote_Seq_Gen", sequenceName = "CaseNote_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caseid", nullable = false)
    @ForeignKey(name = "fk_note_caserelated")
    private CaseRelated caseRelated;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date diaryDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public CaseRelated getCaseRelated() {
        return caseRelated;
    }

    public void setCaseRelated(final CaseRelated caseRelated) {
        this.caseRelated = caseRelated;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(final Date diaryDate) {
        this.diaryDate = diaryDate;
    }

}
