/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;

import de.uni_bremen.st.rcf.model.CloneClass;
import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.Version;

/**
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 */

public class CloneDataModel
{
    public static CloneDataModel getInstance() {
        return model;
    }

    public static CloneDataModel importData(Version latest) {
        model.setVersion(latest);
        return model;
    }

    protected CloneDataModel() {
        classes = new LinkedList<CloneClass>();
        files = new Vector<File>();
        fileToClones = new HashMap<File, HashSet<Fragment>>();
        cloneToClass = new HashMap<Fragment, CloneClass>();
    }

    private void setVersion(Version latest) {
        clear();
        version = latest;
        classes.addAll(latest.getCloneClasses());
        files.addAll(version.getFiles());

        populateMaps();
    }

    private void clear() {
        classes.clear();
        files.clear();
        fileToClones.clear();
        cloneToClass.clear();
    }

    private void populateMaps() {
        for (CloneClass klass : classes) {
            for (Fragment clone : klass.getFragments()) {
                cloneToClass.put(clone, klass);
                File file = clone.getStart().getFile();
                if (!fileToClones.containsKey(file)) {
                    fileToClones.put(file, new HashSet<Fragment>());
                }
                fileToClones.get(file).add(clone);
            }
        }
    }

    public Version getVersion() {
        return version;
    }

    public List<CloneClass> getCloneClasses() {
        return classes;
    }

    public CloneClass getCloneClass(Fragment clone) {
        return cloneToClass.get(clone);
    }

    public HashSet<Fragment> getClones(CloneClass klass) {
        return new HashSet<Fragment>(klass.getFragments());
    }

    public HashSet<Fragment> getClones(File file) {
        return fileToClones.get(file);
    }

    public Vector<File> getFiles() {
        return files;
    }

    public File getFile(Fragment clone) {
        return clone.getStart().getFile();
    }

    private static CloneDataModel model = new CloneDataModel();

    private Version version;
    private List<CloneClass> classes;
    private Vector<File> files;
    private HashMap<File, HashSet<Fragment>> fileToClones;
    private HashMap<Fragment, CloneClass> cloneToClass;
}