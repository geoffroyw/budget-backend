package io.yac.core.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by geoffroy on 07/02/2016.
 */

@MappedSuperclass
public class TimestampableEntity {
    private Date createdAt;
    private Date updatedAt;

    @Column(name = "created_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updated_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    @PrePersist
    public void setInsertionAudit() {
        final Date now = new Date();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void setUpdateAudit() {
        final Date now = new Date();
        this.setUpdatedAt(now);
    }
}
