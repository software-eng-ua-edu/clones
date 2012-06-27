/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonerank.importing;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;

import edu.ua.eng.software.clonerank.CodeFragment;


/**
 * Usage:
 * ------
 * File cloneFile = new File(cloneResultXML);
 * SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 * NiCadHandler niCadHandler = new NiCadHandler();
 * saxParser.parse(cloneFile, niCadHandler);
 * ArrayList<ArrayList<CodeFragment>> clones = niCadHandler.getClones();
 * ------
 *
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class NiCadImporter extends CloneImporter
{

    public ArrayList<ArrayList<CodeFragment>> getClones(File cloneFile) {
        SAXHandler handler = new SAXHandler();
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(cloneFile, handler);
        } catch (SAXException e) {
            //FIXME: Do something?
        } catch (ParserConfigurationException e) {
            //FIXME: Do something?
        } catch (IOException e) {
            //FIXME: Do something?
        }
        return handler.getClones(); 
    }

    private class SAXHandler extends DefaultHandler
    {
        public void startElement (String uri, String localName, String qname, Attributes attributes) throws SAXException {
            if(qname.equals("clone")) {
                cloneClass = new ArrayList<CodeFragment>();
                clones.add(cloneClass);
            } else if(qname.equals("source")) {
                CodeFragment fragment = new CodeFragment(
                    attributes.getValue("file"),
                    Integer.parseInt(attributes.getValue("startline")),
                    Integer.parseInt(attributes.getValue("endline"))
                );
                cloneClass.add(fragment);
            }
        }
        
        public ArrayList<ArrayList<CodeFragment>> getClones() { return clones; }
        
        private ArrayList<ArrayList<CodeFragment>> clones = new ArrayList<ArrayList<CodeFragment>>();
        private ArrayList<CodeFragment> cloneClass;
    }

}