import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.xml.sax.*;
import org.w3c.dom.*; 
import java.io.*;

public class XMLValidator{
    public static boolean validate(String xml, String xsd, boolean isXMLPath){ //(01)
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
            Document d = isXMLPath ? db.parse(new File(xml)) : db.parse(new InputSource(new StringReader(xml)));
            Schema s = sf.newSchema(new StreamSource(xsd)); 
            s.newValidator().validate(new DOMSource(d));
        }catch(Exception e){
            if(e instanceof SAXException)
                System.out.println("Errore di validazione: " + e.getMessage());
            else
                System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

/*
Note
(01)
    Consente la validazione di un file o stringa XML con uno schema XSD specificato.
    Ponendo isXMLPath a true permette di validare un file XML specificando il suo percorso, altrimenti se false 
    permette di validare un stringa.
*/