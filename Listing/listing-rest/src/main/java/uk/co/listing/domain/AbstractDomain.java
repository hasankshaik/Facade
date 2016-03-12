package uk.co.listing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractDomain {

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date lastModifiedOn;

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

}
