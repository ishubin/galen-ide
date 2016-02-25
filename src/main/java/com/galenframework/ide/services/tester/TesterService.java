package com.galenframework.ide.services.tester;

import com.galenframework.ide.*;
import com.galenframework.ide.services.Service;

public interface TesterService extends Service {
    void runtTest(TestCommand testCommand);

    TestCommand getLastTestCommand();
}
