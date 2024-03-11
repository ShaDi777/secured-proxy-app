package ru.shadi777.proxyapplication.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.data.util.CastUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shadi777.proxyapplication.utils.Audit;
import ru.shadi777.proxyapplication.utils.CustomSpelFunctions;

import static ru.shadi777.proxyapplication.configuration.CacheConfiguration.PROXY_CACHE;

@Log4j2
@RestController
@RequestMapping(ProxyController.API_URL)
public class ProxyController {
    public static final String API_URL = "/api";

    @Value("${app.proxy.base-url}")
    private String baseUrl;

    @Audit
    @GetMapping("/**")
    @Cacheable(value = PROXY_CACHE, key = "#root.args[0].path()")
    public ResponseEntity<?> proxyGet(ProxyExchange<byte[]> proxy) throws Exception {
        log.info("==============================");
        log.info("SENDING HTTP-GET REQUEST...");
        String path = proxy.path(API_URL);
        return proxy
                .uri(baseUrl + path)
                .header("Accept-Encoding", "identity")
                .get();
    }

    @Audit
    @Caching(
        put = @CachePut(value = PROXY_CACHE, key = "#root.args[0].path()"),
        evict = @CacheEvict(value = PROXY_CACHE, key = "#root.args[0].path().substring(0, #root.args[0].path().lastIndexOf(\"/\"))")
    )
    @PutMapping("/**")
    public ResponseEntity<?> proxyPut(ProxyExchange<byte[]> proxy, @RequestBody String requestBody) throws Exception {
        log.info("==============================");
        log.info("SENDING HTTP-PUT REQUEST...");
        String path = proxy.path(API_URL);
        return proxy
                .uri(baseUrl + path)
                .body(requestBody)
                .header("Accept-Encoding", "identity")
                .put();
    }

    @Audit
    @Caching(
        put = @CachePut(value = PROXY_CACHE, key = "#root.args[0].path() + \"/\" + T(ru.shadi777.proxyapplication.utils.CustomSpelFunctions).jsonParser(new String(#result.getBody()), \"id\")"),
        evict = @CacheEvict(value = PROXY_CACHE, key = "#root.args[0].path()")
    )
    @PostMapping("/**")
    public ResponseEntity<?> proxyPost(ProxyExchange<byte[]> proxy, @RequestBody String requestBody) throws Exception {
        log.info("==============================");
        log.info("SENDING HTTP-POST REQUEST...");
        String path = proxy.path(API_URL);
        return proxy
                .uri(baseUrl + path)
                .body(requestBody.toString())
                .header("Accept-Encoding", "identity")
                .post();
    }

    @Audit
    @Caching(
        evict = {
            @CacheEvict(value = PROXY_CACHE, key = "#root.args[0].path()"),
            @CacheEvict(value = PROXY_CACHE, key = "#root.args[0].path().substring(0, #root.args[0].path().lastIndexOf(\"/\"))")
        }
    )
    @DeleteMapping("/**")
    public ResponseEntity<?> proxyDelete(ProxyExchange<byte[]> proxy) throws Exception {
        log.info("==============================");
        log.info("SENDING HTTP-DELETE REQUEST...");
        String path = proxy.path(API_URL);
        return proxy
                .uri(baseUrl + path)
                .header("Accept-Encoding", "identity")
                .delete();
    }
}
