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
package jsequtils.regions;

import java.util.BitSet;
import java.util.HashMap;
import jsequtils.genome.GenomeInfo;

/**
 * A generalisation of a bitset to a genome. The class holds a hashmap storing
 * one bitset per chromosome.
 *
 * The data structure can remember flags associated with a given genomic
 * position.
 *
 * @author tkonopka
 */
public class GenomeBitSet {

    private final HashMap<String, BitSet> genomebitset = new HashMap<String, BitSet>();

    public GenomeBitSet(GenomeInfo genomeinfo) {
        for (int i=0; i<genomeinfo.getNumChromosomes(); i++) {
            String nowname = genomeinfo.getChrName(i);
            int nowlength = genomeinfo.getChrLength(nowname);
            if (!genomebitset.containsKey(nowname)) {
                genomebitset.put(nowname, new BitSet(nowlength));
            }            
        }
    }
            
    /**
     * clears all bits of the chromosome to zero/off
     *
     */
    public void clearChr(String chr) {
        BitSet chrbitset = genomebitset.get(chr);
        if (chrbitset != null) {
            chrbitset.clear();
        }
    }

    /**
     * sets an interval on one chromosome to true/false.
     *
     * Note: start/end are interpreted here in bed format. Start is inclusive,
     * end is exclusive.
     *
     * @param chr
     * @param start
     * @param end
     * @param value
     */
    public void set(String chr, int start, int end, boolean value) {
        BitSet chrbitset = genomebitset.get(chr);
        if (chrbitset != null) {
            chrbitset.set(start, end, value);
        }
    }

    /**
     * sets an interval on one chromosome to true.
     *
     * @param chr
     * @param start
     * @param end
     */
    public void set(String chr, int start, int end) {
        set(chr, start, end, true);
    }

    /**
     * Get the value/flag at one genomic position
     *
     * @param chr
     * @param position
     * @return
     */
    public boolean get(String chr, int position) {
        BitSet chrbitset = genomebitset.get(chr);
        if (chrbitset != null) {
            return chrbitset.get(position);
        }
        return false;
    }

    /**
     * Get all flags at one genomic interval
     *
     * @param chr
     * @param start
     * @param end
     * @return
     */
    public BitSet get(String chr, int start, int end) {
        BitSet chrbitset = genomebitset.get(chr);
        if (chrbitset != null) {
            return chrbitset.get(start, end);
        } else {
            return null;
        }
    }
}