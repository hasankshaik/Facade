package uk.co.listing.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public abstract class GenericDao extends HibernateDaoSupport {

    public GenericDao() {
        super();
    }

    @Autowired
    public void init(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

    public <T> T getSingleResult(List<T> listOfValue) {
        if (!listOfValue.isEmpty()) {
            for (T value : listOfValue) {
                return value;
            }
        }
        return null;
    }

    public <T> void save(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public <T> void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    public <T> void merge(T entity) {
        getHibernateTemplate().merge(entity);
    }

}