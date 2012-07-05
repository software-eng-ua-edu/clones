/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.eclipse.jgit.diff.DiffEntry;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class GitCommit extends Commit
{
    public GitCommit(List<DiffEntry> diffs, String message) {
        super(processDiff(diffs), message);
    }

    protected static Set<FileChange> processDiff(List<DiffEntry> diffs) {
        Set<FileChange> changes = new HashSet<FileChange>();
        for(DiffEntry de : diffs) {
            changes.add(new GitFileChange(de));
        }
        return changes;
    }

}