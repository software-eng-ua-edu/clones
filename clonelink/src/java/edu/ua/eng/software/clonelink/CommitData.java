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
        bugChanges = new HashMap<String, Integer>();
        changes = new HashMap<String, Integer>();
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public List<Commit> getBugCommits() {
        return bugCommits;
    }

    public int numBugChanges(String filePath) {
        return lookup(bugChanges, filePath);
    }

    public int numChanges(String filePath) {
        return lookup(changes, filePath);
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
                    incMap(changes, filePath);
                    if (commit.isBugFix()) {
                        incMap(bugChanges, filePath); 
                    }
                }
            }
        }
    }

    private void carryChanges(FileChange change) {
        String oldPath = change.getOldPath();
        ChangeType type = change.getChangeType();
        if (type == ChangeType.COPY || type == ChangeType.RENAME) {
            String newPath = change.getNewPath();
            for(int i = 0, j = lookup(changes, oldPath); i < j; i++) {
                incMap(changes, newPath);
            }
            for(int i = 0, j = lookup(bugChanges, oldPath); i < j; i++) {
                incMap(bugChanges, newPath);
            }
        }
    }

    protected int lookup(Map<String, Integer> map, String key) {
        return (map.containsKey(key)) ? map.get(key) : 0;
    }

    protected void incMap(Map<String, Integer> map, String key) {
        map.put(key, lookup(map, key) + 1);
    }

    private LinkedList<Commit> commits;
    private LinkedList<Commit> bugCommits;
    private Map<String, Integer> bugChanges;
    private Map<String, Integer> changes;
}