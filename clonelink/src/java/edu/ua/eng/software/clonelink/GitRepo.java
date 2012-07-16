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

    public GitRepo(String uri) {
        this.uri = uri;
        this.commitData = new CommitData();
    }

    public void walk() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder
                    .setGitDir(new File(uri)).readEnvironment()
                    .findGitDir().build();

            RevWalk walk = new RevWalk(repository);
            ObjectId rootId = repository.resolve("HEAD");
            RevCommit root = walk.parseCommit(rootId);
            walk.markStart(root);

            DiffFormatter diffr = new DiffFormatter(new ByteArrayOutputStream());
            diffr.setRepository(repository);

            for (RevCommit rc : walk) {
                commitData.addFirst(new GitCommit(rc, diffr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommitData getCommitData() {
        return commitData;
    }

    private CommitData commitData;
    private String uri;
}