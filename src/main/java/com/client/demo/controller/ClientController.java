package com.client.demo.controller;

import com.client.demo.model.CatalogStock;
import com.client.demo.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Controller
public class ClientController {

    private final CatalogService catalogService;

    public ClientController(@Autowired CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping(value = "/v1/pubsub-message", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED, reason = "successful")
    public Mono<ResponseEntity<String>> processStock(@RequestBody CatalogStock catalogStock) {

        return catalogService.process(catalogStock)
                .map(s -> ResponseEntity.status(HttpStatus.CREATED).body("successful"));
    }
}
