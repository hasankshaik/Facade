package uk.co.listing.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CourtRoom")
public class CourtRoom extends AbstractDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CourtRoom_Seq_Gen")
    @SequenceGenerator(name = "CourtRoom_Seq_Gen", sequenceName = "CourtRoom_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomName;

    private static final long serialVersionUID = 877943693031620350L;

    public CourtRoom() {
        super();
    }

    public CourtRoom(final String roomName) {
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "CourtRoom [id=" + id + ", roomName=" + roomName + "]";
    }

}
