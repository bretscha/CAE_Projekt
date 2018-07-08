<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
							 xmlns:caex="http://data.ifs.tuwien.ac.at/aml/ontology#"
							  
							  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    						   xmlns:rs="http://www.w3.org/2001/sw/DataAccess/tests/result-set#"
    						  xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
    						  
    					 		  xmlns="http://graphml.graphdrawing.org/xmlns"  
     						  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     				 xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns 
        								 http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
	
	<xsl:template match="/">
		<xsl:text>&#xa;</xsl:text>	
		<graphml>
			<!-- Variablen anlegen -->
			<key id="node_label" for="node" attr.name="label"  attr.type="string"/>
			<key id="edge_label" for="edge" attr.name="label"  attr.type="string"/>
			<graph id="G" edgedefault="undirected">
			<!-- <xsl:apply-templates match="rs:ResultSet"/>	-->
			<xsl:apply-templates/>
			</graph>		               					    
		</graphml>
	</xsl:template>
	
	<xsl:template match="rs:ResultSet">
		<!-- <xsl:call-template name="identification_if"/>-->
		<xsl:for-each select="rs:solution">	
			<xsl:call-template name="create_nodes_from_id"/>
			<xsl:call-template name="create_other_nodes"/>
			<xsl:call-template name="create_edges"></xsl:call-template>
		</xsl:for-each>
		<!--<xsl:apply-templates select="rs:solution/rs:binding/rs:value" />-->
		<!--<xsl:apply-templates select="rs:solution" />-->
	</xsl:template>

	<!-- create one node for every id *** working -->
	<xsl:template name="create_nodes_from_id">
		<!-- search currently selected solution for ids -->
			<xsl:for-each select="rs:binding">
				<!-- check if solution contains an ID -->
				<xsl:if test="rs:variable = 'p' and rs:value/@rdf:resource = 'http://theuniverse.org/id'">				
					
					<!-- load subject from same solution as node -->
					<xsl:variable name="found_node">
						<xsl:for-each select="../rs:binding">
							<xsl:if test="rs:variable = 's'">
								<xsl:value-of select="rs:value/@rdf:resource"/>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					
					<!-- load object from same solution as node id -->
					<xsl:variable name="node_id">
						<xsl:for-each select="../rs:binding">
							<xsl:if test="rs:variable = 'o'">
								<xsl:value-of select="rs:value"/>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>

					<!-- search other solutions for matching label -->
					<xsl:variable name="node_label">
						<xsl:for-each select="../../rs:solution">
							<!-- test if solution contains any title -->
							<xsl:if test="rs:binding/rs:variable = 'p' and rs:binding/rs:value/@rdf:resource = 'http://purl.org/dc/elements/1.1/title'">
								<!-- test if solution contains the matching title -->
								<xsl:if test="rs:binding/rs:variable = 's' and rs:binding/rs:value/@rdf:resource = $found_node">
									
									<!-- load label into variable -->
									<xsl:for-each select="rs:binding">
										<xsl:if test="rs:variable = 'o'">
											<xsl:value-of select="rs:value"/>
										</xsl:if>
									</xsl:for-each>
									
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					
					<!-- create node -->
					<node id="{$node_id}">
						<data key="node_label">
							<xsl:value-of select="$node_label"/>
						</data>
					</node>
					
				</xsl:if>
			</xsl:for-each>
	</xsl:template>
	
	<!-- create one node for other item *** working but questionable -->
	<xsl:template name="create_other_nodes">
		<!-- search currently selected solution -->
			<xsl:for-each select="rs:binding">
				<!-- choose 'p' binding -->
				<xsl:if test="rs:variable = 'p'">
					<!-- check if not title nor id -->
					<xsl:if test="not(rs:value/@rdf:resource = 'http://purl.org/dc/elements/1.1/title' or rs:value/@rdf:resource = 'http://theuniverse.org/id')">
						<!-- node found, that still has to be created! -->
						
						<!-- load object from currently selected solution -->
						<xsl:variable name="node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if test="rs:variable = 'o'">
									<!-- check if value or attribute -->
									<xsl:choose> 
										<xsl:when test = "rs:value/@rdf:resource"> 
						            		<xsl:value-of select="rs:value/@rdf:resource"/>
						            	</xsl:when> 
						            	<xsl:otherwise>									
											<xsl:value-of select="rs:value"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</xsl:for-each>
						</xsl:variable>
						
						<!-- create node -->
						<node id="{$node_id}">
							<data key="node_label">
								<xsl:value-of select="$node_id"/>
							</data>
						</node>
						
					</xsl:if>					
				</xsl:if>
			</xsl:for-each>
	</xsl:template> 

	<!-- create one edge for each triple except for IDs and titles *** working -->
	<xsl:template name="create_edges">
		<!-- only create edges, that are no titles nor IDs -->
		<xsl:if test="not(rs:binding/rs:value/@rdf:resource = 'http://purl.org/dc/elements/1.1/title' or rs:binding/rs:value/@rdf:resource = 'http://theuniverse.org/id')">
				
			<!-- identify edge source from triple subject -->
			<xsl:variable name='edge_source_id'>
				<!-- pick binding "subject" -->
				<xsl:for-each select="rs:binding">
					<xsl:if test="rs:variable = 's'">
					
						<!-- load subject --> 
						<xsl:variable name='edge_source'>
							<xsl:value-of select="rs:value/@rdf:resource"/>
						</xsl:variable>
						
						<!-- check all solutions to find triple with subjects ID -->
						<xsl:for-each select="../../rs:solution">
							<!-- check if solution contains any ID -->
							<xsl:if test="rs:binding/rs:value/@rdf:resource='http://theuniverse.org/id'">
								<!-- check if solution contains matching ID -->
								<xsl:if test="rs:binding/rs:value/@rdf:resource=$edge_source">
									<!-- enter object binding -->
									<xsl:for-each select="rs:binding">
										<xsl:if test="rs:variable='o'">
											<!-- extract value of ID -->
											<xsl:value-of select="rs:value"/>
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
						
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			
			<!-- identify label from triple predicate -->
			<xsl:variable name='edge_label'>
				<xsl:for-each select="rs:binding">
					<xsl:if test="rs:variable = 'p'">
						<!-- extract label from triple -->
						<xsl:value-of select="rs:value/@rdf:resource"></xsl:value-of>
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			
			
			<xsl:variable name='edge_target'>
				<xsl:for-each select="rs:binding">
					<xsl:if test="rs:variable = 'o'">				
						<!-- load object --> 
						
							<xsl:choose> 
								<xsl:when test = "rs:value/@rdf:resource"> 
						            <xsl:value-of select="rs:value/@rdf:resource"/>
						        </xsl:when> 
						        <xsl:otherwise>									
									<xsl:value-of select="rs:value"/>
								</xsl:otherwise>
							</xsl:choose>

					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			
			<!-- identify edge target ID from triple object -->
			<xsl:variable name='edge_target_id'>
				<!-- pick binding "object" -->
				<xsl:for-each select="rs:binding">
					<xsl:if test="rs:variable = 'o'">

						<!-- check all solutions to find triple with objects ID if possible -->
						<xsl:for-each select="../../rs:solution">
							<!-- check if solution contains any ID -->
							<xsl:if test="rs:binding/rs:value/@rdf:resource='http://theuniverse.org/id'">
								<!-- check if solution contains matching ID -->
								<xsl:if test="rs:binding/rs:value/@rdf:resource=$edge_target">
									<!-- enter object binding -->
									<xsl:for-each select="rs:binding">
										<xsl:if test="rs:variable='o'">
											<!-- extract value of ID -->
											<xsl:value-of select="rs:value"/>
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
						
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			
			<!-- create unique ID -->
			<!-- mind the possibility of not finding a matching ID -->
			<xsl:variable name='edge_id'>
				<xsl:value-of select="$edge_source_id"></xsl:value-of>
				<xsl:text>_to_</xsl:text>
				
				<xsl:choose> 
					<xsl:when test = "not($edge_target_id='')"> 
						<xsl:value-of select="$edge_target_id"></xsl:value-of>
					</xsl:when> 
					<xsl:otherwise>									
						<xsl:value-of select="$edge_target"></xsl:value-of>
					</xsl:otherwise>
				</xsl:choose>
							
				<xsl:value-of select="$edge_target_id"></xsl:value-of>
				
			</xsl:variable>
			
			<!-- create edge -->
			<!-- mind the possibility of not finding a matching ID -->
			<xsl:choose> 
				<xsl:when test = "not($edge_target_id='')"> 
					<edge id="{$edge_id}" source="{$edge_source_id}" target="{$edge_target_id}">
						<data key="edge_label">
							<xsl:value-of select="$edge_label"/>
						</data>
					</edge>
				</xsl:when> 
				<xsl:otherwise>									
					<edge id="{$edge_id}" source="{$edge_source_id}" target="{$edge_target}">
						<data key="edge_label">
							<xsl:value-of select="$edge_label"/>
						</data>
					</edge>
				</xsl:otherwise>
			</xsl:choose>
			
			
		</xsl:if>	
	</xsl:template>

	<!-- enter each stage of xml and print for learning puposes -->
	<xsl:template name="identification_switch">
	<xsl:text>&#xa;-----identifying-----</xsl:text>			
		<xsl:for-each select="rs:solution">	
			<xsl:for-each select="rs:binding">
				<xsl:choose> 
	            	<xsl:when test = "rs:variable = 'o'"> 
	            		<xsl:text>&#xa;-----object_found-----</xsl:text>
						<xsl:value-of select = "rs:value"/> 
	            	</xsl:when> 
					<xsl:when test = "rs:variable = 'p'"> 
		                <xsl:text>&#xa;-----praedicat_found-----</xsl:text>	
						<xsl:value-of select = "rs:value/@rdf:resource"/>
						<xsl:if test="rs:value/@rdf:resource = 'http://theuniverse.org/id'">
							<xsl:text>&#xa;*****id_found*****</xsl:text>	
						</xsl:if>
	                </xsl:when> 
					<xsl:when test = "rs:variable = 's'"> 
		                <xsl:text>&#xa;-----subject_found-----</xsl:text>
						<xsl:value-of select = "rs:value/@rdf:resource"/>
	                </xsl:when>
	            </xsl:choose>           			
			</xsl:for-each>
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>

	<!-- enter each stage of xml and print for learning puposes -->	
	<xsl:template name="identification_if">	
	<xsl:text>&#xa;-----identifying-----</xsl:text>		
		<xsl:for-each select="rs:solution">	
			<xsl:for-each select="rs:binding">
				<xsl:if test="rs:variable = 'o'">
					<xsl:text>&#xa;-----object_found-----</xsl:text>
					<xsl:value-of select = "rs:value"/>
				</xsl:if>
				<xsl:if test="rs:variable = 'p'">
					<xsl:text>&#xa;-----praedicat_found-----</xsl:text>	
					<xsl:value-of select = "rs:value/@rdf:resource"/>
					<xsl:if test="rs:value/@rdf:resource = 'http://theuniverse.org/id'">
						<xsl:text>&#xa;*****id_found*****</xsl:text>	
					</xsl:if>
				</xsl:if>
				<xsl:if test="rs:variable = 's'">
					<xsl:text>&#xa;-----subject_found-----</xsl:text>
					<xsl:value-of select = "rs:value/@rdf:resource"/>	
				</xsl:if>
			</xsl:for-each>
			<xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="rs:solution">
		<xsl:text>&#xa;-----inside_rs_solution-----</xsl:text>			
	</xsl:template>
	
	<xsl:template match="rs:resultVariable">
		<xsl:text>-----inside_rs_resultVariable-----</xsl:text>
	</xsl:template>
	
	<xsl:template match="rs:size">
		<xsl:text>-----inside_rs_size-----</xsl:text>
	</xsl:template>
	
	
	
</xsl:stylesheet>