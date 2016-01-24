package com.galenframework.ide;

import org.openqa.selenium.Dimension;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class SizeVariation {
    private Size start;
    private Size end;
    private int iterations;
    private boolean random = false;

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
                if (random) {
                    randomWidthDelta = (int) (Math.random() * deltaWidth / amount);
                    randomHeightDelta = (int) (Math.random() * deltaHeight / amount);
                }

                for (int i = 0; i < amount; i++) {
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
}
