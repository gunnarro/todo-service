package org.gunnarro.microservice.todoservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

@Disabled
public class GetHtmlPageTest {

    @Test
    void compareProduct() throws IOException {
       String menyProductJson = getProductJsonFromHtmlPage("https://meny.no/varer/ost/brunost/gudbrandsdalsost-7038010029530");
       String odaProductJson = getProductJsonFromHtmlPage("https://oda.com/no/products/438-tine-skivet-gudbrandsdalsost/");


       System.out.println("Meny: " + menyProductJson);
       System.out.println("Oda: " + odaProductJson);

        ObjectMapper jsonMapper = new JsonMapper();
        Map<String, Object> map = jsonMapper.readValue(menyProductJson, new TypeReference<Map<String,Object>>(){});
        System.out.println("Meny: " + map.get("name") + ", " + map.get("description") + ", " + ((Map<?, ?>)map.get("offers")).get("price"));
        map = jsonMapper.readValue(odaProductJson, new TypeReference<Map<String,Object>>(){});
        System.out.println("Oda:" + map.get("name") + ", " + map.get("description") + ", " + ((Map<?, ?>)map.get("offers")).get("price"));
        System.out.println(map);
    }

    private String getProductJsonFromHtmlPage(String productUrl) throws IOException {
        Document htmlDoc = Jsoup.connect(productUrl).get();
        Elements scriptTags = htmlDoc.select("script[type=application/ld+json]");
      //  scriptTags.forEach(script -> System.out.println("script: " + script.data()));
        return scriptTags.get(0).data();
    }
}
