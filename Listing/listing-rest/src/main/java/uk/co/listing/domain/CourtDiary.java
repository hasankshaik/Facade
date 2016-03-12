package uk.co.listing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CourtDiary")
public class CourtDiary extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CourtDiary_Seq_Gen")
    @SequenceGenerator(name = "CourtDiary_Seq_Gen", sequenceName = "CourtDiary_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CourtCenterId", nullable = false)
    @ForeignKey(name = "fk_courtDiary_courtCenter")
    private CourtCenter center;

    public CourtDiary(final CourtCenter center) {
        super();
        this.center = center;
    }

    public CourtDiary() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CourtCenter getCenter() {
        return center;
    }

    public void setCenter(final CourtCenter center) {
        this.center = center;
    }

}
