/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.EnumSet;
import edu.ua.eng.software.clonelink.FileChange.ChangeType;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 * @author      Casey Ferris <cmferris1@crimson.ua.edu>
 */

public class CommitData
{
    public CommitData() {
        commits = new LinkedList<Commit>();
        bugCommits = new LinkedList<Commit>();
        changes = new HashMap<String, List<FileChange>>();
        bugChanges = new HashMap<String, List<FileChange>>();
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public List<Commit> getBugCommits() {
        return bugCommits;
    }

    public List<FileChange> getChanges(String file) {
        return lookup(changes, file);
    }

    public List<FileChange> getBugChanges(String file) {
        return lookup(bugChanges, file);
    }

    public void add(Commit commit) {
        commits.add(commit);
        if (commit.isBugFix()) {
            bugCommits.add(commit);
        }
    }

    public void addFirst(Commit commit) {
        commits.addFirst(commit);
        if (commit.isBugFix()) {
            bugCommits.addFirst(commit);
        }
    }

    public void computeFileChanges() {
        computeFileChanges(EnumSet.allOf(ChangeType.class), true);
    }

    public void computeFileChanges(Collection<ChangeType> types, boolean carryChange) {
        EnumSet<ChangeType> typeSet = EnumSet.copyOf(types);
        for(Commit commit : commits) {
            for(FileChange change : commit.getFileChanges()) {
                if (carryChange) {
                    carryChanges(change);
                }
                if (typeSet.contains(change.getChangeType())) {
                    String filePath = change.getOldPath();
                    registerChange(filePath, change);
                }
            }
        }
    }

    private void carryChanges(FileChange change) {
        String oldPath = change.getOldPath();
        ChangeType type = change.getChangeType();
        if (type == ChangeType.COPY || type == ChangeType.RENAME) {
            String newPath = change.getNewPath();
            lookup(changes, newPath).addAll(lookup(changes, oldPath));
            lookup(bugChanges, newPath).addAll(lookup(bugChanges, oldPath));
        }
    }

    protected List<FileChange> lookup(Map<String, List<FileChange>> map, String key) {
        if (!map.containsKey(key)) {
            map.put(key, new LinkedList<FileChange>());
        } 
        return map.get(key);
    }

    protected void registerChange(String file, FileChange change) {
        lookup(changes, file).add(change);
        if(change.getCommit().isBugFix()) {
            lookup(bugChanges, file).add(change);
        }
    }

    private LinkedList<Commit> commits;
    private LinkedList<Commit> bugCommits;
    private Map<String, List<FileChange>> changes;
    private Map<String, List<FileChange>> bugChanges;
}