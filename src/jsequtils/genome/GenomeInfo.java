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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import jsequtils.file.BufferedReaderMaker;
import jsequtils.sequence.FastaReader;

/**
 * Structure holding basic information about a genome: chromosome names and
 * lengths.
 *
 * After the constructor, this object is for read-only purposes.
 *
 *
 * @author Tomasz Konopka
 */
public class GenomeInfo {

    // internally, the class will hold the names and lengths in arrays
    private final ArrayList<String> chrnames = new ArrayList<String>();
    private final ArrayList<Integer> chrlengths = new ArrayList<Integer>();
    // the class also stores (redundantly) mappings between chromsome names and integers
    // (indexes to the chrnames and chrlengths arrays)
    private final HashMap<String, Integer> chrindexes = new HashMap<String, Integer>();
    // will be useful to have the number of chromosomes
    private final int numchroms;

    /**
     * Define a genome with a set of chromosomes.
     * 
     * @param chrnames
     * 
     * list of chromosome names, in order of appearance.
     * 
     * @param chrlengths 
     * 
     * list of chromosome lengths, matching the order in chrnames.
     * 
     */
    public GenomeInfo(ArrayList<String> chrnames, ArrayList<Integer> chrlengths) {
        if (chrnames.size()!=chrlengths.size()) {
            numchroms=0;
            return;
        }
        
        for (int i=0; i<chrnames.size(); i++) {
            String nowchr = chrnames.get(i);
            int nowlen = chrlengths.get(i);
            chrindexes.put(nowchr, i);
            this.chrnames.add(nowchr);
            this.chrlengths.add(nowlen);
        }
        
        this.chrnames.trimToSize();
        this.chrlengths.trimToSize();
        numchroms = this.chrnames.size();
    }

    /**
     * Constructor using a genome fasta file. If an index file with extension
     * fai exists, all information is read from the index file. Otherwise, this
     * function will read all the sequence of the genome by brute force.
     *
     * @param genome
     * @throws IOException
     */
    public GenomeInfo(File genome) throws IOException {

        File faifile = new File(genome.getAbsolutePath() + ".fai");
        if (faifile.exists() && faifile.canRead()) {
            BufferedReader br = BufferedReaderMaker.makeBufferedReader(faifile);
            String s;
            while ((s = br.readLine()) != null) {
                String[] tokens = s.split("\t");
                chrindexes.put(tokens[0], chrnames.size());
                chrnames.add(tokens[0]);
                chrlengths.add(Integer.parseInt(tokens[1]));
            }
            br.close();
        } else {
            // genome index does not exist. Read the raw fasta file instead
            FastaReader fr = new FastaReader(genome);
            while (fr.hasNext()) {
                fr.readNext();
                chrindexes.put(fr.getChromosomeName(), chrnames.size());
                chrnames.add(fr.getChromosomeName());
                chrlengths.add(fr.getChromosomeLength());
            }
            fr.close();
        }

        chrnames.trimToSize();
        chrlengths.trimToSize();
        numchroms = chrnames.size();
    }

    /**
     *
     * @return
     *
     * the number of chromosomes in the genome
     */
    public int getNumChromosomes() {
        return numchroms;
    }

    /**
     *
     * @param index
     *
     * index of chromosome
     *
     * @return
     *
     *
     */
    public String getChrName(int index) {
        if (index < numchroms) {
            return chrnames.get(index);
        } else {
            return null;
        }
    }

    /**
     *
     * @param index
     * @return
     *
     * length of chromosome at given index. -1 if index is out of range.
     *
     */
    public int getChrLength(int index) {
        if (index < numchroms) {
            return chrlengths.get(index);
        } else {
            return -1;
        }
    }

    public boolean containsChr(String chr) {
        return chrindexes.containsKey(chr);
    }

    /**
     *
     * @param chrname
     * @return
     *
     * the length of a chromosome.
     */
    public int getChrLength(String chrname) {
        if (chrindexes.containsKey(chrname)) {
            return (chrlengths.get(chrindexes.get(chrname)));
        } else {
            return -1;
        }
    }

    /**
     *
     * @param chr
     * @return
     *
     * the index associated with a chromsome. -1 if the chromsome name has not
     * been defined.
     */
    public int getChrIndex(String chr) {
        if (containsChr(chr)) {
            return chrindexes.get(chr);
        } else {
            return -1;
        }
    }
}
