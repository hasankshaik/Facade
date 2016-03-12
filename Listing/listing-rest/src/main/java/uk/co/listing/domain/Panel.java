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

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "Panel")
public class Panel extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Panel_Seq_Gen")
    @SequenceGenerator(name = "Panel_Seq_Gen", sequenceName = "Panel_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private String description;

    @OneToMany(mappedBy = "panel", fetch = FetchType.EAGER)
    private Set<PanelMember> panelMember = new HashSet<PanelMember>();

    @OneToMany(mappedBy = "panel", fetch = FetchType.LAZY)
    private Set<CourtSession> sessions = new HashSet<CourtSession>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<PanelMember> getPanelMember() {
        return panelMember;
    }

    public void setPanelMember(final Set<PanelMember> panelMember) {
        this.panelMember = panelMember;
    }

    public Set<CourtSession> getSessions() {
        return sessions;
    }

    public void setSessions(final Set<CourtSession> sessions) {
        this.sessions = sessions;
    }

}
