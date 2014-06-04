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
 */
package jsequtils.genome;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

/**
 * A comparator that can be used to sort genomic positions (chr:position pairs).
 *
 * Objects that can be compared using the compare() function MUST implement the
 * (GenomicPosiionInterface).
 *
 *
 * @author tkonopka
 */
public class GenomicPositionComparator implements Comparator {

    // the preset order of strings will be stored in a hashmap for quick lookup    
    private final GenomeInfo genomeinfo;

    /**
     * make a comparator, with chromosome order gotten from a genome fasta or 
     * a genome index file.     
     *
     * @param genome
     *
     * File to fasta file with genome. The constructor makes a GenomeInfo object
     * internally, see other constructor.
     *
     *
     */
    public GenomicPositionComparator(File genome) throws IOException {
        genomeinfo = new GenomeInfo(genome);
    }

    /**
     * Constructor using an already made GenomeInfo object.
     * 
     * @param genomeinfo 
     */
    public GenomicPositionComparator(GenomeInfo genomeinfo) {
        this.genomeinfo = genomeinfo;
    }

    /**
     * 
     * @param o1
     * @param o2
     * @return 
     * 
     * negative number if o1 is before o2. 
     * (i.e. if chromosome o1 is before chromosome o2, or if position is smaller on same chromosome)
     *
     * positive num if o2 is before o1.
     * 
     * 0 when the two position are exactly the same.
     * 
     */
    @Override
    public int compare(Object o1, Object o2) {

        GenomicPositionInterface e1 = (GenomicPositionInterface) o1;
        GenomicPositionInterface e2 = (GenomicPositionInterface) o2;

        int c1 = genomeinfo.getChrIndex(e1.getChr());
        int c2 = genomeinfo.getChrIndex(e2.getChr());
                
        // if the chromosomes are different, use the preset order
        if (c1 == -1 || c2 == -1) {
            if (c1 == -1) {
                return -1;
            } else {
                return 1;
            }
        } else if (c1 != c2) {
            return c1 - c2;
        }

        // if the chromosomes are the same compare the positions        
        return (e1.getPosition() - e2.getPosition());
    }
}