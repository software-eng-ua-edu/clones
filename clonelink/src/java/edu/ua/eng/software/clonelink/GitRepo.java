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
public class GitRepo {

    public void walk() throws Exception {

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setGitDir(new File("regex/jhotdraw/.git")).readEnvironment()
                .findGitDir().build();
        // System.out.println(repository.getFullBranch());
        RevWalk walk = new RevWalk(repository);
        ObjectId rootId = repository.resolve("HEAD");
        RevCommit root = walk.parseCommit(rootId);
        walk.markStart(root);
        Iterator<RevCommit> it = walk.iterator();

        // Scanner scan = new Scanner(System.in);
        // System.out.println("Expression to find in commit messages: ");
        // String input = scan.nextLine();
        // Pattern pattern = Pattern.compile(".*" + input + ".*",
        //         Pattern.CASE_INSENSITIVE);

        Pattern pattern = Pattern.compile("((bug|fix|pr)\\s*[#=]?\\s*[0-9]{4,6})",
                Pattern.CASE_INSENSITIVE);

        for (int i = 0; it.hasNext(); i++) {
            RevCommit current = it.next();
            FileTreeIterator fileTreeItr = new FileTreeIterator(repository);
            IndexDiff diff = new IndexDiff (repository, current.getId(), fileTreeItr);
            diff.diff();
            Matcher matcher = pattern.matcher(current.getFullMessage());
            if (matcher.find()) {
                System.out.println(i + ": " + current.getFullMessage());
                System.out.println(diff.getChanged().toArray()[0]);
                // System.out.println(current.getFullMessage());
            } else {

            }
        }
    }
}