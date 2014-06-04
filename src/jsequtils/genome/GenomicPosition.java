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

/**
 * Simple class implementing the GenomicPositionInterface. It just holds a
 * string as a chromosome and an integer as a position.
 *
 * @author Tomasz Konopka
 */
public class GenomicPosition implements GenomicPositionInterface {

    private String chr;
    private int position;

    public GenomicPosition(String chr, int position) {
        this.chr = chr;
        this.position = position;
    }

    @Override
    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return chr + ":" + position;
    }
}