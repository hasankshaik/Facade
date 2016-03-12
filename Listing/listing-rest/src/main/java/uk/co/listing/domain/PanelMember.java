package uk.co.listing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

import uk.co.listing.domain.constant.PanelMemberType;

@Entity
@Audited
@Table(name = "PanelMember")
public class PanelMember extends AbstractDomain {

    @ManyToOne
    @JoinColumn(name = "panelId", nullable = false)
    @ForeignKey(name = "fk_panelMember_panel")
    private Panel panel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "judicialOfficerId", nullable = false)
    @ForeignKey(name = "fk_panelMember_judicialOfficer")
    private JudicialOfficer judicialOfficer;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PanelMember_Seq_Gen")
    @SequenceGenerator(name = "PanelMember_Seq_Gen", sequenceName = "PanelMember_Seq", allocationSize = 10000)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PanelMemberType panelMemberType;

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(final Panel panel) {
        this.panel = panel;
    }

    public JudicialOfficer getJudicialOfficer() {
        return judicialOfficer;
    }

    public void setJudicialOfficer(final JudicialOfficer judicialOfficer) {
        this.judicialOfficer = judicialOfficer;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public PanelMemberType getPanelMemberType() {
        return panelMemberType;
    }

    public void setPanelMemberType(PanelMemberType panelMemberType) {
        this.panelMemberType = panelMemberType;
    }
    
}
