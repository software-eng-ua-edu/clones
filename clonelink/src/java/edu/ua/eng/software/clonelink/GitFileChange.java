/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import org.eclipse.jgit.diff.DiffEntry;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class GitFileChange implements FileChange
{
    public GitFileChange(GitCommit commit, DiffEntry de) {
        this.commit = commit;
        this.changeType = mapChangeType(de.getChangeType());
        this.oldPath = de.getOldPath();
        this.newPath = de.getNewPath();
        if(this.changeType == changeType.REMOVE){
            this.newPath = this.oldPath;
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

    protected ChangeType mapChangeType(DiffEntry.ChangeType ct) {
        if(ct == DiffEntry.ChangeType.ADD) {
            return ChangeType.ADD;
        } else if (ct == DiffEntry.ChangeType.COPY) {
            return ChangeType.COPY;
        } else if (ct == DiffEntry.ChangeType.DELETE) {
            return ChangeType.REMOVE;
        } else if (ct == DiffEntry.ChangeType.MODIFY) {
            return ChangeType.MODIFY;
        } else if (ct == DiffEntry.ChangeType.RENAME) {
            return ChangeType.RENAME;
        }
        //Rushed, sorry
        return null;
    }

    private ChangeType changeType;
    private String oldPath;
    private String newPath;
    private GitCommit commit;
}