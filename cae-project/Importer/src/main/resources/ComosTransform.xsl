<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="https://www.w3.org/TR/xmlschema11-2/#" xmlns:mso="http://eatld.et.tu-dresden.de/mso/"
	xmlns:vcard="http://www.w3.org/2006/vcard/ns#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">
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
				<vcard:hasName>
					<xsl:value-of select="./@name" />
				</vcard:hasName>
				<rdfs:label>
					<xsl:value-of select="./@label" />
				</rdfs:label>
				<xsd:string>
					<xsl:value-of select="./@description" />
				</xsd:string>
				<xsl:apply-templates select="./ProcessCell" />
			</mso:Site>

		</xsl:for-each>
	</xsl:template>

	<!-- Template ProcessCell -->
	<xsl:template match="ProcessCell">
		<xsl:for-each select=".">

			<mso:hasProcessCell>
				<mso:ProcessCell>
					<rdfs:label>
						<xsl:value-of select="./@label" />
					</rdfs:label>
					<xsd:string>
						<xsl:value-of select="./@description" />
					</xsd:string>
					<xsl:apply-templates select="./Unit" />
				</mso:ProcessCell>
			</mso:hasProcessCell>

		</xsl:for-each>
	</xsl:template>

	<!-- Template Unit -->

	<xsl:template match="Unit">
		<xsl:for-each select=".">

			<mso:hasUnit>
				<mso:Unit>
					<rdfs:label>
						<xsl:value-of select="./@label" />
					</rdfs:label>
					<xsd:string>
						<xsl:value-of select="./@description" />
					</xsd:string>
					<xsl:apply-templates select="./Equipment" />
					<xsl:apply-templates select="./Armature" />
					<xsl:apply-templates select="./Pipe" />
				</mso:Unit>
			</mso:hasUnit>

		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Equipment">
		<!-- for each equipment > Scan for name > Set Template -->
		<xsl:for-each select=".">

			<mso:hasEquipment>
				<mso:Equipment>
					<rdf:type>
						<xsl:value-of select="./@label" />
					</rdf:type>
					<rdf:label>
						<xsl:value-of select="./@description" />
					</rdf:label>
					<mso:hasProceedingTemperature>
						<xsl:value-of select="./@ProceedingTemperature" />
					</mso:hasProceedingTemperature>
					<mso:hasAllowableTemperature>
						<xsl:value-of select="./@AllowableTemperature" />
					</mso:hasAllowableTemperature>
					<mso:hasProceedingPressure>
						<xsl:value-of select="./@ProceedingPressure" />
					</mso:hasProceedingPressure>
					<mso:hasAllowablePressure>
						<xsl:value-of select="./@AllowablePressure" />
					</mso:hasAllowablePressure>
					<mso:material>
						<xsl:value-of select="./@material" />
					</mso:material>
					<mso:hasHeight>
						<xsl:value-of select="./@Height" />
					</mso:hasHeight>
					<mso:hasCapacity>
						<xsl:value-of select="./@Volume" />
					</mso:hasCapacity>
					<mso:hasInnerDiameter>
						<xsl:value-of select="./@InnerDiameter" />
					</mso:hasInnerDiameter>
					<!-- <mso:reserve><xsl:value-of select="./@"/></mso:reserve> -->
				</mso:Equipment>
			</mso:hasEquipment>

		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Armature">
		<!-- for each equipment > Scan for name > Set Template -->
		<xsl:for-each select=".">

			<mso:Armature>
				<mso:Valve>
					<rdf:type>
						<xsl:value-of select="./@label" />
					</rdf:type>
					<rdf:label>
						<xsl:value-of select="./@description" />
					</rdf:label>
					<mso:hasAllowableOperatingPressure>
						<xsl:value-of select="./@ProceedingTemperature" />
					</mso:hasAllowableOperatingPressure>
					<mso:hasAllowableOperatingTemperature>
						<xsl:value-of select="./@AllowableTemperature" />
					</mso:hasAllowableOperatingTemperature>
					<mso:hasDesignPressure>
						<xsl:value-of select="./@ProceedingPressure" />
					</mso:hasDesignPressure>
					<mso:hasDesignTemperature>
						<xsl:value-of select="./@AllowablePressure" />
					</mso:hasDesignTemperature>
					<mso:material>
						<xsl:value-of select="./@material" />
					</mso:material>
					<!-- <mso:positionFeedback><xsl:value-of select="./@"/></mso:positionFeedback> <mso:safetyPosition><xsl:value-of select="./@"/></mso:safetyPosition> -->
				</mso:Valve>
			</mso:Armature>

		</xsl:for-each>
	</xsl:template>

	<!-- <xsl:template match="Vessel"> <rdfs:label><xsl:value-of select="./@label"/></rdfs:label> <xsd:string><xsl:value-of select="./@description"/></xsd:string> </xsl:template> -->
	<xsl:template match="Pipe">
		<!-- for each equipment > Scan for name > Set Template -->
		<xsl:for-each select=".">
		
			<mso:has>
				<mso:Pipe>
						<rdf:label>
							<xsl:value-of select="./@label" />
						</rdf:label>
						<rdf:label>
							<xsl:value-of select="./@description" />
						</rdf:label>
						<mso:hasAllowableTemperature>
							<xsl:value-of select="./@AllowableTemperature" />
						</mso:hasAllowableTemperature>
						<mso:hasAllowablePressure>
							<xsl:value-of select="./@AllowablePressure" />
						</mso:hasAllowablePressure>
				</mso:Pipe>
			</mso:has>

		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>