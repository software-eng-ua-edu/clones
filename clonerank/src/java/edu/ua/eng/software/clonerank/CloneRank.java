/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonerank;

import edu.ua.eng.software.clonerank.importing.CloneImporter;
import edu.ua.eng.software.clonerank.importing.NiCadImporter;
import java.util.ArrayList;
import java.io.File;

/**
* @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
*/
public class CloneRank {

	public static void main (String [] args) throws Exception {
        String cloneResultXML = "test/rhino-1.6R5_functions-blind-clones-0.0.xml";
        File cloneFile = new File(cloneResultXML);
        CloneImporter importer = new NiCadImporter();
        ArrayList<ArrayList<CodeFragment>> clones = importer.getClones(cloneFile);
        test(new FilepathDistanceMetric(), clones);
        test(new TLPDistanceMetric(), clones);
	}

    private static void test(DistanceMetric distMetric, ArrayList<ArrayList<CodeFragment>> clones) {        
        int maxDist = -1;
        ArrayList<CodeFragment> high = null;
        for(ArrayList<CodeFragment> cloneClass : clones) {
            int dist = distMetric.getDistance(cloneClass.get(0).getFilename(), cloneClass.get(1).getFilename());
            if(dist > maxDist) {
                maxDist = dist;
                high = cloneClass;
            }
        }
        System.out.println(maxDist);
        for(CodeFragment clone : high) System.out.println(clone.getFilename());
    }
}
