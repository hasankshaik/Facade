package uk.co.listing.domain;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
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
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "SittingTarget", uniqueConstraints = { @UniqueConstraint(columnNames = { "courtcenter", "year" }) })
public class SittingTarget extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SittingTarget_Seq_Gen")
    @SequenceGenerator(name = "SittingTarget_Seq_Gen", sequenceName = "SittingTarget_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courtcenter")
    private CourtCenter courtCenter;

    @Column(name = "year")
    private String year;

    private Long sittings;

    @OneToMany(mappedBy = "sittingTarget", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MonthlyTarget> monthlyTargets = new HashSet<MonthlyTarget>();

    public SittingTarget() {
        super();
    }

    public SittingTarget(final CourtCenter courtCenter, final String year, final Long sittings) {
        super();
        this.courtCenter = courtCenter;
        this.year = year;
        this.sittings = sittings;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CourtCenter getCourtCenter() {
        return courtCenter;
    }

    public void setCourtCenter(final CourtCenter courtCenter) {
        this.courtCenter = courtCenter;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public Long getSittings() {
        return sittings;
    }

    public void setSittings(final Long sittings) {
        this.sittings = sittings;
    }

    public Set<MonthlyTarget> getMonthlyTargets() {
        return monthlyTargets;
    }

    public void setMonthlyTargets(final Set<MonthlyTarget> monthlyTargets) {
        this.monthlyTargets = monthlyTargets;
    }

    @Override
    @Generated(value = { "eclipse.generated" })
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (courtCenter == null ? 0 : courtCenter.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (sittings == null ? 0 : sittings.hashCode());
        result = prime * result + (year == null ? 0 : year.hashCode());
        return result;
    }

    @Override
    @Generated(value = { "eclipse.generated" })
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return validateEquals(obj);
    }

    private boolean validateEquals(final Object obj) {
        final SittingTarget other = (SittingTarget) obj;
        if (courtCenter == null) {
            if (other.courtCenter != null) {
                return false;
            }
        } else if (!courtCenter.equals(other.courtCenter)) {
            return false;
        }
        if (!validateMonthlyTargets(other))
            return false;
        else if (!validateSittingTargets(other))
            return false;
        return true;
    }

    private boolean validateSittingTargets(final SittingTarget other) {
        if (sittings == null) {
            if (other.sittings != null) {
                return false;
            }
        } else if (!sittings.equals(other.sittings)) {
            return false;
        }
        if (year == null) {
            if (other.year != null) {
                return false;
            }
        } else if (!year.equals(other.year)) {
            return false;
        }
        return true;
    }

    private boolean validateMonthlyTargets(final SittingTarget other) {
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (monthlyTargets == null) {
            if (other.monthlyTargets != null) {
                return false;
            }
        } else if (!monthlyTargets.equals(other.monthlyTargets)) {
            return false;
        }
        return true;
    }

}
