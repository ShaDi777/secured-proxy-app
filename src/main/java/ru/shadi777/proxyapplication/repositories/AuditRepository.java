package ru.shadi777.proxyapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shadi777.proxyapplication.models.AuditInfo;

@Repository
public interface AuditRepository  extends JpaRepository<AuditInfo, Long> {
    AuditInfo findByIpAddress(String ipAddress);

    @Override
    void delete(AuditInfo auditInfo);
}
