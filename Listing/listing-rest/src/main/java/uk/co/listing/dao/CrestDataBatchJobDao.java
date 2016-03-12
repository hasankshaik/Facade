package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CrestData;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.CrestDefendant;
import uk.co.listing.domain.CrestLinkedCase;
import uk.co.listing.domain.CrestNonAvailable;
import uk.co.listing.domain.CrestNote;
import uk.co.listing.domain.CrestPcmh;
import uk.co.listing.domain.constant.ProcessingStatus;

@Repository("CrestDataBatchJobDao")
public class CrestDataBatchJobDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CrestDataBatchJobDao.class);
    @SuppressWarnings("unchecked")
    public CrestDataBatchJob findTaskByProcessingStatus(final ProcessingStatus processingStatus) {
        LOGGER.debug("findTaskByProcessingStatus"+ processingStatus.getDescription());
        return getSingleResult((List<CrestDataBatchJob>) getHibernateTemplate().findByNamedQuery("findTaskByProcessingStatus", processingStatus));
    }

    @SuppressWarnings("unchecked")
    public List<CrestDataBatchJob> findAllTaskByProcessingStatus(final ProcessingStatus processingStatus) {
        LOGGER.debug("findAllTaskByProcessingStatus"+ processingStatus.getDescription());
        return (List<CrestDataBatchJob>) getHibernateTemplate().findByNamedQuery("findTaskByProcessingStatus", processingStatus);
    }

    @SuppressWarnings("unchecked")
    public CrestDataBatchJob getCrestDataBatchJobById(final Long batchId) {
        LOGGER.debug("getCrestDataBatchJobById"+ batchId);
        return getSingleResult((List<CrestDataBatchJob>) getHibernateTemplate().findByNamedQuery("getCrestDataBatchJobById", batchId));
    }

    @SuppressWarnings("unchecked")
    public List<CrestData> getCreateDataByBatchId(final Long batchId) {
        return (List<CrestData>) getHibernateTemplate().findByNamedQuery("getCreateDataByBatchId", batchId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestData> getCreateDataForInsert(final Long batchId) {
        return (List<CrestData>) getHibernateTemplate().findByNamedQuery("getCreateDataForInsert", batchId);
    }

    @SuppressWarnings("unchecked")
    public List<CaseRelated> getCreateDataForCompletion(final Long batchId) {
        return (List<CaseRelated>) getHibernateTemplate().findByNamedQuery("getCreateDataForCompletion", batchId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestData> getCreateDataForUpdate(final Long batchId) {
        return (List<CrestData>) getHibernateTemplate().findByNamedQuery("getCreateDataForUpdate", batchId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestDataBatchJob> getProcessingStatus() {
        return (List<CrestDataBatchJob>) getHibernateTemplate().findByNamedQuery("getProcessingStatus");
    }

    @SuppressWarnings("unchecked")
    public List<CrestDefendant> getInvalidCrestdefendant(final Long batchJobId) {
        return (List<CrestDefendant>) getHibernateTemplate().findByNamedQuery("getInvalidCrestdefendant", batchJobId);
    }

    public void deleteCrestdefendant() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("deleteCrestdefendant").executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<CrestDefendant> getCrestdefendant(final Long batchJobId) {
        return (List<CrestDefendant>) getHibernateTemplate().findByNamedQuery("getCrestdefendant", batchJobId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestNote> getInvalidCrestNotes(final Long batchJobId) {
        return (List<CrestNote>) getHibernateTemplate().findByNamedQuery("getInvalidCrestNotes", batchJobId);
    }

    public void deleteCrestNotes() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("deleteCrestNotes").executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<CrestNote> getCrestNotes(final Long batchJobId) {
        return (List<CrestNote>) getHibernateTemplate().findByNamedQuery("getCrestNotes", batchJobId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestNonAvailable> getInvalidCrestNonAvail(final Long batchJobId) {
        return (List<CrestNonAvailable>) getHibernateTemplate().findByNamedQuery("getInvalidCrestNonAvail", batchJobId);
    }

    public void deleteCrestNonAvail() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("deleteCrestNonAvail").executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<CrestNonAvailable> getCrestNonAvail(final Long batchJobId) {
        return (List<CrestNonAvailable>) getHibernateTemplate().findByNamedQuery("getCrestNonAvail", batchJobId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestPcmh> getInvalidCrestHearing(final Long batchJobId) {
        return (List<CrestPcmh>) getHibernateTemplate().findByNamedQuery("getInvalidCrestHearing", batchJobId);
    }

    public void deleteCrestHearing() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("deleteCrestHeraingInstance").executeUpdate();
        getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("deleteCrestHearing").executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<CrestPcmh> getCrestHearing(final Long batchJobId) {
        return (List<CrestPcmh>) getHibernateTemplate().findByNamedQuery("getCrestHearing", batchJobId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestLinkedCase> getInvalidCrestLinkedcases(final Long batchJobId) {
        return (List<CrestLinkedCase>) getHibernateTemplate().findByNamedQuery("getInvalidCrestLinkedcases", batchJobId);
    }

    @SuppressWarnings("unchecked")
    public List<CrestLinkedCase> getCrestLinkedCases(final Long batchJobId) {
        return (List<CrestLinkedCase>) getHibernateTemplate().findByNamedQuery("getCrestLinkedCases", batchJobId);
    }

    public void deleteCrestLinkedCase() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("deleteCrestLinkedCase").executeUpdate();
    }

}