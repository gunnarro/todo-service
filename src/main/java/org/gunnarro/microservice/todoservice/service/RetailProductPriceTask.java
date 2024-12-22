package org.gunnarro.microservice.todoservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * grocery store
 */
@Slf4j
@Service
public class RetailProductPriceTask {

    private final List<String> menyProductList;
    private final List<String> odaProductList;
    private final List<String> sparProductList;
    private final ObjectMapper jsonMapper;

    private RetailProductPriceTask() {
        this.menyProductList = new ArrayList<>();
        this.menyProductList.add("https://meny.no/varer/ost/brunost/gudbrandsdalsost-7038010029530");
        this.menyProductList.add("https://meny.no/varer/snacks-godteri/sjokolade/paskeegg-fylte-7040110642106");
        this.menyProductList.add("https://meny.no/varer/meieri-egg/egg/frokostegg-7039610000318");

        this.odaProductList = new ArrayList<>();
        this.odaProductList.add("https://oda.com/no/products/438-tine-skivet-gudbrandsdalsost/");
        this.odaProductList.add("https://oda.com/no/products/18196-freia-paskeegg-fylte-4-stk/");
        this.odaProductList.add("https://oda.com/no/products/28870-prior-frokostegg-fra-frittgaende-honer-str-l/");

        this.sparProductList = new ArrayList<>();
        this.sparProductList.add("https://spar.no/nettbutikk/varer/drikke/brus/coca-cola-5000112636833");
        this.sparProductList.add("");
        this.sparProductList.add("https://spar.no/nettbutikk/varer/meieri-og-egg/egg/frokostegg-7039610000318");



        this.jsonMapper = new JsonMapper();
    }

    /**
     * every 30 minutes = 1000 ms * 60 sec * 30 min
     */
   // @Scheduled(fixedDelay = 60000)
    public void checkProductPrice() {
            this.menyProductList.forEach( productUrl -> {
                try {
                    String productJson = getProductJsonFromHtmlPage(productUrl);
                    Map<String, Object> menyMap = jsonMapper.readValue(productJson, new TypeReference<Map<String, Object>>() {
                    });
                    log.info("Meny: {},{}, {}", menyMap.get("name"), menyMap.get("description"), ((Map<?, ?>) menyMap.get("offers")).get("price"));
                } catch (Exception e) {
                    log.error("", e);
                }
            });

        this.odaProductList.forEach( productUrl -> {
            try {
                String productJson = getProductJsonFromHtmlPage(productUrl);
                Map<String, Object> odaMap = jsonMapper.readValue(productJson, new TypeReference<Map<String, Object>>() {
                });
                log.info("Oda: {}, {}, {}", odaMap.get("name") ,  odaMap.get("description"), ((Map<?, ?>) odaMap.get("offers")).get("price"));
            } catch (Exception e) {
                log.error("", e);
            }
        });

        this.sparProductList.forEach( productUrl -> {
            try {
                String productJson = getProductJsonFromHtmlPage(productUrl);
                Map<String, Object> odaMap = jsonMapper.readValue(productJson, new TypeReference<Map<String, Object>>() {
                });
                log.info("Oda: {}, {}, {}", odaMap.get("name") ,  odaMap.get("description"), ((Map<?, ?>) odaMap.get("offers")).get("price"));
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private String getProductJsonFromHtmlPage(String productUrl) throws IOException {
        Document htmlDoc = Jsoup.connect(productUrl).get();
        Elements scriptTags = htmlDoc.select("script[type=application/ld+json]");
        //  scriptTags.forEach(script -> System.out.println("script: " + script.data()));
        return scriptTags.get(0).data();
    }

    class Product {
        LocalDateTime checkedDate;
        String name;
        String description;
        String price;
        List<String> groceryStores;
    }

    class groceryStore {
        String name;
    }
}
