package uk.co.listing.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CourtCenter")
public class CourtCenter extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CourtCenter_Seq_Gen")
    @SequenceGenerator(name = "CourtCenter_Seq_Gen", sequenceName = "CourtCenter_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String centerName;

    @Column(unique = true, nullable = false)
    private Long code;

    @OneToMany(mappedBy = "courtCenter", fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<SittingTarget> sittingTargets = new HashSet<SittingTarget>();

    public CourtCenter(final Long code, final String centerName) {
        super();
        this.centerName = centerName;
        this.code = code;
    }

    public CourtCenter() {
        super();
    }

    public Long getId() {
        return id;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(final String centerName) {
        this.centerName = centerName;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(final Long code) {
        this.code = code;
    }

    public Set<SittingTarget> getSittingTargets() {
        return sittingTargets;
    }

    public void setSittingTargets(final Set<SittingTarget> sittingTargets) {
        this.sittingTargets = sittingTargets;
    }
}
