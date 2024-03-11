package ru.shadi777.proxyapplication.services.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shadi777.proxyapplication.repositories.AuditRepository;
import ru.shadi777.proxyapplication.models.AuditInfo;

@Service
@RequiredArgsConstructor
public class DatabaseAuditService implements AuditService {
    private final AuditRepository auditRepository;

    @Override
    public void writeAudit(AuditInfo auditInfo) {
        auditRepository.save(auditInfo);
    }
}
