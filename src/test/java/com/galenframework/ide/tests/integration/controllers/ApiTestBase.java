package com.galenframework.ide.tests.integration.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.tests.integration.components.MockedWebApp;
import com.galenframework.ide.tests.integration.components.api.Response;
import com.galenframework.ide.tests.integration.mocks.MockRegistry;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mockito.Mockito;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
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
        .setConnectionTimeToLive(5, TimeUnit.SECONDS)
        .setMaxConnTotal(20)
        .build();

    public Response getJson(String path) throws IOException {
        HttpGet httpGet = new HttpGet(createUri(path));
        return executeRequest(httpGet);
    }

    private URI createUri(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return URI.create(getTestUrl() + path);
    }


    public Response postJson(String path, String requestBody) throws IOException {
        HttpPost httpPost = new HttpPost(createUri(path));
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        return executeRequest(httpPost);
    }

    private Response executeRequest(HttpRequestBase httpRequestBase) throws IOException {
        httpRequestBase.setHeader("Content-Type", "application/json");
        httpRequestBase.setHeader("Cookie", MockedServiceProvider.MOCK_KEY_COOKIE_NAME + "=" + mockUniqueKey);
        HttpResponse response = client.execute(httpRequestBase);
        int code = response.getStatusLine().getStatusCode();
        String textResponse = IOUtils.toString(response.getEntity().getContent());
        return new Response(code, textResponse);
    }

    ObjectMapper mapper = new ObjectMapper();

    public String jsonFromResources(String path) throws IOException {
        String contents = FileUtils.readFileToString(new File(getClass().getResource(path).getFile()));
        JsonNode jsonNode = mapper.readTree(contents);
        return mapper.writeValueAsString(jsonNode);
    }
}
