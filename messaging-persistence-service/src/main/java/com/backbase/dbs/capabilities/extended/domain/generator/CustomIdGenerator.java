package com.backbase.dbs.capabilities.extended.domain.generator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public class CustomIdGenerator extends UUIDHexGenerator {
    public CustomIdGenerator() {
    }

    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        Persistable entity = (Persistable)object;
        String customId = (String) entity.getId();
        return (Serializable)(StringUtils.isNotBlank(customId) ? customId : super.generate(session, object));
    }
}
