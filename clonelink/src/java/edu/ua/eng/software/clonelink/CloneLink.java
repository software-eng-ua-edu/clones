/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;

import java.util.EnumSet;

import edu.ua.eng.software.clonelink.FileChange.ChangeType;

/**
* @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
* @author      Colin C. Hemphill <colin@hemphill.us>
* @author      Paige A. Rodeghero <parodeghero@bsu.edu>
* @author      Blake Bassett <rbbassett@crimson.ua.edu>
* @author      Casey Ferris <cmferris1@crimson.ua.edu>
*/
public class CloneLink {

	public static void main (String [] args) throws Exception {

        //Git Repo testing
        System.out.println("Setting up Git Repo.");
        Repo git = new GitRepo();
        git.walk();

        //SVN Repo testing
        System.out.println("Setting up SVN Repo.");
        Repo svn = new SVNRepo();
        svn.walk();

        EnumSet<ChangeType> modify = EnumSet.of(ChangeType.ADD, ChangeType.MODIFY);

        CommitData gitCD = git.getCommitData();
        CommitData svnCD = svn.getCommitData();

        gitCD.computeFileChanges(modify, true);
        svnCD.computeFileChanges(modify, true);

        Commit gitTest = gitCD.getBugCommits().get(1);

        for(FileChange change : gitTest.getFileChanges()) {
            String file = change.getNewPath();
            System.out.println(file + "::" + change.getChangeType());
            System.out.printf("Git:: changes: %d, bugFixes: %d\n", gitCD.numChanges(file), gitCD.numBugChanges(file));
            System.out.printf("SVN:: changes: %d, bugFixes: %d\n", svnCD.numChanges(file), svnCD.numBugChanges(file));
        }
	}
}
