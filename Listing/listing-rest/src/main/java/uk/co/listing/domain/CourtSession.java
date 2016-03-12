package uk.co.listing.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CourtSession")
public class CourtSession extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CourtSession_Seq_Gen")
    @SequenceGenerator(name = "CourtSession_Seq_Gen", sequenceName = "CourtSession_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sittingId", nullable = false)
    @ForeignKey(name = "fk_session_sitting")
    private SittingDate sitting;

    @ManyToOne
    @JoinColumn(name = "panelid", nullable = false)
    @ForeignKey(name = "fk_session_panel")
    private Panel panel;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    private Set<SessionBlock> sessionBlock = new HashSet<SessionBlock>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enddate")
    private Date endDate;

    @Type(type = "yes_no")
    private Boolean isClosed;

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(final Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public SittingDate getSitting() {
        return sitting;
    }

    public void setSitting(final SittingDate sitting) {
        this.sitting = sitting;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(final Panel panel) {
        this.panel = panel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Set<SessionBlock> getSessionBlock() {
        return sessionBlock;
    }

    public void setSessionBlock(final Set<SessionBlock> sessionBlock) {
        this.sessionBlock = sessionBlock;
    }

    @Override
    public String toString() {
        return "CourtSession [id=" + id + ", sitting=" + sitting + ", panel=" + panel + ", sessionBlock=" + sessionBlock + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }

}
