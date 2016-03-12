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
@Table(name = "PersonAvailability")
public class PersonAvailability extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PersonAvailability_Seq_Gen")
    @SequenceGenerator(name = "PersonAvailability_Seq_Gen", sequenceName = "PersonAvailability_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "day")
    private Date day;

    @ManyToOne
    @JoinColumn(name = "PersonInCaseId", nullable = false)
    @ForeignKey(name = "fk_personAvailability_personInCase")
    private PersonInCase personInCase;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(final Date day) {
        this.day = day;
    }

    public PersonInCase getPersonInCase() {
        return personInCase;
    }

    public void setPersonInCase(final PersonInCase personInCase) {
        this.personInCase = personInCase;
    }

}
