/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

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
        for(Commit commit : commits) {
            for(FileChange change : commit.getFilesChanged()) {
                String filePath = change.getNewPath();
                incMap(changes, filePath);
                if (commit.isBugFix()) {
                    incMap(bugChanges, filePath); 
                }
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