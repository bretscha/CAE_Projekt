package Importer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.daverog.tripliser.Tripliser;
import org.daverog.tripliser.TripliserFactory;
import org.daverog.tripliser.exception.TripliserException;

public class ImporterBase {
    private InputStream inputXml = null;
    private InputStream mappingXml = null;
    private String rdfOutputPath = "";

    public ImporterBase(String input, String mapping, String rdfOutputPath) {
	setImportProperties(input, mapping, rdfOutputPath);
    }

    public void doImport() throws TripliserException {
	TripliserFactory instance = TripliserFactory.instance();
	TripliserFactory setMapping = instance.setMapping(mappingXml);
	Tripliser tripliser = instance.create();
	Tripliser setInputStream = tripliser.setInputStream(inputXml);
	String generateRdf = tripliser.generateRdf();
	try {
	    System.out.println("Writing RDF on new File: " + rdfOutputPath);
	    PrintWriter out = new PrintWriter(rdfOutputPath);
	    out.println(generateRdf);
	    out.flush();
	    System.out.println("Writing done");
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void setImportProperties(String inputPath, String mappingPath, String rdfOutputPath) {
	if (inputPath.equals("") || mappingPath.equals("") || rdfOutputPath.equals("")) {
	    System.out.println("no ImportProperties found. Setting example xmls");
	    inputXml = this.getClass().getResourceAsStream("input.xml");
	    mappingXml = this.getClass().getResourceAsStream("mappingStars.xml");
	    this.rdfOutputPath = "C:/Users/abpma/Documents/1UNI/8 Semester/CAE-Project/rdfOutput.xml";
	} else {
	    inputXml = this.getClass().getResourceAsStream(inputPath);
	    mappingXml = this.getClass().getResourceAsStream(mappingPath);
	    this.rdfOutputPath = rdfOutputPath;
	}
    }

    public InputStream getInputXml() {
	return inputXml;
    }

    public InputStream getMappingXml() {
	return mappingXml;
    }

    public String getRdfOutputPath() {
	return rdfOutputPath;
    }

    public void setInputXml(InputStream inputXml) {
	this.inputXml = inputXml;
    }

    public void setMappingXml(InputStream mappingXml) {
	this.mappingXml = mappingXml;
    }

    public void setRdfOutputPath(String rdfOutputPath) {
	this.rdfOutputPath = rdfOutputPath;
    }
}
