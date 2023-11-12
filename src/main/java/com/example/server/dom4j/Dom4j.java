package com.example.server.dom4j;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Dom4j {

    public static void main(String[] args) throws DocumentException {
        SAXReader saxReader=new SAXReader();
        //根据user.xml文档生成Document对象
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Document document = saxReader.read(resourceLoader.getClassLoader().getResource("ICSR.xml"));
        Element rootElement = document.getRootElement();
        Element porr_in049016UV = rootElement.element("PORR_IN049016UV");
        Element controlActProcess = porr_in049016UV.element("controlActProcess");

        List<Element> elements = rootElement.elements("D.1");
        String name = rootElement.getName();
        Object data = rootElement.getData();
        String text = rootElement.getText();
        System.out.println(1);
    }
}
