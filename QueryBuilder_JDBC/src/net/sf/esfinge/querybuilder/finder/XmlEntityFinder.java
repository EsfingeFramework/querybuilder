package net.sf.esfinge.querybuilder.finder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XmlEntityFinder implements IFindable {
	public String search(String SearchCriteria) {
		String result = "";
		try {
			File xmlFile = new File("conf/Entities.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Entity");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
				String nodeNameT = eElement.getElementsByTagName("class").item(0).getChildNodes().item(0).getNodeValue();
				if (nodeNameT.trim().toUpperCase().endsWith(SearchCriteria.trim().toUpperCase())) {
					result = nodeNameT;				
					break;
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return result;
	}

}