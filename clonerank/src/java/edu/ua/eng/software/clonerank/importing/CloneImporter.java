/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */

package edu.ua.eng.software.clonerank.importing;

import java.util.ArrayList;
import java.io.File;
import edu.ua.eng.software.clonerank.CodeFragment;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public abstract class CloneImporter
{
    public abstract ArrayList<ArrayList<CodeFragment>> getClones(File cloneFile);
}