<?xml version="1.0" encoding="UTF-8"?>
<!-- @generated mapFile="xslt/XSLTransformation1_req_1.map" md5sum="5b5ebe59ac8b9d734ebe6d96a08c6c53" version="7.0.500" -->
<!--
*****************************************************************************
*   This file has been generated by the IBM XML Mapping Editor V7.0.500
*
*   Mapping file:		XSLTransformation1_req_1.map
*   Map declaration(s):	XSLTransformation1_req_1
*   Input file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252FFullTestProc%252FFullTestInterface%257Doperation1RequestMsg/xpath%3D%252Fbody/smo.xsd
*   Output file(s):		smo://smo/name%3Dwsdl-primary/message%3D%257Bhttp%253A%252F%252FFullTestProc%252FFullTestInterface%257Doperation1ResponseMsg/xpath%3D%252Fbody/smo.xsd
*
*   Note: Do not modify the contents of this file as it is overwritten
*         each time the mapping model is updated.
*****************************************************************************
-->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:str="http://exslt.org/strings"
    xmlns:set="http://exslt.org/sets"
    xmlns:math="http://exslt.org/math"
    xmlns:exsl="http://exslt.org/common"
    xmlns:date="http://exslt.org/dates-and-times"
    xmlns:io3="http://www.w3.org/2003/05/soap-envelope"
    xmlns:io2="http://FullTestLib"
    xmlns:io="http://FullTestProc/FullTestInterface"
    xmlns:io5="http://www.ibm.com/xmlns/prod/websphere/mq/sca/6.0.0"
    xmlns:io4="http://www.ibm.com/websphere/sibx/smo/v6.0.1"
    xmlns:io6="http://schemas.xmlsoap.org/ws/2004/08/addressing"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:io8="http://www.ibm.com/xmlns/prod/websphere/http/sca/6.1.0"
    xmlns:io7="wsdl.http://FullTestProc/FullTestInterface"
    xmlns:xsd4xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:io9="http://www.w3.org/2005/08/addressing"
    xmlns:map="http://FullTestProc/xslt/XSLTransformation1_req_1"
    xmlns:msl="http://www.ibm.com/xmlmap"
    exclude-result-prefixes="xalan str set msl math map exsl date"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no"/>

  <!-- root wrapper template  -->
  <xsl:template match="/">
    <xsl:choose>
      <xsl:when test="msl:datamap">
        <msl:datamap>
          <dataObject>
            <xsl:attribute name="xsi:type">
              <xsl:value-of select="'io7:operation1ResponseMsg'"/>
            </xsl:attribute>
            <xsl:call-template name="map:XSLTransformation1_req_12">
              <xsl:with-param name="body" select="msl:datamap/dataObject[1]"/>
            </xsl:call-template>
          </dataObject>
        </msl:datamap>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="body" mode="map:XSLTransformation1_req_1"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- This rule represents an element mapping: "body" to "body".  -->
  <xsl:template match="body"  mode="map:XSLTransformation1_req_1">
    <body>
      <xsl:attribute name="xsi:type">
        <xsl:value-of select="'io7:operation1ResponseMsg'"/>
      </xsl:attribute>
      <io:operation1Response>
        <output1>
          <!-- a simple data mapping: "io:operation1/input1/vorname"(string) to "field1"(string) -->
          <xsl:if test="io:operation1/input1/vorname">
            <field1>
              <xsl:value-of select="io:operation1/input1/vorname"/>
            </field1>
          </xsl:if>
          <!-- a simple data mapping: "io:operation1/input1/nachname"(string) to "field2"(string) -->
          <xsl:if test="io:operation1/input1/nachname">
            <field2>
              <xsl:value-of select="io:operation1/input1/nachname"/>
            </field2>
          </xsl:if>
        </output1>
      </io:operation1Response>
    </body>
  </xsl:template>

  <!-- This rule represents a type mapping: "body" to "body".  -->
  <xsl:template name="map:XSLTransformation1_req_12">
    <xsl:param name="body"/>
    <io:operation1Response>
      <output1>
        <!-- a simple data mapping: "$body/io:operation1/input1/vorname"(string) to "field1"(string) -->
        <xsl:if test="$body/io:operation1/input1/vorname">
          <field1>
            <xsl:value-of select="$body/io:operation1/input1/vorname"/>
          </field1>
        </xsl:if>
        <!-- a simple data mapping: "$body/io:operation1/input1/nachname"(string) to "field2"(string) -->
        <xsl:if test="$body/io:operation1/input1/nachname">
          <field2>
            <xsl:value-of select="$body/io:operation1/input1/nachname"/>
          </field2>
        </xsl:if>
      </output1>
    </io:operation1Response>
  </xsl:template>

  <!-- *****************    Utility Templates    ******************  -->
  <!-- copy the namespace declarations from the source to the target -->
  <xsl:template name="copyNamespaceDeclarations">
    <xsl:param name="root"/>
    <xsl:for-each select="$root/namespace::*">
      <xsl:copy/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
