/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public interface FileChange 
{
    public ChangeType getChangeType();

    public String getOldPath();

    public String getNewPath();

    public enum ChangeType {
        ADD, MODIFY, REMOVE, COPY, RENAME
    }
}