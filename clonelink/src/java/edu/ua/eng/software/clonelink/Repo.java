/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;



public abstract class Repo
{
    public CommitData getCommitData();

    protected void walk();
}