package uk.co.listing.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.JudicialTicketsEnum;

@Entity
@Audited
@Table(name = "JudicialOfficer")
public class JudicialOfficer extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JudicialOfficer_Seq_Gen")
    @SequenceGenerator(name = "JudicialOfficer_Seq_Gen", sequenceName = "JudicialOfficer_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JudicialOfficerType judicialOfficerType;

    @Type(type = "yes_no")
    @Column(nullable = false)
    private Boolean isQC;

    @Type(type = "yes_no")
    private Boolean isResident;

    @OneToMany(mappedBy = "judicialOfficer", fetch = FetchType.LAZY)
    private Set<PanelMember> panelMember = new HashSet<PanelMember>();

    @ElementCollection(targetClass = JudicialTicketsEnum.class)
    @CollectionTable(name = "judicialofficerstickets", joinColumns = @JoinColumn(name = "judicialOfficerId"))
    @Column(name = "judicialTicketId")
    private final Set<JudicialTicketsEnum> judicialTickets = new HashSet<JudicialTicketsEnum>();

    public JudicialOfficer(final String fullName, final JudicialOfficerType judicialOfficerType, final Boolean isQC) {
        this.fullName = fullName;
        this.judicialOfficerType = judicialOfficerType;
        this.isQC = isQC;
    }

    public JudicialOfficer(final String fullName, final JudicialOfficerType judicialOfficerType, final Boolean isQC, final Boolean isResident) {
        this.fullName = fullName;
        this.judicialOfficerType = judicialOfficerType;
        this.isQC = isQC;
        this.isResident = isResident;
    }

    public JudicialOfficer() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boolean getIsQC() {
        return isQC;
    }

    public void setIsQC(final Boolean isQC) {
        this.isQC = isQC;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public JudicialOfficerType getJudicialOfficerType() {
        return judicialOfficerType;
    }

    public void setJudicialOfficerType(final JudicialOfficerType judicialOfficerType) {
        this.judicialOfficerType = judicialOfficerType;
    }

    public Set<PanelMember> getPanelMember() {
        return panelMember;
    }

    public void setPanelMember(final Set<PanelMember> panelMember) {
        this.panelMember = panelMember;
    }

    public Boolean getIsResident() {
        return isResident;
    }

    public void setIsResident(final Boolean isResident) {
        this.isResident = isResident;
    }

    public Set<JudicialTicketsEnum> getJudicialTickets() {
        return judicialTickets;
    }

}
