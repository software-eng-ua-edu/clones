/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonerank;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class FilepathDistanceMetric extends DistanceMetric
{
    public int getDistance(String path1, String path2) {
        return super.pathDistance(super.breakPath(path1), super.breakPath(path2));
    }
}