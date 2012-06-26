/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.clonerank;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 * @author      Conor B. Kirkman <conor.kirkman@gmail.com>
 */
public class CodeFragment
{
    public CodeFragment(String filename, int startLine, int endLine) {
        this.filename = filename;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String getFilename() { return filename; }
    public int getStartLine() { return startLine; }
    public int getEndLine() { return endLine; }

    private String filename;
    private int startLine;
    private int endLine;
}
