package com.client.demo.bean;

import com.client.demo.exceptions.FatalException;
import com.client.demo.exceptions.RetryableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.List;
import java.util.Map;

@Component
public class CatalogWebClient {
    private final WebClient webClient;

    public CatalogWebClient(@Autowired WebClient.Builder webClientBuilder) {

        ConnectionProvider provider = ConnectionProvider.create("custom", 10);
        HttpClient httpClient = HttpClient.create(provider);

        this.webClient = webClientBuilder
                .baseUrl("http://localhost:3000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public <T> Mono<T> post(Object body, String uri, Class<T> clazz, Map<String, String> headers) {
        return webClient
                .post()
                .uri(uri)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.TOO_MANY_REQUESTS), response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED), response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.SERVICE_UNAVAILABLE), response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .onStatus(HttpStatus::is4xxClientError, response -> response.toEntity(String.class)
                        .flatMap(this::toFatal))
                .onStatus(HttpStatus::is5xxServerError, response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .bodyToMono(clazz);
    }

    public <T> Mono<List<T>> getAll(String uri, Class<T> returnType, Map<String, String> headers) {
        return webClient
                .get()
                .uri(uri)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.TOO_MANY_REQUESTS), response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED), response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.SERVICE_UNAVAILABLE), response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .onStatus(HttpStatus::is4xxClientError, response -> response.toEntity(String.class)
                        .flatMap(this::toFatal))
                .onStatus(HttpStatus::is5xxServerError, response -> response.toEntity(String.class)
                        .flatMap(this::toRetryable))
                .bodyToFlux(returnType)
                .collectList();
    }

    private Mono<? extends Throwable> toFatal(ResponseEntity<String> objectResponseEntity) {
        return Mono.error(new FatalException(objectResponseEntity.getStatusCode(), objectResponseEntity.getBody()));
    }

    private Mono<? extends Throwable> toRetryable(ResponseEntity<String> mapResponseEntity) {
        return Mono.error(new RetryableException(mapResponseEntity.getStatusCode(), mapResponseEntity.getBody()));
    }
}
