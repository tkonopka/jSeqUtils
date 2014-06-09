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

import jsequtils.genome.GenomeInfo;
import jsequtils.genome.GenomePositionInterface;

/**
 * A derivative of VcfEntry, but here using the interface
 * GenomePositionInterface using integers for chromosomes.
 *
 * (Should now deprecate the VcfEntry class, so it's ok the code is duplicated.)
 *
 * @author Tomasz Konopka
 */
public class VCFEntry implements GenomePositionInterface {

    // this object has the canonical fields for a vcf entry
    private int position = 1;
    private int chrindex = -1;
    private String ref, alt, quality, format = "", genotype = "";
    private String id = ".";
    private String filter = ".";
    private String info = ".";
    private final GenomeInfo ginfo;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    public String getChr(GenomeInfo ginfo) {
        return ginfo.getChrName(chrindex);
    }

    public String getChr() {
        return ginfo.getChrName(chrindex);
    }

    public void setChr(String chr) {
        this.chrindex = ginfo.getChrIndex(chr);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * constructor that does not do anything, just creates blank values
     */
    public VCFEntry(GenomeInfo ginfo) {
        this.ginfo = ginfo;
    }

    /**
     * copy constructor. All fields are copied, object GenomeInfo is copied non-defensively.
     * 
     * @param entry 
     */
    public VCFEntry(VCFEntry entry) {
        this.position = entry.position;
        this.chrindex = entry.chrindex;
        this.ref = entry.ref;
        this.alt = entry.alt;
        this.quality = entry.quality;
        this.format = entry.format;
        this.genotype = entry.genotype;
        this.id = entry.id;
        this.filter = entry.filter;
        this.info = entry.info;
        this.ginfo = entry.ginfo;
    }

    /**
     * Constructor that parses a line from a vcf file
     *
     * @param entry
     */
    public VCFEntry(String entry, GenomeInfo ginfo) {
        this.ginfo = ginfo;
        String[] tokens = entry.split("\t");
        chrindex = ginfo.getChrIndex(tokens[0]);
        position = Integer.parseInt(tokens[1]);
        if (tokens.length > 2) {
            id = tokens[2];
            ref = tokens[3];
            alt = tokens[4];
            quality = tokens[5];
            filter = tokens[6];
            info = tokens[7];
            if (tokens.length > 8) {
                format = tokens[8];
                genotype = tokens[9];
            }
        }
    }

    public void setAlt(byte alt) {
        this.alt = "" + (char) alt;
    }

    public void setRef(byte ref) {
        this.ref = "" + (char) ref;
    }

    /**
     * return true if the entry is describing an indel. The isindel field should
     * be set manually. This function does not determine whether the combination
     * of ref and alt are actually describing an indel
     *
     * @return
     */
    public boolean isIndel() {
        return isIndel(ref, alt);
    }

    /**
     * helper function which decides whether a combination of ref and alt
     * strings can be considered as an indel
     *
     * @param ref
     * @param alt
     * @return
     */
    public static boolean isIndel(String ref, String alt) {
        // determine if the variant is an indel
        String[] tokref = ref.split(",");
        String[] tokalt = alt.split(",");
        for (int i = 0; i < tokref.length; i++) {
            if (tokref[i].length() > 1) {
                return true;
            }
        }
        for (int i = 0; i < tokalt.length; i++) {
            if (tokalt[i].length() > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if ((format + genotype).length() > 0) {
            return getChr() + "\t" + position + "\t" + id + "\t" + ref + "\t" + alt + "\t" + quality + "\t" + filter + "\t" + info + "\t" + format + "\t" + genotype + "\n";
        } else {
            return getChr() + "\t" + position + "\t" + id + "\t" + ref + "\t" + alt + "\t" + quality + "\t" + filter + "\t" + info + "\n";
        }
    }

    /**
     *
     * @return
     */
    public static String getHeader() {
        return "chr\tposition\tid\tref\talt\tquality\tfilter\tinfo\tformat\tgenotype\n";
    }

    @Override
    public int getChrIndex() {
        return chrindex;
    }

    @Override
    public String toString(GenomeInfo ginfo) {
        return toString();
    }
}
