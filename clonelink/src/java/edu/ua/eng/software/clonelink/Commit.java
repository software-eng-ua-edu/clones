/*
* [The "New BSD" license]
* Copyright (c) 2012 The Board of Trustees of The University of Alabama
* All rights reserved.
*
* See LICENSE for details.
*/
package edu.ua.eng.software.clonelink;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 * @author      Casey Ferris <cmferris1@crimson.ua.edu>
 */

public class Commit
{
    public void setFileChanges(Set<FileChange> files) {
        this.files = files;
    }

    public void setBugFix(boolean bugFlag) {
        this.bugFlag = bugFlag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<FileChange> getFileChanges() {
        return files;
    }

    public boolean isBugFix() {
        return bugFlag;
    }

    public String getMessage() {
        return message;
    }

    public String getBugID() {
        return bugid;
    }

    protected boolean checkBugFix() {
        Matcher match = pattern.matcher(message);
        boolean isMatch = match.find();
        if(isMatch) {
            bugid = match.group(1);
        }
        return isMatch;
    }


    private Set<FileChange> files;
    private boolean bugFlag;
    private String message;
    private String bugid;
    static private final String regex = "(?:bug|fix|pr)\\s*(?:id|[#=])?\\s*([0-9]{4,6})";
    static private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
}