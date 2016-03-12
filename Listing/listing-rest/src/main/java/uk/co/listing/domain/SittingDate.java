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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "SittingDate")
public class SittingDate extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SittingDate_Seq_Gen")
    @SequenceGenerator(name = "SittingDate_Seq_Gen", sequenceName = "SittingDate_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courtRoomId", nullable = false)
    @ForeignKey(name = "fk_sitting_courtRoom")
    private CourtRoom courtRoom;

    private Date day;

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

    public Date getDay() {
        return day;
    }

    public void setDay(final Date day) {
        this.day = day;
    }

}
