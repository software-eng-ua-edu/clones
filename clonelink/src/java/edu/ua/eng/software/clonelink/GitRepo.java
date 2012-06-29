/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * @author      Colin C. Hemphill <colin@hemphill.us>
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 * @author      Casey Ferris <cmferris1@crimson.ua.edu> 
 */
public class GitRepo extends Repo {

    public GitRepo() {
        this.commitData = new CommitData();
    }

    public void walk() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder
                    .setGitDir(new File("regex/jhotdraw/.git")).readEnvironment()
                    .findGitDir().build();
            RevWalk walk = new RevWalk(repository);
            ObjectId rootId = repository.resolve("HEAD");
            RevCommit root = walk.parseCommit(rootId);
            walk.markStart(root);
            DiffFormatter diffr = new DiffFormatter(new ByteArrayOutputStream());
            diffr.setRepository(repository);
            Iterator<RevCommit> it = walk.iterator();

            for (int i = 0; it.hasNext(); i++) {
                RevCommit current = it.next();
                if (current.getParentCount() > 0) {
                    List<DiffEntry> diffList = diffr.scan(current.getTree(), current.getParent(0).getTree());
                    Set<String> changed = new HashSet<String>(); 
                    for (DiffEntry de : diffList) {
                        changed.add("/trunk/" + de.getOldPath());
                    }
                    Commit commit = new Commit(changed, current.getFullMessage());
                    commitData.add(commit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommitData getCommitData() {
        return commitData;
    }

    private CommitData commitData;
}