package net.sf.esfinge.querybuilder.finder;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlEntityFinder implements IFindable {

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public String search(String SearchCriteria) {
        var result = "";
        try {
            var xmlFile = new File("conf/Entities.xml");
            var dbFactory = DocumentBuilderFactory
                    .newInstance();
            var dBuilder = dbFactory.newDocumentBuilder();
            var doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            var nList = doc.getElementsByTagName("Entity");

            for (var temp = 0; temp < nList.getLength(); temp++) {
                var nNode = nList.item(temp);
                var eElement = (Element) nNode;
                var nodeNameT = eElement.getElementsByTagName("class").item(0).getChildNodes().item(0).getNodeValue();
                if (nodeNameT.trim().toUpperCase().endsWith(SearchCriteria.trim().toUpperCase())) {
                    result = nodeNameT;
                    break;
                }
            }
        } catch (IOException | ParserConfigurationException | DOMException | SAXException err) {
            err.printStackTrace();
        }
        return result;
    }

}
