/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonerank;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

/**
 * Allows for filesystem distance based on package naming conventions.
 * 
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class TLPDistanceMetric extends DistanceMetric
{
    public TLPDistanceMetric() {
        for(String tlp: defaultTlps) tlpSet.add(tlp);
    }

    public TLPDistanceMetric(String tlp) {
        this();
        addTLP(tlp);
    }

    public void addTLP(String tlp) {
        tlpSet.add(tlp);
    }

    public int getDistance(String path1, String path2) {
        List<String> pathList1 = breakPath(path1);
        List<String> pathList2 = breakPath(path2);
        int index1 = findIndex(pathList1);
        int index2 = findIndex(pathList2);
        if(index1 != -1 && index2 != -1) {
            pathList1 = pathList1.subList(index1, pathList1.size());
            pathList2 = pathList2.subList(index2, pathList2.size());
            return pathDistance(pathList1, pathList2);
        } else {
            //TODO: no top level package?
        }
        return -1;
    }

    protected int findIndex(List<String> path) {
        ListIterator<String> it = path.listIterator();
        while(it.hasNext()) {
            String segment = it.next();
            if(tlpSet.contains(segment)) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    final Set<String> tlpSet = new HashSet<String>();
    final String[] defaultTlps = {"edu", "com", "net", "org"};
}