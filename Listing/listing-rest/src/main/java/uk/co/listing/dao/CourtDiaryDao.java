package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CourtDiary;

@Repository("CourtDiaryDao")
public class CourtDiaryDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CourtDiaryDao.class);
    @SuppressWarnings("unchecked")
    public CourtDiary findCourtDiary(final String courtCenterName) {
        LOGGER.debug("findCourtDiary"+courtCenterName);
        return getSingleResult((List<CourtDiary>) getHibernateTemplate().findByNamedQueryAndNamedParam("findCourtDiaryByCenterName", "centerName", courtCenterName));
    }
}