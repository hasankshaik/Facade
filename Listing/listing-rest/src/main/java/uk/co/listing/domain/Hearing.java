package uk.co.listing.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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

import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.HearingStatusEnum;
import uk.co.listing.domain.constant.HearingType;

@Entity
@Audited
@Table(name = "Hearing")
@EntityListeners(value = ApplicationEntityInterceptor.class)
public class Hearing extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Hearing_Seq_Gen")
    @SequenceGenerator(name = "Hearing_Seq_Gen", sequenceName = "Hearing_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caseid", nullable = false)
    @ForeignKey(name = "fk_hearing_caserelated")
    private CaseRelated caseRelated;

    @Enumerated(EnumType.STRING)
    private HearingType hearingType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enddate")
    private Date endDate;

    @Column(nullable = false)
    private Double daysEstimated;

    @Column
    private String name;

    @Column
    private String hearingKey;

    @OneToMany(mappedBy = "hearing", fetch = FetchType.LAZY)
    private Set<HearingInstance> hearingInstance = new HashSet<HearingInstance>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatusEnum bookingStatus;

    @Enumerated(EnumType.STRING)
    private HearingStatusEnum hearingStatus;

    @Enumerated(EnumType.STRING)
    private BookingTypeEnum bookingType;

    @Type(type = "yes_no")
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column
    private String bookedCourtRoomName;

    @Column
    private String hearingNote;

    public String getBookedCourtRoomName() {
        return bookedCourtRoomName;
    }

    public void setBookedCourtRoomName(final String bookedCourtRoomName) {
        this.bookedCourtRoomName = bookedCourtRoomName;
    }

    public String getHearingKey() {
        return hearingKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CaseRelated getCaseRelated() {
        return caseRelated;
    }

    public void setCaseRelated(final CaseRelated caseRelated) {
        this.caseRelated = caseRelated;
    }

    public HearingType getHearingType() {
        return hearingType;
    }

    public void setHearingType(final HearingType hearingType) {
        this.hearingType = hearingType;
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

    public Double getDaysEstimated() {
        return daysEstimated;
    }

    public void setDaysEstimated(final Double daysEstimated) {
        this.daysEstimated = daysEstimated;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<HearingInstance> getHearingInstance() {
        return hearingInstance;
    }

    public void setHearingInstance(final Set<HearingInstance> hearingInstance) {
        this.hearingInstance = hearingInstance;
    }

    public BookingStatusEnum getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(final BookingStatusEnum bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public BookingTypeEnum getBookingType() {
        return bookingType;
    }

    public void setBookingType(final BookingTypeEnum bookingType) {
        this.bookingType = bookingType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public String getHearingNote() {
        return hearingNote;
    }

    public void setHearingNote(final String hearingNote) {
        this.hearingNote = hearingNote;
    }

    public HearingStatusEnum getHearingStatus() {
        return hearingStatus;
    }

    public void setHearingStatus(final HearingStatusEnum hearingStatus) {
        this.hearingStatus = hearingStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Hearing [id=");
        sb.append(id);
        sb.append(", caseRelated=");
        sb.append(caseRelated);
        sb.append(", hearingType=");
        sb.append(hearingType);
        sb.append(", startDate=");
        sb.append(startDate);
        sb.append(", endDate=");
        sb.append(endDate);
        sb.append(", daysEstimated=");
        sb.append(daysEstimated);
        sb.append(", name=");
        sb.append(name);
        sb.append(", hearingInstance=");
        sb.append(hearingInstance);
        sb.append(", note=");
        sb.append(hearingNote);
        sb.append("]");
        return sb.toString();
    }

}
