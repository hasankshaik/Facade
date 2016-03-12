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
@Table(name = "CourtRoomInDiary")
public class CourtRoomInDiary extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CourtRoomInDiary_Seq_Gen")
    @SequenceGenerator(name = "CourtRoomInDiary_Seq_Gen", sequenceName = "CourtRoomInDiary_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    @ForeignKey(name = "fk_roomInDiary_room")
    private CourtRoom courtRoom;

    @ManyToOne
    @JoinColumn(name = "courtDiaryId", nullable = false)
    @ForeignKey(name = "fk_roomInDiary_courtDiary")
    private CourtDiary courtDiary;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startdate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enddate")
    private Date endDate;

    public CourtRoomInDiary() {
        super();
    }

    public CourtRoomInDiary(final CourtRoom courtRoom, final CourtDiary courtDiary) {
        super();
        this.courtRoom = courtRoom;
        this.courtDiary = courtDiary;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CourtRoom getCourtRoom() {
        return courtRoom;
    }

    public void setCourtRoom(final CourtRoom courtRoom) {
        this.courtRoom = courtRoom;
    }

    public CourtDiary getCourtDiary() {
        return courtDiary;
    }

    public void setCourtDiary(final CourtDiary courtDiary) {
        this.courtDiary = courtDiary;
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

}
