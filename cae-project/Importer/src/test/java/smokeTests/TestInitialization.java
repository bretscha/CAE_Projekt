package smokeTests;

import static org.testng.Assert.assertEquals;

import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

public class TestInitialization {

    /**
     * tests interconnectivity between modules
     * 
     * @throws JAXBException
     */
    @Test(groups = { "include-group" })
    public void TestIntermodularConnectivity() {
	String sayHello = "Hello";
	assertEquals(sayHello, "Hello", "Couldnt connect to Modue");
    }
}
