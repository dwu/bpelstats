<?xml version="1.0" encoding="UTF-8"?>
<!--
*****************************************************************************
*   This file contains the XSLT code snippets for the custom mappings
*   defined using the IBM XML Mapping Editor V1.0.110
*
*   Mapping file:		FullTestXMLMap.map
*   Map declaration(s):	FullTestXMLMap
*   Input file(s):		../FullTestLib/Person.xsd
*   Output file(s):		../FullTestLib/Stuff.xsd
*
*   Custom code snippets are wrapped in top-level <xsl:template> 
*   elements. These <xsl:template> elements, in turn, are used to
*   interface with the custom mappings in the XML Mapping editor.
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
     xmlns:io="http://FullTestLib"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:xsd4xsd="http://www.w3.org/2001/XMLSchema"
     xmlns:CustomJavaMapping="xalan://test.CustomJavaMapping"
     xmlns:map="http://FullTestProc/FullTestXMLMap"
     exclude-result-prefixes="xalan str set math CustomJavaMapping map exsl date"
     version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>
  <xsl:strip-space elements="*"/>

  <!-- The rule represents a custom mapping: "geburtsdatum" to "field3". -->
  <xsl:template name="GeburtsdatumToField3">
    <xsl:param name="geburtsdatum1"/>
    <xsl:value-of select="$geburtsdatum1"/>
  </xsl:template>
</xsl:stylesheet>