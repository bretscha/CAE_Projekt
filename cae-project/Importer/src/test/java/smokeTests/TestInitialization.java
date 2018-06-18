package smokeTests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.Test;

import generated.CAEXFile.Attribute;
import generated.CAEXFile.CAEXFile;
import generated.CAEXFile.InstanceHierarchy;
import generated.CAEXFile.InternalElement;
import utilities.UtilityA;

public class TestInitialization {
    private String pathToAml = "..\\Manifest.aml";

    /**
     * tests interconnectivity between modules
     * 
     * @throws JAXBException
     */
    @Test
    public void TestIntermodularConnectivity() {
	String sayHello = UtilityA.sayHello();
	assertEquals(sayHello, "Servus", "Couldnt connect to Modue");
    }

    /**
     * this Method test if the Manifest.aml can be unmarshalled into Java Objects
     * 
     * @throws JAXBException
     */
    @Test
    public void TestUnmarshall() throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(CAEXFile.class);
	Unmarshaller unmarshaller = context.createUnmarshaller();
	CAEXFile caexFile = (CAEXFile) unmarshaller.unmarshal(new File(pathToAml));
	assertNotNull(caexFile, "caexFile was not Found. Please give the right Path to the Manifest.aml");
	List<InstanceHierarchy> instanceHierarchies = caexFile.getInstanceHierarchy();
	InstanceHierarchy instanceHierarchy = new InstanceHierarchy();
	for (InstanceHierarchy instanceHierarchy2 : instanceHierarchies) {
	    String name = instanceHierarchy2.getName();
	    if (name.equalsIgnoreCase("HMI")) {
		instanceHierarchy = instanceHierarchy2;
		break;
	    }
	}
	assertNotNull(instanceHierarchy);
	InternalElement internalElement = instanceHierarchy.getInternalElement().get(0);
	Attribute attribute = internalElement.getAttribute().get(0);
	String value = attribute.getValue();
	assertNotNull(value);
    }
}
