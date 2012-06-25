package edu.ua.eng.software.clonerank;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

public abstract class DistanceMetric
{
    public abstract int getDistance(String path1, String path2);

    protected List<String> breakPath(String path) {
        File file = new File(path);
        final LinkedList<String> pathList = new LinkedList<String>();
        do {
            pathList.addFirst(file.getName());
            file = file.getParentFile();
            System.out.println("test");
        } while (file != null);
        return pathList;
    }
}