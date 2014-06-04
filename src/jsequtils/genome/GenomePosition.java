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
package jsequtils.genome;

/**
 * A simple class holding a chromosome and a position. Very similar to
 * GenomicPostion, but here the chromosome is stored an integer; the full chromosome 
 * name can only be read via a GenomeInfo translation object.
 *
 * @author Tomasz Konopka
 */
public class GenomePosition implements GenomePositionInterface {

    private final int chrindex;
    private final int position;

    public GenomePosition(String chr, int position, GenomeInfo ginfo) {
        this.chrindex = ginfo.getChrIndex(chr);
        this.position = position;
    }

    public GenomePosition(int chrindex, int position) {
        this.chrindex = chrindex;
        this.position = position;
    }

    @Override
    public String getChr(GenomeInfo ginfo) {
        return ginfo.getChrName(chrindex);
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "[" + chrindex + "]:" + position;
    }

    @Override
    public String toString(GenomeInfo ginfo) {
        return ginfo.getChrName(chrindex) + ":" + position;
    }

    @Override
    public int getChrIndex() {
        return this.chrindex;
    }    
}
