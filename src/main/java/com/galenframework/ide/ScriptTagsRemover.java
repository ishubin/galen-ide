package com.galenframework.ide;


public class ScriptTagsRemover {

    private final int position;
    private final String html;

    public ScriptTagsRemover(String html) {
        this.position = 0;
        this.html = html;
    }


    public String removeScriptTags() {
        String [] parts = html.split("(<script>|<script\\s)");

        StringBuilder builder = new StringBuilder();
        builder.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            builder.append(removeUntilEnding(parts[i]));
        }
        return builder.toString();
    }

    private String removeUntilEnding(String part) {
        int totalLength = part.length();
        int selectedSkipIndex;

        int closingSlashIndex = indexOfOrValue(part, "/>", totalLength  + 1);
        int closingScriptIndex = indexOfOrValue(part, "</script>", totalLength + 1);

        if (closingSlashIndex < closingScriptIndex) {
            selectedSkipIndex = closingSlashIndex + 2;
        } else {
            selectedSkipIndex = closingScriptIndex + 9;
        }

        if (selectedSkipIndex <= totalLength) {
            return part.substring(selectedSkipIndex);
        } else {
            return part;
        }
    }

    private int indexOfOrValue(String part, String sub, int totalLength) {
        int index = part.indexOf(sub);
        if (index >= 0) {
            return index;
        } else {
            return totalLength;
        }
    }
}
