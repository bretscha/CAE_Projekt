<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
                xmlns:quantity="http://data.nasa.gov/qudt/owl/quantity#"
                xmlns:ssn="http://purl.oclc.org/NET/ssnx/ssn#"
                xmlns:cat="http://www.sensorhome.com/Catalog/"
                xmlns:ont="https://github.com/bretscha/CAE_Projekt/tree/master/cae-project/src/main/resources/ontologies/"
                xmlns:unit="http://data.nasa.gov/qudt/owl/unit#"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:qudt="http://data.nasa.gov/qudt/owl/qudt#">
  <xsl:output method="xml"
              version="1.0"
              encoding="UTF-8"
              indent="yes"/>	
    <xsl:template match="/">
    <rdf:RDF> 
      <ssn:SensingDevice rdf:about="http://www.sensorhome.com/Catalog/TemperatureSensor_TS_2000">
        <ssn:observes rdf:resource="http://data.nasa.gov/qudt/owl/quantity#ThermodynamicTemperature"/>
        <ssn:hasMeasurementCapability>
          <ssn:MeasurementCapability rdf:about="http://www.sensorhome.com/Catalog/TS_2000_MeasurementCapability">
            <!--Aufruf der anderen Templates-->
            <xsl:apply-templates select="/ComosXmlExport/Sensor/Messbereich"/>
            <xsl:apply-templates select="/ComosXmlExport/Sensor/Genauigkeit"/>
          </ssn:MeasurementCapability>
        </ssn:hasMeasurementCapability>
        <!--Zusatzinfo - Im Katalog angeboten-->
        <ont:isOfferedInCatalog>
          <ont:Catalog rdf:about="http://www.sensorhome.com/Catalog/catalog-2013">
          </ont:Catalog>
        </ont:isOfferedInCatalog>
      </ssn:SensingDevice>
    </rdf:RDF>
	</xsl:template>
</xsl:stylesheet>