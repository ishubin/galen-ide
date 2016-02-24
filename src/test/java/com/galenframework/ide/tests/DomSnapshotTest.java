package com.galenframework.ide.tests;

import com.galenframework.ide.DomSnapshot;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class DomSnapshotTest {


    @Test
    public void shouldReplace_allLinks_andRemoveScripts() throws IOException {
        String originSource = IOUtils.toString(getClass().getResourceAsStream("/some-source-origin.html"));
        String expectedResult = IOUtils.toString(getClass().getResourceAsStream("/sanitized-source.html"));

        DomSnapshot domSnapshot = DomSnapshot.createSnapshotAndReplaceUrls(originSource, "http://another.example.com");

        assertEquals(trimEveryLine(domSnapshot.getOriginSource()), expectedResult);
    }

    private String trimEveryLine(String text) {
        StringBuilder builder = new StringBuilder();
        String lines[] = text.split("\\r?\\n");

        for (String line : lines) {
            builder.append(line.trim());
            builder.append('\n');
        }
        return builder.toString();
    }
}
