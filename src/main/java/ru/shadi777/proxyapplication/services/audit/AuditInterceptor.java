package ru.shadi777.proxyapplication.services.audit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.shadi777.proxyapplication.enums.AuditType;
import ru.shadi777.proxyapplication.models.AuditInfo;
import ru.shadi777.proxyapplication.services.UserService;
import ru.shadi777.proxyapplication.utils.Audit;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuditInterceptor implements HandlerInterceptor {
    private final AuditService auditService;
    private final UserService userService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Request caught!");
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            Audit annotation = method.getAnnotation(Audit.class);
            if (annotation == null) {
                return;
            }

            AuditType auditType = annotation.type();
            if (doAudit(auditType, response.getStatus())) {
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setEndpoint(request.getRequestURI());
                auditInfo.setOffsetDateTime(OffsetDateTime.now());
                auditInfo.setIpAddress(request.getRemoteAddr());
                auditInfo.setResponseStatus(response.getStatus());
                auditInfo.setUser(userService.findByUsername(getUsername(getBasicToken(request))));

                auditService.writeAudit(auditInfo);
                log.info("Request saved!");
            }
        }
    }

    private boolean doAudit(AuditType auditType, int status) {
        return switch (auditType) {
            case ALL -> true;
            case SUCCESS -> status % 100 == 2;
            case FAIL -> status % 100 != 2;
        };
    }

    private String getBasicToken(HttpServletRequest request) {
        String token = "";
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Basic")) {
            token = authorizationHeaderValue.substring(6);
        }

        return token;
    }

    private String getUsername(String token) {
        String credentials = new String(Base64.decodeBase64(token));
        return credentials.substring(0, credentials.indexOf(':'));
    }
}
