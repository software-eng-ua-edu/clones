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
*/
public class CloneLink {

	public static void main (String [] args) throws Exception {
            
            GitWalk git = new GitWalk();
            git.walk();
	}
}
