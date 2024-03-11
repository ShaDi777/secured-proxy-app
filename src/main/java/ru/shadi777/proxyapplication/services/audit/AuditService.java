package ru.shadi777.proxyapplication.services.audit;

import ru.shadi777.proxyapplication.models.AuditInfo;

public interface AuditService {
    void writeAudit(AuditInfo auditInfo);
}
