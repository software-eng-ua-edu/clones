/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class SVNFileChange implements FileChange
{
    public SVNFileChange(SVNCommit commit, SVNLogEntryPath path) {
        this.commit = commit;
        this.changeType = mapChangeType(path.getType());
        this.oldPath = path.getPath().substring(7);
        this.newPath = path.getPath().substring(7);
        if(path.getCopyPath() != null) {
            this.oldPath = path.getCopyPath();
            if(this.changeType == ChangeType.ADD) {
                this.changeType = ChangeType.COPY;
            }
        }
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public String getOldPath() {
        return oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public Commit getCommit() {
        return commit;
    }

    protected static ChangeType mapChangeType(char ct) {
        if(ct == SVNLogEntryPath.TYPE_ADDED) {
            return ChangeType.ADD;
        } else if (ct == SVNLogEntryPath.TYPE_DELETED) {
            return ChangeType.REMOVE;
        } else if (ct == SVNLogEntryPath.TYPE_MODIFIED) {
            return ChangeType.MODIFY;
        } else if (ct == SVNLogEntryPath.TYPE_REPLACED) {
            return ChangeType.RENAME;
        }
        //Rushed, sorry
        return null;
    }

    private ChangeType changeType;
    private String oldPath;
    private String newPath;
    private SVNCommit commit;
}