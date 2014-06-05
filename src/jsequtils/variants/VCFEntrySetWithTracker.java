/*
 * Copyright 2014 Tomasz Konopka.
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
 */
package jsequtils.variants;

import java.io.File;
import jsequtils.genome.GenomeInfo;
import jsequtils.genome.GenomePositionInterface;

/**
 * Class holds a set of VCF entries, like VCFEntrySet. Here, searching for
 * variants within the set is aided with a tracking pointer (an index).
 *
 * The concept is to speed up search for positions that are nearby. Consider
 * searching for a position adjacent to the last searched position. This class
 * will carry out the search in a linear fashion starting from the last
 * searched-for position, and arrive at a hit in constant time. In contrast, a
 * usual search always takes log(N) time.
 *
 * @author tkonopka
 */
public class VCFEntrySetWithTracker extends VCFEntrySet {

    // when searching for variants, try linear search up to so many comparisons before 
    // switching to binary search.
    private final int maxlinear;
    // for the linear search, need to have a record of the last searched-for position
    private int lastindex = 0;

    public VCFEntrySetWithTracker(File f, GenomeInfo ginfo, boolean withindels, int maxlinear) {
        super(f, ginfo, withindels);
        this.maxlinear = maxlinear;
    }

    /**
     * Uses a maximum of 3 linear steps. To change number of linear steps, use
     * the other constructor.
     *
     * @param f
     * @param ginfo
     * @param withindels
     */
    public VCFEntrySetWithTracker(File f, GenomeInfo ginfo, boolean withindels) {
        this(f, ginfo, withindels, 3);
    }

    /**
     * Overrides the getIndexOf function of the VCFEntrySet. This one performes
     * a few steps of linear search
     *
     * @param entry
     * @return
     */
    @Override
    public int getIndexOf(GenomePositionInterface entry) {

        // reminder for compare (o1, o2)
        // 0 - identical
        // >0 - o1 is after o2
        // <0 - o1 is before o2
        
        int numtries = -1;
        int direction = 0;
        int ss = size();
        while (numtries < maxlinear) {
            int temp = compare(entry, this.getVariant(lastindex));
            if (temp == 0) {
                return lastindex;
            } else if (temp < 0) {
                // move backward in the set
                if (lastindex > 0 && direction <= 0) {
                    lastindex--;
                    direction = -1;
                } else {
                    return -lastindex - 1;
                }
            } else if (temp > 0) {
                // move forward in the set                
                if (lastindex < ss - 1 && direction >= 0) {
                    lastindex++;
                    direction = +1;
                } else {
                    return -lastindex - 2;
                }
            }
            numtries++;
        }
        
        // if reached here, the linear search failed.
        int index = super.getIndexOf(entry);
        if (index < 0) {
            lastindex = -index - 1;
            if (lastindex == ss) {                
                lastindex--;
            }
        }
        return index;
    }
}
