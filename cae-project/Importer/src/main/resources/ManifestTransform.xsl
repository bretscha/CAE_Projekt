<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  version="1.0"
				 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				 xmlns:xsd="https://www.w3.org/TR/xmlschema11-2/#"
				 xmlns:caex="http://data.ifs.tuwien.ac.at/aml/ontology#"
                 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 >
 

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>


	<!-- CAEXFile Template -->
	<xsl:template match="/">
		<rdf:RDF>
			<caex:CAEXFile rdf:about="http://data.ifs.tuwien.ac.at/aml/ontology#CAEXFile">
				<caex:fileName><xsl:value-of select="/CAEXFile/@FileName" /></caex:fileName>				
				<caex:schemaVersion><xsl:value-of select="/CAEXFile/@SchemaVersion" /></caex:schemaVersion>
				<caex:xSISchemaLocation><xsl:value-of select="/CAEXFile/@xsi:noNamespaceSchemaLocation" /></caex:xSISchemaLocation>
				<xsl:apply-templates select="/CAEXFile/AdditionalInformation"/>
			</caex:CAEXFile>
		</rdf:RDF> 
	</xsl:template>
	<!-- AdditionalInformation Template -->
	<xsl:template match="/CAEXFile/AdditionalInformation">
		<caex:additionalInformation><xsl:value-of select="/CAEXFile/AdditionalInformation/@AutomationMLVersion" /></caex:additionalInformation>

	</xsl:template>




 </xsl:stylesheet>
