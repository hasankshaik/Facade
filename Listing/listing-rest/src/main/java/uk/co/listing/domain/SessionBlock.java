package uk.co.listing.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import uk.co.listing.domain.constant.SessionBlockType;

@Entity
@Audited
@Table(name = "sessionblock")
public class SessionBlock extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SessionBlock_Seq_Gen")
    @SequenceGenerator(name = "SessionBlock_Seq_Gen", sequenceName = "SessionBlock_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enddate")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "blockType")
    private SessionBlockType blockType;

    @ManyToOne
    @JoinColumn(name = "sessionid", nullable = false)
    @ForeignKey(name = "fk_block_session")
    private CourtSession session;

    @OneToMany(mappedBy = "block", fetch = FetchType.LAZY)
    private Set<HearingInstance> hearings = new HashSet<HearingInstance>();

    private Integer casesPerHour;

    @Type(type = "yes_no")
    private Boolean isBackUp;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public SessionBlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(final SessionBlockType blockType) {
        this.blockType = blockType;
    }

    public Integer getCasesPerHour() {
        return casesPerHour;
    }

    public void setCasesPerHour(final Integer casesPerHour) {
        this.casesPerHour = casesPerHour;
    }

    public CourtSession getSession() {
        return session;
    }

    public void setSession(final CourtSession session) {
        this.session = session;
    }

    public Set<HearingInstance> getHearings() {
        return hearings;
    }

    public void setHearings(final Set<HearingInstance> hearings) {
        this.hearings = hearings;
    }

    public Boolean getIsBackUp() {
        return isBackUp;
    }

    public void setIsBackUp(final Boolean isBackUp) {
        this.isBackUp = isBackUp;
    }

    @Override
    public String toString() {
        return "SessionBlock [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", blockType=" + blockType + ", session=" + session + ", hearings=" + hearings + ", casesPerHour="
                + casesPerHour + ", isBackUp=" + isBackUp + "]";
    }
}
