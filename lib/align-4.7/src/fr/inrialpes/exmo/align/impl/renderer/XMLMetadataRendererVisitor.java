/*
 * $Id: XMLMetadataRendererVisitor.java 1988 2014-11-14 22:20:28Z euzenat $
 *
 * Copyright (C) INRIA, 2007, 2009-2010, 2012, 2014
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package fr.inrialpes.exmo.align.impl.renderer; 

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;
import java.io.PrintWriter;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owl.align.Relation;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentVisitor;

import fr.inrialpes.exmo.align.impl.Annotations;
import fr.inrialpes.exmo.align.impl.Namespace;
import fr.inrialpes.exmo.align.impl.BasicAlignment;

/**
 * Renders an alignment in its RDF format
 *
 * @author J�r�me Euzenat
 * @version $Id: XMLMetadataRendererVisitor.java 1988 2014-11-14 22:20:28Z euzenat $ 
 */

public class XMLMetadataRendererVisitor extends GenericReflectiveVisitor implements AlignmentVisitor {
    
    PrintWriter writer = null;
    Alignment alignment = null;
    boolean embedded = false; // if the output is XML embeded in a structure
    Hashtable<String,String> nslist = null;
    boolean newstyle = false;

    public XMLMetadataRendererVisitor( PrintWriter writer ){
	this.writer = writer;
    }

    public void init( Properties p ) {
	if ( p.getProperty( "embedded" ) != null 
	     && !p.getProperty( "embedded" ).equals("") ) embedded = true;
    };

    public void visit( Alignment align ) throws AlignmentException {
	if ( subsumedInvocableMethod( this, align, Alignment.class ) ) return;
	// default behaviour
	String extensionString = "";
	alignment = align;
	nslist = new Hashtable<String,String>();
        nslist.put(Namespace.ALIGNMENT.prefix, Namespace.ALIGNMENT.shortCut);
        nslist.put(Namespace.EXT.prefix, Namespace.EXT.shortCut);
        nslist.put(Namespace.RDF.prefix, Namespace.RDF.shortCut);
        nslist.put(Namespace.XSD.prefix, Namespace.XSD.shortCut);
	// Get the keys of the parameter
	int gen = 0;
	for ( String[] ext : align.getExtensions() ){
	    String prefix = ext[0];
	    String name = ext[1];
	    String tag = nslist.get(prefix);
	    if ( tag == null ) {
		tag = "ns"+gen++;
		nslist.put( prefix, tag );
	    }
	    if ( tag.equals("align") ) { tag = name; }
	    else { tag += ":"+name; }
	    extensionString += "  <"+tag+">"+ext[2]+"</"+tag+">\n";
	}
	if ( embedded == false ) {
	    writer.print("<?xml version='1.0' encoding='utf-8");
	    writer.print("' standalone='no'?>\n");
	}
	writer.print("<rdf:RDF xmlns='"+Namespace.ALIGNMENT.uri+"'");
	for ( Entry<String,String> e : nslist.entrySet() ) {
	    writer.print("\n         xmlns:"+e.getValue()+"='"+e.getKey()+"'");
	}
	if ( align instanceof BasicAlignment ) {
	    for ( Entry<Object,Object> e : ((BasicAlignment)align).getXNamespaces().entrySet() ) {
		String label = (String)e.getKey();
		if ( !label.equals("rdf") && !label.equals("xsd")
		     && !label.equals("<default>") )
		    writer.print("\n         xmlns:"+label+"='"+e.getValue()+"'");
	    }
	}
	writer.print(">\n");
	writer.print("<Alignment");
	String idext = align.getExtension( Namespace.ALIGNMENT.uri, Annotations.ID );
	if ( idext != null ) {
	    writer.print(" rdf:about=\""+idext+"\"");
	}
	writer.print(">\n  <xml>yes</xml>\n");
	writer.print("  <level>");
	writer.print( align.getLevel() );
	writer.print("</level>\n  <type>");
	writer.print( align.getType() );
	writer.print("</type>\n");
	// Get the keys of the parameter
	if ( align.getFile1() != null )
	    writer.print("  <onto1>"+align.getFile1().toString()+"</onto1>\n");
	if ( align.getFile2() != null )
	    writer.print("  <onto2>"+align.getFile2().toString()+"</onto2>\n");
	writer.print("  <uri1>");
	writer.print( align.getOntology1URI().toString() );
	writer.print("</uri1>\n");
	writer.print("  <uri2>");
	writer.print( align.getOntology2URI().toString() );
	writer.print("</uri2>\n");
	writer.print(extensionString);
	if ( newstyle ){
	    writer.print("  <onto1>\n    <Ontology");
	    if ( align.getOntology1URI() != null ) {
		writer.print(" rdf:about=\""+align.getOntology1URI()+"\"");
	    }
	    writer.print(">\n      <location>"+align.getFile1()+"</location>");
	    if ( align instanceof BasicAlignment && ((BasicAlignment)align).getOntologyObject1().getFormalism() != null ) {
		writer.print("\n      <formalism>\n        <Formalism align:name=\""+((BasicAlignment)align).getOntologyObject1().getFormalism()+"\" align:uri=\""+((BasicAlignment)align).getOntologyObject1().getFormURI()+"\"/>\n      </formalism>");
	    }
	    writer.print("\n    </Ontology>\n  </onto1>\n");
	    writer.print("  <onto2>\n    <Ontology");
	    if ( align.getOntology2URI() != null ) {
		writer.print(" rdf:about=\""+align.getOntology2URI()+"\"");
	    }
	    writer.print(">\n      <location>"+align.getFile2()+"</location>");
	    if ( align instanceof BasicAlignment && ((BasicAlignment)align).getOntologyObject2().getFormalism() != null ) {
		writer.print("\n      <formalism>\n        <Formalism align:name=\""+((BasicAlignment)align).getOntologyObject2().getFormalism()+"\" align:uri=\""+((BasicAlignment)align).getOntologyObject2().getFormURI()+"\"/>\n      </formalism>");
	    }
	    writer.print("\n    </Ontology>\n  </onto2>\n");
	}
	writer.print("</Alignment>\n");
	writer.print("</rdf:RDF>\n");
    }

    public void visit( Cell c ) throws AlignmentException {
	if ( subsumedInvocableMethod( this, c, Cell.class ) ) return;
	// default behaviour
    };

    public void visit( Relation r ) throws AlignmentException {
	if ( subsumedInvocableMethod( this, r, Relation.class ) ) return;
	// default behaviour
    };
    
}
