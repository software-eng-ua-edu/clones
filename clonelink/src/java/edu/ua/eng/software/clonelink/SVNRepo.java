/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 * @author      Paige Rodeghero <parodeghero@gmail.com>
 * @author      Casey Ferris <cmferris1@crimson.ua.edu>
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class SVNRepo extends Repo {
    public SVNRepo() {
        this.commitData = new CommitData();
    }

    public void walk() {
        DAVRepositoryFactory.setup();

        String url = "https://jhotdraw.svn.sourceforge.net/svnroot/jhotdraw";
        long startingRevision = 0;
        long endingRevision = -1;
                
        try{
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
            Collection logEntries = repository.log(new String[] {}, null, startingRevision, endingRevision, true, true);
            Iterator entries = logEntries.iterator();
            while(entries.hasNext()){
                SVNLogEntry logEntry = (SVNLogEntry) entries.next();             
                Commit commit = new Commit(logEntry.getChangedPaths().keySet(), logEntry.getMessage());
                commitData.add(commit);
            }   
        } catch (Exception e) {
        }
    }

    public CommitData getCommitData() {
        return commitData;
    }

    private CommitData commitData;
}
