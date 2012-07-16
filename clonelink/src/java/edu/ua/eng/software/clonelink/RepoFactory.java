/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonelink;

/**
 * 
 * 
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 */

public class RepoFactory
{
    public static Repo build(String uri, RepoType type) {
        switch (type) {
            case GIT:
                return new GitRepo(uri).walk();
            case SVN:
                return new SVNRepo(uri).walk();
            default:
                return null;
        }
    }

    protected RepoFactory() {

    }
}