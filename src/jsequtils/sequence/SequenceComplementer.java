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

/**
 * get the complement to a genomic sequence
 *
 * @author tkonopka
 */
public class SequenceComplementer {

    /**
     * Basic complementer, i.e. converted A->T, C->G, etc.
     *
     * @param base
     * @return
     */
    public static byte complement(byte base) {
        switch (base) {
            case 'A':
                return 'T';
            case 'T':
                return 'A';
            case 'C':
                return 'G';
            case 'G':
                return 'C';
            case 'a':
                return 't';
            case 't':
                return 'a';
            case 'c':
                return 'g';
            case 'g':
                return 'c';
            case 'n':
                return 'n';
            default:
                return 'N';
        }
    }

    /**
     * Basic complemented. This function converts the char into a byte
     * before using the other complement function. 
     * 
     * @param base
     * @return 
     */
    public static char complement(char base) {
        return (char) complement((byte) base);
    }

    /**
     *
     * @param sequence
     *
     * genomic sequence with ATCG and other characters
     *
     * @return
     *
     * a sequence complementary to the one input. ATCG bases are processed.
     * Other bases are turned into N.
     *
     *
     */
    public static byte[] complement(byte[] sequence) {

        // avoid processing empty signals
        if (sequence == null) {
            return null;
        }

        int seqlen = sequence.length;
        byte[] complement = new byte[seqlen];
        int maxindex = seqlen - 1;
        for (int i = 0; i < seqlen; i++) {
            complement[maxindex - i] = complement(sequence[i]);
        }

        return complement;
    }
}
