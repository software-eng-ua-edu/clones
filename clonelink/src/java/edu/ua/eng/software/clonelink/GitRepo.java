/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.lib.IndexDiff;

/**
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
public class GitRepo extends Repo {

    public GitRepo() {
        this.commitData = new CommitData();
    }

    public void walk() throws Exception {

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setGitDir(new File("regex/jhotdraw/.git")).readEnvironment()
                .findGitDir().build();
        RevWalk walk = new RevWalk(repository);
        ObjectId rootId = repository.resolve("HEAD");
        RevCommit root = walk.parseCommit(rootId);
        walk.markStart(root);
        Iterator<RevCommit> it = walk.iterator();

        for (int i = 0; it.hasNext(); i++) {
            RevCommit current = it.next();
            FileTreeIterator fileTreeItr = new FileTreeIterator(repository);
            IndexDiff diff = new IndexDiff (repository, current.getId(), fileTreeItr);
            diff.diff();
            List<String> files = new ArrayList<String>();
            files.addAll(diff.getChanged());
            Commit commit = new Commit(files, current.getFullMessage());
            commitData.add(commit);
        }
    }

    public CommitData getCommitData() {
        return commitData;
    }

    private CommitData commitData;
}