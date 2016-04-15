/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.ide.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openqa.selenium.Dimension;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class SizeVariation {
    private Size start;
    private Size end;
    private int iterations;
    private boolean random = false;

    public SizeVariation() {
    }

    public SizeVariation(Size start, Size end, int iterations, boolean random) {
        this.start = start;
        this.end = end;
        this.iterations = iterations;
        this.random = random;
    }

    public Size getStart() {
        return start;
    }

    public void setStart(Size start) {
        this.start = start;
    }

    public Size getEnd() {
        return end;
    }

    public void setEnd(Size end) {
        this.end = end;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public List<Dimension> generateVariations() {
        if (end == null || iterations < 2) {
            return asList(start.toSeleniumDimension());
        } else {
            List<Dimension> sizes = new LinkedList<>();

            int deltaWidth  = end.getWidth() - start.getWidth();
            int deltaHeight = end.getHeight() - start.getHeight();
            int maxDistance = Math.max(Math.abs(deltaWidth), Math.abs(deltaHeight));

            int amount = Math.min(maxDistance, iterations);

            if (amount > 0) {
                int randomWidthDelta = 0;
                int randomHeightDelta = 0;

                for (int i = 0; i < amount; i++) {
                    if (random) {
                        randomWidthDelta = (int) (Math.random() * deltaWidth / amount);
                        randomHeightDelta = (int) (Math.random() * deltaHeight / amount);
                    }

                    int w = (int) Math.floor(start.getWidth() + deltaWidth * i / amount + randomWidthDelta);
                    int h = (int) Math.floor(start.getHeight() + deltaHeight * i / amount + randomHeightDelta);
                    sizes.add(new Dimension(w, h));
                }
            } else {
                sizes.add(new Dimension(start.getWidth(), start.getHeight()));
            }
            return sizes;
        }
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.start)
                .append(this.end)
                .append(this.iterations)
                .append(this.random)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj == this) {
            return true;
        } else if(!(obj instanceof SizeVariation)) {
            return false;
        } else {
            SizeVariation rhs = (SizeVariation) obj;
            return new EqualsBuilder()
                    .append(rhs.start, this.start)
                    .append(rhs.end, this.end)
                    .append(rhs.iterations, this.iterations)
                    .append(rhs.random, this.random)
                    .isEquals();
        }
    }
}
