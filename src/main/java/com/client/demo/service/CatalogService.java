package com.client.demo.service;

import com.client.demo.bean.CatalogWebClient;
import com.client.demo.exceptions.FatalException;
import com.client.demo.model.CatalogStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;

@Service
public class CatalogService {

    private final CatalogWebClient catalogWebClient;

    public CatalogService(@Autowired CatalogWebClient catalogWebClient) {
        this.catalogWebClient = catalogWebClient;

    }

    public Mono<String> process(CatalogStock catalogStock) {

        HashMap<String, String> catalogHeader = new HashMap<>() {{
            put("tenant", "SOME");
            put("AUTHORIZATION", "SOME AUTH TOKEN");
        }};

        List<String> variantIds = List.of(
                "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009", "1010"
                , "4001", "4002", "4003", "4004", "4005", "4006", "4007", "4008", "4009", "4010"
                , "3001", "3002", "3003", "3004", "3005", "3006", "3007", "3008", "3009", "3010"
        );


        List<String> postIds = List.of(
                "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010"
                , "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009", "1010"
                , "001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010"
        );

        String bundleItemId = catalogStock.getVariantId();
        List<CatalogStock> zeroQuantityStock = List.of(new CatalogStock(bundleItemId, null, bundleItemId, null, null, null, "OutOfStock", false, 0, null));
        return Flux.fromIterable(variantIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(variantId ->
                        catalogWebClient.getAll("/stocks?variantId=" + variantId, CatalogStock.class, catalogHeader)
                                .onErrorResume(throwable -> {
                                    if (throwable instanceof FatalException) {
                                        FatalException ex = (FatalException) throwable;
                                        if (ex.getHttpStatus().equals(HttpStatus.NOT_FOUND)) {
                                            return Mono.just(zeroQuantityStock);
                                        } else return Mono.error(throwable);
                                    } else return Mono.error(throwable);
                                })
                )
                .sequential()
                .collectList()
                .flatMap(lists -> postAll(postIds, catalogStock, catalogHeader))
                .map(strings -> "success");
    }

    private Mono<List<String>> postAll(List<String> postIds, CatalogStock catalogStock, HashMap<String, String> catalogHeader) {
        return Flux.fromIterable(postIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(id -> catalogWebClient.post(catalogStock, "/stocks", String.class, catalogHeader))
                .sequential()
                .collectList();
    }
}
