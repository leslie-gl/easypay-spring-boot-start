package com.leslie.framework.easypay.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * xml 工具类
 *
 * @author leslie
 * @date 2020/9/7
 */
@Slf4j
public class XmlUtils {

    private XmlUtils() {
    }

    public static Document buildDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

    /**
     * 将XML格式字符串转换为Map
     *
     * @param xmlStr XML格式字符串
     * @return Map<String, String>
     */
    public static Map<String, String> xml2Map(String xmlStr) {
        byte[] xmlBytes = StrUtils.convert2Utf8(xmlStr).getBytes();
        Document document;

        try (InputStream is = new ByteArrayInputStream(xmlBytes)) {

            DocumentBuilder documentBuilder = newDocumentBuilder();
            document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();

        } catch (Exception e) {
            String errorMessage = "can not convert XML to Map, reason=" + e.getMessage();
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
        return parseDocument(document);
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map<String, String>
     * @return XML格式的字符串
     */
    public static String map2Xml(Map<String, String> data) {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        try {
            Transformer transformer = createTransformer();
            transformer.transform(new DOMSource(createDocument(data)), result);
        } catch (Exception e) {
            String errorMessage = "can not convert Map to XML, reason=" + e.getMessage();
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
        return writer.getBuffer().toString();
    }

    /**
     * 解析 Document，生成Map集合
     *
     * @param document XML文档对象
     * @return Map集合
     */
    private static Map<String, String> parseDocument(Document document) {
        Map<String, String> data = new HashMap<>(16);
        if (document == null) {
            return data;
        }
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        return data;
    }

    private static Document createDocument(Map<String, String> data) throws ParserConfigurationException {
        Document document = buildDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isBlank(key)) {
                continue;
            }
            Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(StringUtils.trimToEmpty(value)));
            root.appendChild(filed);
        }
        return document;
    }

    private static Transformer createTransformer() throws TransformerConfigurationException {
        Transformer transformer = newTransformerFactory().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

    /**
     * https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html
     */
    private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    private static TransformerFactory newTransformerFactory() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        return transformerFactory;
    }

}
