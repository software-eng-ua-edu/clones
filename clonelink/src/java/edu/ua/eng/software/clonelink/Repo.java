/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 * @author      Casey Ferris <cmferris1@crimson.ua.edu>
 */

public abstract class Repo
{
    abstract public CommitData getCommitData();

    abstract protected void walk();
}