package app.cib.service.bat;

import java.util.*;
import javax.servlet.http.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import java.io.File;

public class BatchXMLFactory {

    private static HashMap instances =
            new HashMap(20);
    private static String xmlDirName = Config.getProperty("BatchFileXMLDir") + "/";
    /**
     *    私有的构造子保证外界无法直接将此类实例化
     */
    private BatchXMLFactory() {
    }

    public static void setXMLDir(String newXMLDir) {
        xmlDirName = newXMLDir + "/";
    }

    public static String setXMLDir() {
        return xmlDirName;
    }

    public synchronized static XMLElement
            getBatchXML(String fileName) throws NTBException {
        fileName = xmlDirName + fileName + ".xml";
        if (instances.containsKey(fileName)) {
            return (XMLElement) instances.get(fileName);
        } else {
            try {
                File tempFile = new File(fileName);
                if(!tempFile.exists()){
                    Log.error(
                            "XML file " + fileName + " not exist");
                    return null;
                }
                XMLParser xmlParser = XMLFactory.getParser();
                xmlParser.setInput(fileName);
                xmlParser.unMarshal();
                XMLElement docElement = xmlParser.getRootElement();
                instances.put(fileName, docElement);
                return docElement;
            } catch (Exception e) {
                Log.error(
                        "XML file " + fileName + " initialzation error",
                        e);
                return null;
            }
        }
    }
}
