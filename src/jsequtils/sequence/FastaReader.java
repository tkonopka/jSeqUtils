/*
 * Copyright 2012 Tomasz Konopka.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import jsequtils.file.BufferedReaderMaker;

/**
 * Reader of a fasta file, one chromosome/contig at a time.
 *
 * @author tkonopka
 */
public class FastaReader {

    private String name;
    private byte[] sequence;
    private String nextname;
    private BufferedReader chromosomereader;

    /**
     * creates a new reader for fasta files
     *
     * @param reader
     */
    public FastaReader(BufferedReader reader) throws IOException {
        chromosomereader = reader;
        name = null;
        sequence = null;
        nextname = null;

        // read the first line from the reader and record it as the next chromosome's name
        String s = chromosomereader.readLine();
        if (s.length() > 0 && s.startsWith(">")) {
            nextname = s.substring(1);
        } else {
            nextname = null;
        }
    }

    /**
     * creates a new reader for fasta files
     * 
     * @param genome
     * 
     * fasta file containing genome
     * 
     * @throws IOException 
     */
    public FastaReader(File genome) throws IOException {
        this(BufferedReaderMaker.makeBufferedReader(genome));
    }
    
    /**
     * load the next chromosome sequence into memory.
     *
     * This is a special case for readNext(boolean toUpper) with toUpper 
     * set to true, i.e. all characters will be set to uppercase by default.
     * 
     * @throws IOException
     */
    public void readNext() throws IOException {
        readNext(true);
    }

    /**
     * load the next chromosome into memory
     *
     * @param toUpper
     *
     * allows user to specify whether or not to force input character to
     * uppercase
     *
     * @throws IOException
     */
    public void readNext(boolean toUpper) throws IOException {

        // don't read if it is not necessary
        if (!hasNext()) {
            name = null;
            nextname = null;
            return;
        }

        StringBuilder sb = new StringBuilder(65536);
        String s;

        name = nextname;
        nextname = null;

        boolean readmore = true;
        while (readmore && ((s = chromosomereader.readLine()) != null)) {
            if (s.length() > 0 && s.startsWith(">")) {
                nextname = s.substring(1);
                readmore = false;                
            } else {
                sb.append(s);
            }
        }

        // create a byte array with the sequence from the string builder
        sequence = new byte[sb.length()];
        int sblen = sb.length();
        if (toUpper) {            
            for (int i = 0; i < sblen; i++) {
                sequence[i] = (byte) Character.toUpperCase(sb.charAt(i));
            }
        } else {
            for (int i = 0; i < sblen; i++) {
                sequence[i] = (byte) sb.charAt(i);
            }
        }

    }

    /**
     *
     * @return
     *
     * true if the file contains information about another chromosome
     *
     */
    public boolean hasNext() {
        if (nextname == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @return
     *
     * true if the reader has a currently loaded chromosome
     *
     */
    public boolean hasThis() {
        if (name == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @return
     *
     * the name of the currently loaded chromosome (null if nothing has been
     * read yet, use readNext() to load first chromosome)
     */
    public String getChromosomeName() {
        return name;
    }

    /**
     *
     * @return
     *
     * number of bases read for currently loaded chromosome
     *
     */
    public int getChromosomeLength() {
        if (sequence == null) {
            return 0;
        } else {
            return sequence.length;
        }
    }

    /**
     *
     * @param position
     * 
     * a position index in a 1-based coordinate system. (First character in sequence is 
     * position one)
     * 
     * @return
     *
     * base at location position
     *
     */
    public byte getBaseAtPositionBase1(int position) {
        return sequence[position - 1];
    }

    /**
     *
     * @param position
     * 
     * a position index in a 0-based coordinate system. (First character in sequence is 
     * position zero)     
     * 
     * @return
     *
     * base at location position
     */
    public byte getBaseAtPositionBase0(int position) {
        return sequence[position];
    }

    /**
     *
     * @param start
     * @param end
     * @return
     *
     * a copy of the sequence from positions start (inclusive) to end
     * (exclusive). Coordinates are zero-based.
     *
     */
    public byte[] getSequenceBase0(int start, int end) {
        byte[] newseq = new byte[end - start];
        System.arraycopy(sequence, start, newseq, 0, end - start);
        return newseq;
    }

    /**
     *
     * @param start
     * @param end
     * @return
     *
     * a copy of the sequence from positions start (inclusive) to end
     * (inclusive). Coordinates are one-based.
     *
     */
    public byte[] getSequenceBase1(int start, int end) {
        byte[] newseq = new byte[end - start + 1];
        System.arraycopy(sequence, start - 1, newseq, 0, end - start + 1);
        return newseq;
    }

    byte[] getFullChromosomeSequence() {
        return sequence;
    }
    
    /**
     * closes the buffered reader
     */
    public void close() {
        try {
            chromosomereader.close();
        } catch (Exception ex) {
        }
        name = null;
        nextname = null;
    }
}