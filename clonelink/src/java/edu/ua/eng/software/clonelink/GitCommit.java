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
import java.io.IOException;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class GitCommit extends Commit
{
    public GitCommit(RevCommit commit, DiffFormatter differ) {
        setFileChanges(process(commit, differ));
        setMessage(commit.getFullMessage());
        setBugFix(checkBugFix());
    }

    protected Set<FileChange> process(RevCommit commit, DiffFormatter differ) {
        Set<FileChange> changes = new HashSet<FileChange>();
        for(int i = 0; i < commit.getParentCount(); i++) {
            try{
                List<DiffEntry> diffList = differ.scan(commit.getTree(), commit.getParent(i).getTree());
                for(DiffEntry de : diffList) {
                    changes.add(new GitFileChange(this, de));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return changes;
    }

}