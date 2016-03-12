package uk.co.listing.domain;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class ApplicationEntityInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 4056978784890567239L;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {

        boolean stateModified = false;
        if (entity instanceof Hearing) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("hearingKey".equals(propertyNames[i])) {
                    state[i] = "Hearing" + id;
                    stateModified = true;
                    break;
                }
            }
        }

        if (entity instanceof AbstractDomain) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("lastModifiedOn".equals(propertyNames[i])) {
                    state[i] = new Date();
                    stateModified = true;
                    break;
                }
            }
        }
        return stateModified;

    }
}
