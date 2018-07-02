<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="https://www.w3.org/TR/xmlschema11-2/#" xmlns:caex="http://data.ifs.tuwien.ac.at/aml/ontology#"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">


	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />


	<!-- CAEXFile Template 100% -->
	<xsl:template match="/CAEXFile">
		<rdf:RDF>
			<caex:CAEXFile rdf:about="http://data.ifs.tuwien.ac.at/aml/ontology#CAEXFile">
				<caex:fileName>
					<xsl:value-of select="./@FileName" />
				</caex:fileName>
				<caex:schemaVersion>
					<xsl:value-of select="./@SchemaVersion" />
				</caex:schemaVersion>
				<caex:xSISchemaLocation>
					<xsl:value-of select="./@xsi:noNamespaceSchemaLocation" />
				</caex:xSISchemaLocation>

				<xsl:apply-templates select="./InstanceHierarchy"/>
				
				<xsl:apply-templates select="./AdditionalInformation" />
			</caex:CAEXFile>
		</rdf:RDF>
	</xsl:template>

	<!-- AdditionalInformation Template 100% -->
	<xsl:template match="AdditionalInformation">
		<caex:additionalInformation>
			<xsl:value-of select="/CAEXFile/AdditionalInformation/@AutomationMLVersion" />
		</caex:additionalInformation>
	</xsl:template>

	<!-- InstanceHierarchy Template 100% -->
	<xsl:template match="InstanceHierarchy">
		<xsl:for-each select=".">
			<!-- hasinstanceHierarchy -->
			<caex:instanceHierarchy>
				<caex:InstanceHierarchy rdf:resource="http://data.ifs.tuwien.ac.at/aml/ontology#InstanceHierarchy">
					<caex:name><xsl:value-of select="./@Name" /></caex:name>
					<xsl:apply-templates select="./InternalElement" />	
				</caex:InstanceHierarchy>
			</caex:instanceHierarchy>
			<!-- InternalElements -->

		</xsl:for-each>
	</xsl:template>

	<!-- InternalElement Template TODO SupportedRoleClass-->
	<xsl:template match="InternalElement">
		<xsl:for-each select=".">
			<!-- hasInternalElement -->
			<caex:internalElement>

				<caex:InternalElement rdf:resource="http://data.ifs.tuwien.ac.at/aml/ontology#InternalElement">
					<!-- name -->
					<caex:name><xsl:value-of select="./@Name" /></caex:name>
					<!-- RefBaseSystemUnitPath -->
					<xsl:if test="./@RefBaseSystemUnitPath != ''">
						<caex:refBaseSystemUnitPath>
							<xsl:value-of select="./@RefBaseSystemUnitPath" />
						</caex:refBaseSystemUnitPath>
					</xsl:if>
					<!-- ID -->
					<caex:iD><xsl:value-of select="./@ID" /></caex:iD>
					<!-- attribute -->
					<xsl:apply-templates select="./Attribute" />
					<xsl:apply-templates select="./InternalElement" />
<!-- 					<xsl:apply-templates select="./SupportedRoleClass" />
 -->				</caex:InternalElement>

			</caex:internalElement>
				


		</xsl:for-each>
	</xsl:template>
	
	<!-- Attribute Template -->
	<xsl:template match="Attribute">
		<xsl:for-each select=".">
			<caex:attribute>
			
				<caex:Attribute rdf:resource="http://data.ifs.tuwien.ac.at/aml/ontology#Attribute">
					<caex:name><xsl:value-of select="./@Name" /></caex:name>
					<caex:attributeDataType><xsl:value-of select="./@AttributeDataType" /></caex:attributeDataType>
					<caex:value><xsl:value-of select="./Value" /></caex:value>
				</caex:Attribute>

			</caex:attribute>
		</xsl:for-each>

	</xsl:template>

	<!-- SupportedRoleClass Template -->
	<xsl:template match="SupportedRoleClass">
		<xsl:for-each select=".">
						

		</xsl:for-each>
	</xsl:template>


</xsl:stylesheet>
