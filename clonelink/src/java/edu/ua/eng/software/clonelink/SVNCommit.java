/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class SVNCommit extends Commit
{
    public SVNCommit(SVNLogEntry logEntry) {
        setMessage(pullMessage(logEntry));
        setFileChanges(process(logEntry));
        setBugFix(checkBugFix());
    }

    protected Set<FileChange> process(SVNLogEntry logEntry) {
        Map<String, SVNLogEntryPath> map = logEntry.getChangedPaths();
        Set<FileChange> changes = new HashSet<FileChange>();
        for(SVNLogEntryPath path : map.values()) {
            if(path.getPath().startsWith("/trunk/")) {
                changes.add(new SVNFileChange(this, path));
            }
        }
        return changes;
    }

    protected String pullMessage(SVNLogEntry logEntry) {
        String message = logEntry.getMessage();
        return (message != null) ? message : "";
    }
}