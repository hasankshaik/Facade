package uk.co.listing.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import uk.co.listing.domain.constant.ProcessingStatus;

@Entity
@Table(name = "CrestDataBatchJob")
public class CrestDataBatchJob extends AbstractDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CrestDataBatchJob_Seq_Gen")
    @SequenceGenerator(name = "CrestDataBatchJob_Seq_Gen", sequenceName = "CrestDataBatchJob_Seq")
    @Column(name = "batchId", unique = true, nullable = false)
    private Long batchId;

    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessingStatus processingState;

    private String comments;

    @OneToMany(mappedBy = "crestDataBatchJob", fetch = FetchType.LAZY)
    private final Set<CrestData> listCrestData = new HashSet<CrestData>();

    public Set<CrestData> getListCrestData() {
        return listCrestData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(final Long batchId) {
        this.batchId = batchId;
    }

    public ProcessingStatus getProcessingState() {
        return processingState;
    }

    public void setProcessingState(final ProcessingStatus processingState) {
        this.processingState = processingState;
    }

}
