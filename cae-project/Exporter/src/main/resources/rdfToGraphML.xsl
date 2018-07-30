<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:rs="http://www.w3.org/2001/sw/DataAccess/tests/result-set#"
	xmlns="http://graphml.graphdrawing.org/xmlns"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns 
        								 http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd"
	xmlns:y="http://www.yworks.com/xml/graphml">

	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<!-- variable to control, if attributes shall be displayed in graph or not -->
	
	<xsl:variable name="detailed_view">
		<!-- enter some text here to enable details, leave empty for reduced view -->
		hallo
	</xsl:variable>

	<xsl:template match="/">
		<xsl:text>&#xa;</xsl:text>
		<graphml>

			<!-- create attributes for nodes -->
			<key attr.name="color" attr.type="string" for="node"
				id="node_color">
				<default xml:space="preserve"><![CDATA[yellow]]></default>
			</key>
			<key attr.name="label" attr.type="string" for="node"
				id="node_label" />
			<key attr.name="url" attr.type="string" for="node" id="node_url" />
			<key attr.name="description" attr.type="string" for="node"
				id="node_description" />
			<key for="node" id="node_graphics" yfiles.type="nodegraphics" />

			<!-- create attributes for edges -->
			<key id="edge_label" for="edge" attr.name="label"
				attr.type="string" />

			<!-- create graph -->
			<xsl:apply-templates />


		</graphml>
	</xsl:template>

	<xsl:template match="rs:ResultSet">
		<graph id="G" edgedefault="directed">
			<xsl:for-each select="rs:solution">
				<xsl:call-template name="create_main_nodes" />
				<xsl:call-template name="create_main_edges" />
				<xsl:call-template
					name="create_pipes_n_connectors" />
				<xsl:call-template
					name="connect_pipes_n_connectors" />
			</xsl:for-each>
		</graph>
	</xsl:template>

	<!-- create one node for every label -->
	<xsl:template name="create_main_nodes">
		<!-- search currently selected solution for labels -->
		<xsl:for-each select="rs:binding">
			<!-- check if solution contains a label -->
			<xsl:if
				test="rs:variable = 'p' and (rs:value/@rdf:resource = 'http://www.w3.org/2000/01/rdf-schema#label')">


				<!-- load subject from same solution as node id -->
				<xsl:variable name="node_id">
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 's'">
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- load object from same solution as node label -->
				<xsl:variable name="node_label">
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 'o'">
							<xsl:value-of select="rs:value" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- x-coordinate -->
				<xsl:variable name="posX">
					<xsl:text>0</xsl:text>
				</xsl:variable>

				<!-- y-coordinate -->
				<xsl:variable name="posY">
					<xsl:text>50</xsl:text>
				</xsl:variable>

				<!-- find out if found label belongs to a connection object; these need 
					to be treated separately -->
				<xsl:variable name="connection_filter">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'o' 
									and 
									(rs:value = 'I1')">
									<xsl:text>Connection_found</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- find out type of main node, that is to be created -->
				<!-- tweaked to finding comment 'Kreiselpumpe' -->
				<xsl:variable name="node_type">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'p' and rs:value/@rdf:resource = 'http://www.w3.org/2000/01/rdf-schema#comment'">
									<xsl:for-each select="../rs:binding">
										<xsl:if test="rs:variable = 'o'">
											<xsl:value-of select='rs:value' />
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>


				<xsl:variable name="plant-ID">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'p' and (rs:value/@rdf:resource = 'http://eatld.et.tu-dresden.de/mso/plantID')">
									<xsl:for-each select="../rs:binding">
										<xsl:if test="rs:variable = 'o'">
											<xsl:value-of select="rs:value" />
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<xsl:if test="$connection_filter = ''">
					<!-- create node -->
					<node id="{$node_id}">
						<graph id="{$node_id}:" edgedefault="directed">
							<node id="{$node_id}::n0">
								<data key="node_label">
									<xsl:value-of select="$node_label" />
								</data>
								<data key="node_graphics">
									<y:ShapeNode>
										<y:Geometry height="40.0" width="40.0" x="{$posX}"
											y="{$posY}" />
										<y:Fill color="#6ac9e1" transparent="false" />
										<y:BorderStyle color="#000000" raised="false"
											type="line" width="1.0" />
										<y:NodeLabel>
											<xsl:value-of select="$node_label" />
										</y:NodeLabel>
										<y:Shape type="triangle" />
									</y:ShapeNode>
								</data>
							</node>
							<xsl:call-template name="create_sub_graph">
								<xsl:with-param name="main_node_id"
									select="$node_id" />
							</xsl:call-template>

							<!-- create additional connection nodes for certain elements -->
							<xsl:if test="$node_type = 'Kreiselpumpe'">
								<node id="{$plant-ID}:I1">
									<data key="node_label">
										I1
									</data>
									<data key="node_graphics">
										<y:ShapeNode>
											<y:Geometry height="10.0" width="10.0" x="{$posX}"
												y="10" />
											<y:Fill color="#000000" transparent="false" />
											<y:BorderStyle color="#000000" raised="false"
												type="line" width="1.0" />
											<y:NodeLabel modelName="sides" modelPosition="e">
												I1
											</y:NodeLabel>
											<y:Shape type="circle" />
										</y:ShapeNode>
									</data>
								</node>

								<node id="{$plant-ID}:O1">
									<data key="node_label">
										O1
									</data>
									<data key="node_graphics">
										<y:ShapeNode>
											<y:Geometry height="10.0" width="10.0" x="{$posX}"
												y="10" />
											<y:Fill color="#000000" transparent="false" />
											<y:BorderStyle color="#000000" raised="false"
												type="line" width="1.0" />
											<y:NodeLabel modelName="sides" modelPosition="e">
												O1
											</y:NodeLabel>
											<y:Shape type="circle" />
										</y:ShapeNode>
									</data>
								</node>

								<edge id="{$node_id}_to_{$plant-ID}:I1"
									source="{$node_id}::n0" target="{$plant-ID}:I1"></edge>

								<edge id="{$node_id}_to_{$plant-ID}:O1"
									source="{$node_id}::n0" target="{$plant-ID}:O1"></edge>
							</xsl:if>

						</graph>
					</node>
				</xsl:if>


			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- create nodes and edges, that are only connected to currently selected 
		main node -->
	<xsl:template name="create_sub_graph">

		<xsl:param name="main_node_id" />
		<xsl:for-each select="../../rs:solution">
			<!-- check if currently selected solution belongs to this subgraph -->
			<xsl:for-each select="rs:binding">
				<xsl:if
					test="rs:variable = 's' and rs:value/@rdf:resource=$main_node_id">


					<!-- labels themselves also have no nodeID; need to be filtered -->
					<xsl:for-each select="../rs:binding">
						<xsl:if
							test="rs:variable = 'p' and not(rs:value/@rdf:resource = 'http://www.w3.org/2000/01/rdf-schema#label')
									and
										not((rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/has'
										or
										rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/hasProcessCell'
										or
										rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/hasEquipment'
										or
										rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/hasUnit'
										or 
										rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mo/hmi#isConnectedTo'
										or
										rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/isConnectedTo'))">


							<!-- load object from currently selected solution -->
							<xsl:variable name="node_id">
								<xsl:for-each select="../rs:binding">
									<xsl:if test="rs:variable = 'o'">
										<!-- check if value or attribute -->
										<xsl:choose>
											<xsl:when test="rs:value/@rdf:resource">
												<xsl:value-of select="rs:value/@rdf:resource" />
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="rs:value" />
											</xsl:otherwise>
										</xsl:choose>
									</xsl:if>
								</xsl:for-each>
							</xsl:variable>

							<!-- in case information is not provided, need another label -->
							<xsl:variable name="node_id_empty">
								<xsl:for-each select="../rs:binding">
									<xsl:if test="rs:variable = 's'">
										<xsl:value-of select="rs:value/@rdf:nodeID" />
									</xsl:if>
								</xsl:for-each>
								<xsl:text>_</xsl:text>
								<xsl:for-each select="../rs:binding">
									<xsl:if test="rs:variable = 'p'">
										<xsl:value-of select="rs:value/@rdf:resource" />
									</xsl:if>
								</xsl:for-each>
							</xsl:variable>

							<!-- extract from triple, which kind of node needs to be created -->
							<xsl:variable name='node_type'>
								<!-- extract label from triple -->
								<xsl:value-of select="rs:value/@rdf:resource" />
							</xsl:variable>

							<!-- x-coordinate -->
							<xsl:variable name="posX">
								<xsl:text>50</xsl:text>
							</xsl:variable>

							<!-- y-coordinate -->
							<xsl:variable name="posY">
								<xsl:text>100</xsl:text>
							</xsl:variable>

							<xsl:if test="not($detailed_view='')">
								<!-- create node, unless it's plantID -->
								<!-- filter connection objects, they need to be treated separately -->
								<xsl:if
									test="not($node_type='http://eatld.et.tu-dresden.de/mso/plantID')">
									<xsl:choose>
										<xsl:when test="not($node_id='')">
											<!-- create node -->
											<node id="{$main_node_id}::{$node_id}">
												<data key="node_label">
													<xsl:value-of select="$node_id" />
												</data>
												<data key="node_graphics">
													<y:ShapeNode>
														<y:Geometry height="20.0" width="20.0"
															x="{$posX}" y="{$posY}" />
														<y:Fill color="#04900E" transparent="false" />
														<y:BorderStyle color="#000000"
															raised="false" type="line" width="1.0" />
														<y:NodeLabel>
															<xsl:value-of select="$node_id" />
														</y:NodeLabel>
														<y:Shape type="rectangle" />
													</y:ShapeNode>
												</data>
											</node>
										</xsl:when>
										<!-- no information found in triple -->
										<xsl:otherwise>
											<!-- create node -->
											<node id="{$main_node_id}::{$node_id_empty}">
												<data key="node_label">
													<xsl:text>EMPTY</xsl:text>
												</data>
												<data key="node_graphics">
													<y:ShapeNode>
														<y:Geometry height="20.0" width="20.0" x="0"
															y="0" />
														<y:Fill color="#FFCC00" transparent="false" />
														<y:BorderStyle color="#000000"
															raised="false" type="line" width="1.0" />
														<y:NodeLabel>
															<xsl:text>EMPTY</xsl:text>
														</y:NodeLabel>
														<y:Shape type="rectangle" />
													</y:ShapeNode>
												</data>
											</node>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</xsl:if>

							<!-- create plantID nodes -->
							<xsl:if
								test="($node_type='http://eatld.et.tu-dresden.de/mso/plantID')">
								<!-- create node -->
								<node id="{$node_id}">
									<data key="node_label">
										<xsl:value-of select="$node_id" />
									</data>
									<data key="node_graphics">
										<y:ShapeNode>
											<y:Geometry height="10.0" width="10.0" x="{$posX}"
												y="{$posY}" />
											<y:Fill color="#000000" transparent="false" />
											<y:BorderStyle color="#000000" raised="false"
												type="line" width="1.0" />
											<y:NodeLabel modelName="sides" modelPosition="e">
												<xsl:value-of select="$node_id" />
											</y:NodeLabel>
											<y:Shape type="circle" />
										</y:ShapeNode>
									</data>
								</node>
							</xsl:if>


							<!-- edge source is always main node -->
							<xsl:variable name='edge_source_id'>
								<xsl:value-of select="$main_node_id" />
								<xsl:text>::n0</xsl:text>
							</xsl:variable>

							<!-- identify label from triple predicate, which is currently selected -->
							<xsl:variable name='edge_label'>
								<!-- extract label from triple -->
								<xsl:value-of select="rs:value/@rdf:resource" />
							</xsl:variable>

							<!-- identify target from triple object; might be empty though -->
							<xsl:variable name='edge_target_id'>
								<xsl:value-of select="$main_node_id" />
								<xsl:text>::</xsl:text>
								<xsl:choose>
									<xsl:when test="not($node_id='')">
										<xsl:value-of select="$node_id" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$node_id_empty" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>

							<!-- create unique ID -->
							<xsl:variable name='edge_id'>
								<xsl:value-of select="$main_node_id" />
								<xsl:text>::to_</xsl:text>
								<xsl:value-of select="$edge_target_id" />
							</xsl:variable>

							<xsl:if test="not($detailed_view='')">
								<!-- create edge -->
								<!-- filter connection objects, they need to be treated separately -->
								<xsl:if
									test="not($node_type='http://eatld.et.tu-dresden.de/mso/plantID')">
									<edge id="{$edge_id}" source="{$edge_source_id}"
										target="{$edge_target_id}">
										<data key="edge_label">
											<xsl:value-of select="$edge_label" />
										</data>
									</edge>
								</xsl:if>
							</xsl:if>


							<!-- create edge for plantIDs -->
							<xsl:if
								test="($node_type='http://eatld.et.tu-dresden.de/mso/plantID')">
								<edge id="{$edge_id}" source="{$edge_source_id}"
									target="{$node_id}">
									<data key="edge_label">
										<xsl:value-of select="$edge_label" />
									</data>
								</edge>
							</xsl:if>

						</xsl:if>
					</xsl:for-each>

				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>



	<!-- create edges, that only connect main nodes -->
	<xsl:template name="create_main_edges">
		<!-- check if predicate is 'has' -->
		<xsl:for-each select="rs:binding">
			<xsl:if
				test="rs:variable = 'p' and (rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/has'
						or
						rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/hasProcessCell'
						or
						rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/hasEquipment'
						or
						rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/hasUnit'
						or rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mo/hmi#isConnectedTo'
						)">
				<!-- Pipe: -->
				<!-- or rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mo/hmi#isConnectedTo' -->


				<!-- identify edge source from triple subject -->
				<xsl:variable name='edge_source_id'>
					<!-- pick binding "subject" -->
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 's'">
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- identify label from triple predicate -->
				<xsl:variable name='edge_label'>
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 'p'">
							<!-- extract label from triple -->
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- identify target from triple object -->
				<xsl:variable name='edge_target_id'>
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 'o'">
							<!-- load object -->
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- create unique ID -->
				<!-- mind the possibility of not finding a matching ID -->
				<xsl:variable name='edge_id'>
					<xsl:value-of select="$edge_source_id" />
					<xsl:text>_to_</xsl:text>
					<xsl:value-of select="$edge_target_id" />
				</xsl:variable>


				<!-- create edge -->
				<!-- filter pipes that point at "themselves" -->
				<xsl:if test="not($edge_target_id=$edge_source_id)">
					<edge id="{$edge_id}" source="{$edge_source_id}::n0"
						target="{$edge_target_id}::n0">
						<data key="edge_label">
							<xsl:value-of select="$edge_label" />
						</data>
					</edge>
				</xsl:if>

			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- create pipes and connectors from the identifier 'I1' -->
	<xsl:template name="create_pipes_n_connectors">

		<xsl:for-each select="rs:binding">
			<!-- check if solution contains a connection object -->
			<xsl:if test="rs:variable = 'o' and (rs:value = 'I1')">


				<!-- load subject from same solution as node id -->
				<xsl:variable name="node_id">
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 's'">
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>



				<!-- x-coordinate -->
				<xsl:variable name="posX">
					<xsl:text>0</xsl:text>
				</xsl:variable>

				<!-- y-coordinate -->
				<xsl:variable name="posY">
					<xsl:text>0</xsl:text>
				</xsl:variable>



				<!-- find out if found label belongs to a connection object; these need 
					to be treated separately -->
				<xsl:variable name="filter_pipe">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'o' 
										and rs:value/@rdf:resource = 'http://eatld.et.tu-dresden.de/mso/Pipe'">
									<xsl:text>Pipe</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<xsl:variable name="filter_conduit">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'o' and (rs:value/@rdf:resource ='http://eatld.et.tu-dresden.de/mso/Conduit')">
									<xsl:text>Conduit</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<xsl:variable name="filter_connector">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'o' and (rs:value/@rdf:resource = 'http://eatld.et.tu-dresden.de/mso/ConnectionObject')">
									<xsl:text>Connector</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- load object from same solution as node label -->
				<xsl:variable name="node_label">
					<xsl:choose>
						<xsl:when test="not($filter_pipe='')">
							Pipe-segment
						</xsl:when>
						<xsl:when test="not($filter_conduit='')">
							Pipe
						</xsl:when>
						<xsl:otherwise>
							Connector
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>

				<xsl:variable name="plant-ID">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'p' and (rs:value/@rdf:resource = 'http://eatld.et.tu-dresden.de/mso/plantID')">
									<xsl:for-each select="../rs:binding">
										<xsl:if test="rs:variable = 'o'">
											<xsl:value-of select="rs:value" />
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- connector 1 -->
				<xsl:variable name="C1">
					<xsl:text>O1</xsl:text>
				</xsl:variable>

				<!-- connector 2 -->
				<xsl:variable name="C2">
					<xsl:text>I1</xsl:text>
				</xsl:variable>

				<!-- connector 3 -->
				<xsl:variable name="C3">
					<xsl:text>001</xsl:text>
				</xsl:variable>





				<!-- create node with subnodes and subedges -->
				<node id="{$node_id}">
					<graph id="{$node_id}:" edgedefault="directed">
						<node id="{$node_id}::n0">
							<data key="node_label">
								<xsl:value-of select="$node_label" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="20.0" width="20.0" x="{$posX}"
										y="{$posY}" />
									<y:Fill color="#1323A9" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel>
										<xsl:value-of select="$node_label" />
									</y:NodeLabel>
									<y:Shape type="round" />
								</y:ShapeNode>
							</data>
						</node>

						<node id="{$plant-ID}:{$C1}">
							<data key="node_label">
								<xsl:value-of select="$C1" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="10.0" width="10.0" x="{$posX}"
										y="10" />
									<y:Fill color="#000000" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel modelName="sides" modelPosition="e">
										<xsl:value-of select="$C1" />
									</y:NodeLabel>
									<y:Shape type="circle" />
								</y:ShapeNode>
							</data>
						</node>

						<node id="{$plant-ID}:{$C2}">
							<data key="node_label">
								<xsl:value-of select="$C2" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="10.0" width="10.0" x="{$posX}"
										y="10" />
									<y:Fill color="#000000" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel modelName="sides" modelPosition="e">
										<xsl:value-of select="$C2" />
									</y:NodeLabel>
									<y:Shape type="circle" />
								</y:ShapeNode>
							</data>
						</node>

						<node id="{$plant-ID}:{$C3}">
							<data key="node_label">
								<xsl:value-of select="$C3" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="10.0" width="10.0" x="{$posX}"
										y="10" />
									<y:Fill color="#000000" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel modelName="sides" modelPosition="e">
										<xsl:value-of select="$C3" />
									</y:NodeLabel>
									<y:Shape type="circle" />
								</y:ShapeNode>
							</data>
						</node>

						<edge id="{$node_id}_to_{$plant-ID}:{$C1}"
							source="{$node_id}::n0" target="{$plant-ID}:{$C1}"></edge>

						<edge id="{$node_id}_to_{$plant-ID}:{$C2}"
							source="{$node_id}::n0" target="{$plant-ID}:{$C2}"></edge>

						<edge id="{$node_id}_to_{$plant-ID}:{$C3}"
							source="{$node_id}::n0" target="{$plant-ID}:{$C3}"></edge>

					</graph>
				</node>



			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- connect all pipes and connectors -->
	<xsl:template name="connect_pipes_n_connectors">
		<xsl:for-each select="rs:binding">
			<xsl:if
				test="rs:variable='p' and (rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mso/isConnectedTo'
				or rs:value/@rdf:resource='http://eatld.et.tu-dresden.de/mo/hmi#isConnectedTo')">

				<!-- identify edge source from triple subject -->
				<xsl:variable name='edge_source_id'>
					<!-- pick binding "subject" -->
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 's'">
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- identify label from triple predicate -->
				<xsl:variable name='edge_label'>
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 'p'">
							<!-- extract label from triple -->
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- identify target from triple object -->
				<xsl:variable name='edge_target'>
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 'o'">
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- identify target from triple object -->
				<xsl:variable name='edge_target_id'>
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 'o'">

							<!-- load object -->
							<xsl:choose>
								<xsl:when test="not($edge_target='')">
									<xsl:value-of select="$edge_target" />
									<xsl:text>::n0</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="rs:value" />
								</xsl:otherwise>
							</xsl:choose>

						</xsl:if>
					</xsl:for-each>
				</xsl:variable>


				<!-- create unique ID -->
				<!-- mind the possibility of not finding a matching ID -->
				<xsl:variable name='edge_id'>
					<xsl:value-of select="$edge_source_id" />
					<xsl:text>_to_</xsl:text>
					<xsl:value-of select="$edge_target_id" />
				</xsl:variable>


				<!-- create edge -->
				<!-- filter connections without target -->
				<xsl:if test="not($edge_target_id='')">
					<!-- filter loop connections -->
					<xsl:if test="not($edge_target=$edge_source_id)">
						<!-- filter one sensor, because the data is not provided -->
						<xsl:if test="not($edge_target_id='=0.H1.T1.L001.LIC:I')">
							<edge id="{$edge_id}" source="{$edge_source_id}::n0"
								target="{$edge_target_id}">
								<data key="edge_label">
									<xsl:value-of select="$edge_label" />
								</data>
							</edge>
						</xsl:if>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>



	<xsl:template name="create_pipes">

		<xsl:for-each select="rs:binding">
			<!-- check if solution contains a pipe -->
			<xsl:if
				test="rs:variable = 'o' and (rs:value/@rdf:resource = 'http://eatld.et.tu-dresden.de/mso/Pipe')">


				<!-- load subject from same solution as node id -->
				<xsl:variable name="node_id">
					<xsl:for-each select="../rs:binding">
						<xsl:if test="rs:variable = 's'">
							<xsl:value-of select="rs:value/@rdf:resource" />
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>



				<!-- x-coordinate -->
				<xsl:variable name="posX">
					<xsl:text>0</xsl:text>
				</xsl:variable>

				<!-- y-coordinate -->
				<xsl:variable name="posY">
					<xsl:text>50</xsl:text>
				</xsl:variable>


				<!-- load object from same solution as node label -->
				<xsl:variable name="node_label">
					<xsl:text>
						Pipe-segment
					</xsl:text>
				</xsl:variable>

				<xsl:variable name="plant-ID">
					<xsl:for-each select="../../rs:solution/rs:binding">
						<xsl:if
							test="rs:variable = 's' and rs:value/@rdf:resource=$node_id">
							<xsl:for-each select="../rs:binding">
								<xsl:if
									test="rs:variable = 'p' and (rs:value/@rdf:resource = 'http://eatld.et.tu-dresden.de/mso/plantID')">
									<xsl:for-each select="../rs:binding">
										<xsl:if test="rs:variable = 'o'">
											<xsl:value-of select="rs:value" />
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>

				<!-- connector 1 -->
				<xsl:variable name="C1">
					<xsl:text>O1</xsl:text>
				</xsl:variable>

				<!-- connector 2 -->
				<xsl:variable name="C2">
					<xsl:text>I1</xsl:text>
				</xsl:variable>


				<!-- create node -->
				<node id="{$node_id}">
					<graph id="{$node_id}:" edgedefault="directed">
						<node id="{$node_id}::n0">
							<data key="node_label">
								<xsl:value-of select="$node_label" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="20.0" width="20.0" x="{$posX}"
										y="{$posY}" />
									<y:Fill color="#1323A9" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel>
										<xsl:value-of select="$node_label" />
									</y:NodeLabel>
									<y:Shape type="triangle" />
								</y:ShapeNode>
							</data>
						</node>

						<node id="{$plant-ID}:{$C1}">
							<data key="node_label">
								<xsl:value-of select="$C1" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="20.0" width="20.0" x="{$posX}"
										y="{$posY}" />
									<y:Fill color="#1323A9" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel>
										<xsl:value-of select="$C1" />
									</y:NodeLabel>
									<y:Shape type="rectangle" />
								</y:ShapeNode>
							</data>
						</node>

						<node id="{$plant-ID}:{$C2}">
							<data key="node_label">
								<xsl:value-of select="$C2" />
							</data>
							<data key="node_graphics">
								<y:ShapeNode>
									<y:Geometry height="20.0" width="20.0" x="{$posX}"
										y="{$posY}" />
									<y:Fill color="#1323A9" transparent="false" />
									<y:BorderStyle color="#000000" raised="false"
										type="line" width="1.0" />
									<y:NodeLabel>
										<xsl:value-of select="$C2" />
									</y:NodeLabel>
									<y:Shape type="rectangle" />
								</y:ShapeNode>
							</data>
						</node>

						<edge source="{$node_id}::n0" target="{$plant-ID}:{$C1}"></edge>

						<edge source="{$node_id}::n0" target="{$plant-ID}:{$C2}"></edge>

					</graph>
				</node>


			</xsl:if>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>