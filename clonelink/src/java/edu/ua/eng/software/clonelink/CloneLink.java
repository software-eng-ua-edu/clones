/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;

/**
* @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
* @author      Colin C. Hemphill <colin@hemphill.us>
* @author      Casey M. Ferris <cmferris1@crimson.ua.edu>
*/
public class CloneLink {

	public static void main (String [] args) throws Exception {
            
            //Git Repo testing
            GitWalk git = new GitWalk();
            git.walk();


            //CVS Repo testing
            CvsClient cvs = new CvsClient();
	}
}
