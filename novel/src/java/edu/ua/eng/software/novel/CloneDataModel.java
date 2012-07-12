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

import de.uni_bremen.st.rcf.model.Version;
import de.uni_bremen.st.rcf.model.CloneClasses;
import de.uni_bremen.st.rcf.model.CloneClass;
import de.uni_bremen.st.rcf.model.Fragments;
import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.Files;
import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.SourcePosition;

/**
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 */

public class CloneDataModel
{
    public CloneDataModel(Version latest) {
        version = latest;
        classes = latest.getCloneClasses();
        fileToClones = new HashMap<File, HashSet<Fragment>>();
        cloneToClass = new HashMap<Fragment, CloneClass>();

        populateMaps();
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

    private List<CloneClass> getCloneClasses() {
        return classes;
    }

    private CloneClass getCloneClass(Fragment clone) {
        return cloneToClass.get(clone);
    }

    private HashSet<Fragment> getClones(CloneClass klass) {
        return new HashSet<Fragment>(klass.getFragments());
    }

    private HashSet<Fragment> getClones(File file) {
        return fileToClones.get(file);
    }

    private File getFile(Fragment clone) {
        return clone.getStart().getFile();
    }

    private Version version;
    private List<CloneClass> classes;
    private HashMap<File, HashSet<Fragment>> fileToClones;
    private HashMap<Fragment, CloneClass> cloneToClass;
}