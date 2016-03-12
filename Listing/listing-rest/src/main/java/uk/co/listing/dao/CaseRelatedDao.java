package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CaseRelated;

@Repository("CaseRelatedDao")
public class CaseRelatedDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CaseRelatedDao.class);

    @SuppressWarnings("unchecked")
    public CaseRelated findCaseByCrestCaseNumber(final String crestCaseNumber) {
        LOGGER.debug("find case by crrest case number = " + crestCaseNumber);
        return getSingleResult((List<CaseRelated>) getHibernateTemplate().findByNamedQuery("findCaseByCrestCaseNumber", crestCaseNumber));
    }

    @SuppressWarnings("unchecked")
    public CaseRelated findCaseByCrestCaseNumberAndCenter(final String crestCaseNumber, final String center) {
        LOGGER.debug("find case by crrest case number = " + crestCaseNumber + " and center = " + center);
        return getSingleResult((List<CaseRelated>) getHibernateTemplate().findByNamedQuery("findCaseByCrestCaseNumberAndCenter", crestCaseNumber, center));
    }

}
