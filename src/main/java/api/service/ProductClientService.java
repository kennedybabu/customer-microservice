package api.service;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ProductClientService {

    private final WebClient productWebClient;


    public ProductClientService(WebClient productWebClient) {
        this.productWebClient = productWebClient;
    }

    public List<String> getAllProductTypes() {
        return productWebClient
                .get()
                .uri("/api/product/types")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();
    }

    public List<String> getProductDescriptionByType(String productType) {
        return productWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/product/search/type")
                        .queryParam("productType", productType)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .map(product -> (String) product.get("productDescription"))
                .collectList()
                .block(); // Blocks to convert reactive to synchronous
    }
}
