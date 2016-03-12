package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CaseNote;

@Repository("CaseNoteDao")
public class CaseNoteDao extends GenericDao {

    private static final Logger LOGGER = Logger.getLogger(CaseNoteDao.class);

    @SuppressWarnings("unchecked")
    public CaseNote findNoteByCrestCaseNumberAndDescription(final String crestCaseNumber, final Long description) {
        LOGGER.debug("find Note by crest case number = " + crestCaseNumber + " ,description = " + description);
        return getSingleResult((List<CaseNote>) getHibernateTemplate().findByNamedQuery("findNoteByCrestCaseNumberAndDescription", crestCaseNumber, description));
    }

}
