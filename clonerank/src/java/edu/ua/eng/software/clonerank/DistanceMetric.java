package edu.ua.eng.software.clonerank;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

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

    protected int pathDistance(List<String> path1, List<String> path2) {
        int distance = -1;
        if(path1.get(0).equals(path2.get(0))) {
            path1 = path1.subList(0, path1.size() - 1);
            path2 = path2.subList(0, path2.size() - 1);

            distance = path1.size() + path2.size();
            Iterator<String> it1 = path1.iterator();
            Iterator<String> it2 = path2.iterator();
            while(it1.hasNext() && it2.hasNext() && it1.next().equals(it2.next())) {
                distance -= 2;
            }
        }
        return distance;
    }
}