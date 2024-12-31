package org.example.restaurantvoting.restaurant.service;

import jakarta.persistence.EntityManagerFactory;
import org.example.restaurantvoting.common.exception.AppException;
import org.example.restaurantvoting.common.exception.ErrorType;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Transactional
    public <T> T getPreviousEntityVersion(int entityId, Class<T> aClass) {
        final AuditReader auditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());

        List<T> entities = auditReader.createQuery()
                .forRevisionsOfEntity(aClass, true, true)
                .add(AuditEntity.id().eq(entityId))
                .getResultList();

        if (entities.size() > 1) {
            return entities.get(entities.size() - 2);
        } else {
            throw new AppException("No previous versions were found!", ErrorType.NOT_FOUND);
        }
    }
}
