package com.galenframework.ide.tests.integration.controllers;

import com.galenframework.ide.tests.integration.components.MockedWebApp;
import com.galenframework.ide.tests.integration.components.api.Response;
import com.galenframework.ide.tests.integration.mocks.MockRegistry;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mockito.Mockito;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.reset;
import static spark.Spark.stop;

public abstract class ApiTestBase {

    private String mockUniqueKey = UUID.randomUUID().toString();
    private List<Object> mocks = new LinkedList<>();

    @BeforeMethod
    public void resetAllMocks() {
        reset(mocks.toArray(new Object[mocks.size()]));
    }

    protected <T> T registerMock(Class<T> mockClass) {
        return registerMock(Mockito.mock(mockClass), mockClass);
    }

    protected <T> T registerMock(T mock, Class<T> mockClass) {
        MockRegistry.registerMock(mockUniqueKey, mock, mockClass.getName());
        mocks.add(mock);
        return mock;
    }

    @BeforeSuite
    public void startupMockedWebApp() throws IOException {
        MockedWebApp.create();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterSuite
    public void closeWebServer() {
        stop();
    }

    protected String getTestUrl() {
        return "http://localhost:4567";
    }

    private HttpClient client = HttpClientBuilder.create()
            .setConnectionTimeToLive(1, TimeUnit.MINUTES)
            .build();

    public Response postJson(String path, String requestBody) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        HttpPost httpPost = new HttpPost(URI.create(getTestUrl() + path));
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Cookie", MockedServiceProvider.MOCK_KEY_COOKIE_NAME + "=" + mockUniqueKey);
        HttpResponse response = client.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        String textResponse = IOUtils.toString(response.getEntity().getContent());
        return new Response(code, textResponse);
    }
}
