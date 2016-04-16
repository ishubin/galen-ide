package com.galenframework.ide.tests.unit;

import com.galenframework.ide.model.settings.IdeArguments;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class IdeArgumentsTest {

    @Test
    public void should_parse_command_line_arguments_correctly() throws Exception {
        IdeArguments ideArguments = IdeArguments.parse(new String[]{"-P", "profile.json", "-p", "1234", "-s", "some/static/storage"});
        assertEquals(ideArguments.getProfile(), "profile.json");
        assertEquals(ideArguments.getPort(), 1234);
        assertEquals(ideArguments.getFileStorage(), "some/static/storage");
    }

    @Test
    public void should_parse_command_line_arguments_with_full_names_correctly() throws Exception {
        IdeArguments ideArguments = IdeArguments.parse(new String[]{"--profile", "profile.json", "--port", "1234", "--storage", "some/static/storage"});
        assertEquals(ideArguments.getProfile(), "profile.json");
        assertEquals(ideArguments.getPort(), 1234);
        assertEquals(ideArguments.getFileStorage(), "some/static/storage");
    }
}
