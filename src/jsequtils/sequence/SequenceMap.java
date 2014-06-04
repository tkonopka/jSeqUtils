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

package jsequtils.sequence;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * An object holding sequences associated with chromosome names. 
 * This loads all sequences from a fasta file into memory, then allows
 * fast querying of any interval on any chromosome.
 * 
 * @author Tomasz Konopka
 */
public class SequenceMap {
   
    private final HashMap<String, byte[]> seqmap = new HashMap<String, byte[]>();
    
    /**
     * initialize the sequence map by reading a genome from a fasta file.
     * 
     * @param genome 
     * 
     * genome file to read
     * 
     * @param toupper
     * 
     * boolean determines if sequences are automatically converted to uppercase
     * 
     */
    public SequenceMap(File genome, boolean toupper) throws IOException {
       FastaReader fr = new FastaReader(genome);
       while (fr.hasNext()) {
           fr.readNext(toupper);
           seqmap.put(fr.getChromosomeName(), fr.getFullChromosomeSequence());
       }
       fr.close();
    }
    
    /**
     * get sequence associated with a genomic interval. 
     * 
     * @param chr
     * 
     * chromsome name
     * 
     * @param start
     * 
     * 0-based start coordinate (included)
     * 
     * @param to
     * 
     * 0-based start coordinate (not included)
     * 
     * @return 
     * 
     * sequence in interval [start, to).
     * 
     */
    public byte[] getSequenceBase0(String chr, int start, int to) {
        if (!containsChr(chr)) {
            return null;
        }
        return(Arrays.copyOfRange(seqmap.get(chr), start, to));        
    }
   
    public byte getSequenceBase0(String chr, int pos) {
        if (!containsChr(chr)) {
            return '\0';
        }
        return(seqmap.get(chr)[pos]);        
    }
    
    
    /**
     * get sequence associated with a genomic interval, defined using a
     * 1-based coordinate system.
     *      
     * 
     * @param chr
     * 
     * chromosome name
     * 
     * @param start
     * 
     * 1-based start coordinate (included)
     * 
     * @param to
     * 
     * 1-based start coordinate (included)
     * 
     * @return 
     * 
     * sequence in interval [start, to].
     * 
     */
    public byte[] getSequenceBase1(String chr, int start, int to) {
        if (!containsChr(chr)) {
            return null;
        }
        return(Arrays.copyOfRange(seqmap.get(chr), start-1, to));        
    }    
    
    public byte getSequenceBase1(String chr, int pos) {
        if (!containsChr(chr)) {
            return '\0';
        }
        return(seqmap.get(chr)[pos-1]);        
    }
    
    /**
     * 
     * @param chr
     * 
     * 
     * @return 
     * 
     * true if the chromosome is loaded in this sequence map object.
     * 
     */
    public boolean containsChr(String chr) {
        return seqmap.containsKey(chr);
    }
    
    /**
     * 
     * @param chr
     * @return 
     * 
     * the number of bases in a given chromosome
     * 
     */
    public int getChrLength(String chr) {
        if (containsChr(chr)) {
            return(seqmap.get(chr).length);
        } else {
            return -1;
        }
    }
    
    /**
     * 
     * @return 
     * 
     * number of chromosomes loaded into the sequence map
     * 
     */
    public int getNumChr() {
        return seqmap.size();
    }
}
