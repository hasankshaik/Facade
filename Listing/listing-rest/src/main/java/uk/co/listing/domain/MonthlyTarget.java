package uk.co.listing.domain;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "MonthlyTarget", uniqueConstraints = { @UniqueConstraint(columnNames = { "sittingtarget", "month" }) })
public class MonthlyTarget extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MonthlyTarget_Seq_Gen")
    @SequenceGenerator(name = "MonthlyTarget_Seq_Gen", sequenceName = "MonthlyTarget_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sittingtarget")
    private SittingTarget sittingTarget;

    private String month;

    private Long sitting = 0L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public SittingTarget getSittingTarget() {
        return sittingTarget;
    }

    public void setSittingTarget(final SittingTarget sittingTarget) {
        this.sittingTarget = sittingTarget;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(final String month) {
        this.month = month;
    }

    public Long getSitting() {
        return sitting;
    }

    public void setSitting(final Long sitting) {
        this.sitting = sitting;
    }

    @Override
    public String toString() {
        return "MonthlyTarget [sittingTarget=" + sittingTarget + ", month=" + month + ", sitting=" + sitting + "]";
    }

    @Override
    @Generated(value = { "eclipse.generated" })
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((month == null) ? 0 : month.hashCode());
        return result;
    }

    @Override
    @Generated(value = { "eclipse.generated" })
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return validateEquals(obj);
    }

    private boolean validateEquals(final Object obj) {
        final MonthlyTarget other = (MonthlyTarget) obj;
        if (month == null) {
            if (other.month != null)
                return false;
        } else if (!month.equals(other.month))
            return false;
        if (sittingTarget == null) {
            if (other.sittingTarget != null)
                return false;
        } else if (!sittingTarget.equals(other.sittingTarget))
            return false;
        return true;
    }

}
