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

import java.util.Comparator;

/**
 * A comparator for genome positions. Compares by chromosome first, then by position. 
 * Very similar to GenomicPositionComparator, but here the comparison is 
 * on chromosome indexes, not strings.
 * 
 * 
 * @author Tomasz Konopka
 */
public class GenomePositionComparator implements Comparator {
    
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

        GenomePositionInterface e1 = (GenomePositionInterface) o1;
        GenomePositionInterface e2 = (GenomePositionInterface) o2;

        int c1 = e1.getChrIndex();
        int c2 = e2.getChrIndex();
                
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
