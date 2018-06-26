package smokeTests;

import static org.testng.Assert.assertEquals;

import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

import utilities.UtilityA;

public class TestInitialization {

    /**
     * tests interconnectivity between modules
     * 
     * @throws JAXBException
     */
    @Test(groups = { "include-group" })
    public void TestIntermodularConnectivity() {
	String sayHello = UtilityA.sayHello();
	assertEquals(sayHello, "Servus", "Couldnt connect to Modue");
    }
}
