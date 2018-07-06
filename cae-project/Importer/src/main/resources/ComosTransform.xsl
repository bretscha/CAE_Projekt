<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
				xmlns:xsd="https://www.w3.org/TR/xmlschema11-2/#"
				xmlns:mso="http://eatld.et.tu-dresden.de/mso/" xmlns:vcard ="http://www.w3.org/2006/vcard/ns#"
				xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />

	<!-- Template: ComosExport -->
	<xsl:template match="/ComosXmlExport">
		<rdf:RDF>
			<!-- mso:Site -->
			<xsl:apply-templates select="./Site" />

		</rdf:RDF>
	</xsl:template>

	<!-- Template: Site -->
	<xsl:template match="Site">
		<xsl:for-each select=".">

			<mso:Site>
				<vcard:hasName><xsl:value-of select="./@Fullname"/></vcard:hasName>				
				<xsl:apply-templates select="./ProcessCell"/>
			</mso:Site>

		</xsl:for-each>
	</xsl:template>

<!-- Template ProcessCell -->
	<xsl:template match="ProcessCell">
		<xsl:for-each select=".">

			<mso:hasProcessCell>
				<mso:ProcessCell>
					<rdfs:label><xsl:value-of select="./@label"/></rdfs:label>
					<rdfs:label><xsl:value-of select="./@description"/></rdfs:label>
					<xsl:apply-templates select="./Unit"/>
				</mso:ProcessCell>
			</mso:hasProcessCell>

		</xsl:for-each>
	</xsl:template>

	<!-- Template Unit -->

	<xsl:template match="Unit">
		<xsl:for-each select=".">

			<mso:hasUnit>
				<mso:Unit>
					<rdfs:label><xsl:value-of select="./@label"/></rdfs:label>
					<rdfs:label><xsl:value-of select="./@description"/></rdfs:label>
					<xsl:apply-templates select="./Equipment"/>
<!-- 				<xsl:apply-templates select="./Armaturen"/>
 -->			</mso:Unit>
			</mso:hasUnit>

		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Equipment">
		<!-- for each equipment > Scan for name > Set Template -->
			<xsl:for-each select="./node()">
				
				<mso:hasEquipment>
					<mso:Equipment>
 						<xsl:apply-templates select="current()"/>
					</mso:Equipment>
				</mso:hasEquipment>
			
			</xsl:for-each>
	</xsl:template>

	<xsl:template match="Vessel">

				<rdfs:label><xsl:value-of select="./@label"/></rdfs:label>
				<rdfs:label><xsl:value-of select="./@description"/></rdfs:label>
	</xsl:template>



</xsl:stylesheet>