/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import edu.ua.eng.software.clonelink.CommitData;

/**
 * 
 * 
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 */

public class BugDataModel
{
    public static BugDataModel getInstance() {
        return model;
    }
    
    public static BugDataModel importData(CommitData data) {
        model = new BugDataModel(data);
        return model;
    }

    public CommitData getCommitData() {
        return commitData;
    }

    protected BugDataModel(CommitData data) {
        this.commitData = data;
    }

    private static BugDataModel model;
    private CommitData commitData;
}