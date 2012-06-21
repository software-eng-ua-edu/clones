package edu.ua.eng.software.CloneRank;

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
