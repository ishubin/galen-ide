package com.galenframework.ide;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomSnapshot {
    private String originSource;

    public DomSnapshot() {
    }

    public DomSnapshot(String originSource) {
        this.originSource = originSource;
    }

    public String getOriginSource() {
        return originSource;
    }

    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }

    public static DomSnapshot createSnapshotAndReplaceUrls(String source, String originUrl) throws MalformedURLException {
        String host = fetchHost(originUrl);

        Pattern pattern = Pattern.compile("\\s(src|href)(\\s*?)=(\\s)*?(\"|')(.*?)(\"|')");
        Matcher matcher = pattern.matcher(source);

        StringBuffer buff = new StringBuffer();
        while (matcher.find()) {
            String fullGroup = matcher.group();
            String href = matcher.group(5);
            String properHref;

            if (!href.startsWith("http:") && !href.startsWith("https:")) {
                properHref = "http://" + host;
                if (!href.startsWith("/")) {
                    properHref += "/";
                }
                properHref += href;
            } else {
                properHref = href;
            }

            matcher.appendReplacement(buff, fullGroup.replace(href, properHref));
        }
        matcher.appendTail(buff);

        return new DomSnapshot(new ScriptTagsRemover(buff.toString()).removeScriptTags());
    }

    private static String fetchHost(String originUrl) {
        int id1 = originUrl.indexOf("//");
        int id2 = originUrl.indexOf("/", id1 + 2);
        if (id2 < 0) {
            id2 = originUrl.length();
        }

        return originUrl.substring(id1 + 2, id2);
    }
}
