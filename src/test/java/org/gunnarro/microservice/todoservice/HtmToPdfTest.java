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
import java.util.HashMap;
import java.util.Map;

// https://kb.itextpdf.com/itext/chapter-4-creating-reports-using-pdfhtml
public class HtmToPdfTest {

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
