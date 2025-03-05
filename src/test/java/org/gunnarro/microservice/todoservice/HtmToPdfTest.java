package org.gunnarro.microservice.todoservice;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// https://kb.itextpdf.com/itext/chapter-4-creating-reports-using-pdfhtml
public class HtmToPdfTest {

    @Test
    void mod() {

        int antall = 40;
        int[] elements = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int rest = antall % elements.length;
        int perElement = antall / elements.length;

        //   assertEquals(0, perElement);
        //   assertEquals(9, rest);

        for (int i = 0; i < elements.length; i++) {
            if (perElement == 0 && i < rest) {
                elements[i] = 1;
            } else if (i == 0) {
                elements[i] = perElement + rest;
            } else {
                elements[i] = perElement;
            }
        }

        Arrays.stream(elements).forEach(i -> System.out.println("a=" + i));

        int dividend = 6;
        int divisor = elements.length;

        int quotient = dividend / divisor;
        int remainder = dividend % divisor;

        System.out.println("The Quotient is = " + quotient);
        System.out.println("The Remainder is = " + remainder);
    }


    @Test
    void htmlToPdf() throws IOException {

        java.io.File resource = new ClassPathResource("todo-template.mustache").getFile();
        String todoMustacheTemplate = new String(Files.readAllBytes(resource.toPath()));

        TodoDto todoDto = new TodoDto();

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(todoMustacheTemplate), "");
        Map<String, Object> context = new HashMap<>();
        context.put("todo", todoDto);
        StringWriter writer = new StringWriter();
        mustache.execute(writer, context);
        String html = writer.toString();

        ConverterProperties properties = new ConverterProperties();
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileOutputStream outputStream = new FileOutputStream(new File("todo.pdf"));
        HtmlConverter.convertToPdf(html, outputStream);
        outputStream.close();

        // System.out.println("PDF generated from custom HTML content. bytes=" + outputStream.toByteArray().length);
/*
        MemoryStream ms = new MemoryStream(Input)) {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.GetInstance(doc, ms);
            doc.Open();
    }*/
    }
}
