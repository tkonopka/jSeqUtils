/*
 * Copyright 2013 Tomasz Konopka.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */
package jsequtils.variants;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import jsequtils.file.BufferedReaderMaker;
import jsequtils.genome.GenomeInfo;
import jsequtils.genome.GenomePosition;
import jsequtils.genome.GenomePositionComparator;
import jsequtils.genome.GenomePositionInterface;

/**
 * Container class that stores a set of variants in an array. Array should be
 * immutable, i.e. user cannot add or remove items to the set.
 *
 *
 * @author tkonopka
 */
public class VCFEntrySet {

    // hold information from a vcf file - header, column definitions, and variants
    private final StringBuilder header = new StringBuilder();
    private final StringBuilder coldefs = new StringBuilder();
    private final VCFEntry[] variants;
    // genome information (used for sorting and searching for variants)
    private final GenomeInfo ginfo;
    private final GenomePositionComparator vcomp;

    /**
     * A constructor that starts with a list of variants already in memory
     * @param vars
     * @param ginfo
     * @param withindels 
     */
    public VCFEntrySet(ArrayList<VCFEntry> vars, GenomeInfo ginfo, boolean withindels) {
        this.ginfo = ginfo;
        this.vcomp = new GenomePositionComparator();

        // get copy of variants that meet withindels criteria
        ArrayList<VCFEntry> tempvars = new ArrayList<>(vars.size());
        for (int i = 0; i < vars.size(); i++) {
            if (withindels || !vars.get(i).isIndel()) {
                tempvars.add(new VCFEntry(vars.get(i).toString(), ginfo));
            }
        }

        // move the variants into a fixed-length array
        variants = new VCFEntry[tempvars.size()];
        for (int i = 0; i < tempvars.size(); i++) {
            variants[i] = tempvars.get(i);
        }

        Arrays.sort(variants, vcomp);
    }

    /**
     *
     * A set of variants are read from a file. Header, columns and variants will
     * be stored in memory.
     *
     * @param f
     * @param vcomp
     */
    public VCFEntrySet(File f, GenomeInfo ginfo, boolean withindels) {

        // comparator will be useful to search and sort variants
        this.ginfo = ginfo;
        this.vcomp = new GenomePositionComparator();

        // reader for the input vcf file
        BufferedReader vcfreader;

        // temporary object (because don't know how many variants to read from file, use resizeable arraylist)
        ArrayList<VCFEntry> tempvars = new ArrayList<VCFEntry>(1048576);

        // read entries from the file into memory
        try {
            vcfreader = BufferedReaderMaker.makeBufferedReader(f);

            // read the header
            boolean headerdone = false;
            String line = vcfreader.readLine();
            while (line != null && !headerdone) {
                if (line.startsWith("#CHROM")) {
                    coldefs.append(line).append("\n");
                    headerdone = true;
                } else if (line.startsWith("#")) {
                    header.append(line).append("\n");
                }
                line = vcfreader.readLine();
            }

            // read the whole vcf file into memory

            while (line != null) {
                VCFEntry nowentry = new VCFEntry(line, ginfo);
                // add it if the variant is a substitution or if indels are explicitly allowed
                if (nowentry.isIndel()) {
                    if (withindels) {
                        tempvars.add(nowentry);
                    }
                } else {
                    tempvars.add(nowentry);
                }
                line = vcfreader.readLine();
            }
            vcfreader.close();
        } catch (Exception ex) {
            System.out.println("Error reading vcf file: " + ex.getMessage());
        }

        // move the variants into a fixed-length array
        variants = new VCFEntry[tempvars.size()];
        for (int i = 0; i < tempvars.size(); i++) {
            variants[i] = tempvars.get(i);
        }

        Arrays.sort(variants, vcomp);
        System.gc();
    }

    /**
     *
     * @param index
     * @return
     *
     * variant at desired index. This function returns the object in the set,
     * not a copy, so be careful. If the VCFEntry is changed on the chromosome
     * or position field, the set may become unordered and search will not
     * longer work properly.
     *
     */
    public VCFEntry getVariant(int index) {
        return variants[index];
    }

    /**
     *
     * @return
     *
     * number of variants in the set.
     *
     */
    public int size() {
        return variants.length;
    }

    public String getHeader() {
        return header.toString();
    }

    public String getColDefLine() {
        return coldefs.toString();
    }

    /**
     * Checks if the entryset has a variant at position indicated by the given
     * variant.
     *
     * @param entry
     *
     * @return
     *
     */
    public boolean containsPosition(GenomePositionInterface entry) {
        return (Arrays.binarySearch(variants, entry, vcomp) >= 0);
    }

    /**
     * compares the entry with the stored variants.
     *
     * @param entry
     * @return
     *
     * the index of the
     */
    public int getIndexOf(GenomePositionInterface entry) {
        return Arrays.binarySearch(variants, entry, vcomp);
    }

    /**
     * Checks if the entryset has a variant at position indicated by the string.
     * The string should be in format, e.g. chr5:2039
     *
     * @param locus
     * @return
     */
    public boolean containsPosition(String locus) {
        String[] tokens = locus.split(":");
        GenomePosition newentry = new GenomePosition(tokens[0], Integer.parseInt(tokens[1]), ginfo);
        return containsPosition(newentry);
    }

    /**
     *
     * @param entry
     * @return
     *
     * null if the entry set does not contain a variant at indicated position.
     *
     */
    public VCFEntry getAtLocus(GenomePositionInterface entry) {
        int pos = Arrays.binarySearch(variants, entry, vcomp);
        if (pos < 0) {
            return null;
        } else {
            return new VCFEntry(variants[pos].toString(), ginfo);
        }
    }

    /**
     * similar to getNumberInInterval with chrindex.
     *
     * interval is (start, end), i.e. both are included
     *
     * @param chr
     * @param start
     * @param end
     * @return
     */
    public int getNumberInInterval(String chr, int start, int end) {
        if (end < start) {
            return 0;
        }

        int chrindex = ginfo.getChrIndex(chr);
        return getNumberInInterval(chrindex, start, end);
    }

    /**
     * looks through a set of variants and compute how many are within an
     * interval
     *
     * interval is (start, end), i.e. both are included
     *
     * @param chr
     * @param start
     * @param end
     * @return
     */
    public int getNumberInInterval(int chrindex, int start, int end) {
        if (end < start) {
            return 0;
        }

        // get index of start using a binary search
        int startindex = getIndexOf(new GenomePosition(chrindex, start));
        if (startindex < 0) {
            startindex = -startindex - 1;
        }

        // get the end index by forward linear search
        // this should work if the interval is small
        int endindex = startindex;
        if (end - start < 256) {
            // for short intervals, use a linear search
            int varsize = variants.length;
            GenomePosition gend = new GenomePosition(chrindex, end);
            while (endindex < varsize && vcomp.compare(variants[endindex], gend) <= 0) {
                endindex++;
            }
        } else {
            // for larger intervals, use binary search            
            endindex = Arrays.binarySearch(variants, new GenomePosition(chrindex, end + 1), vcomp);
            if (endindex < 0) {
                endindex = -endindex - 1;
            }
        }
        return endindex - startindex;
    }

    /**
     * extensions of class to compare positions using the vcomp comparator.
     *
     * @param o1
     * @param o2
     * @return
     */
    int compare(Object o1, Object o2) {
        return vcomp.compare(o1, o2);
    }
}