package io.payworks.labs.tcpmocker.support.resource;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ResourceUtilsTest {

    @DataProvider
    public Object[][] concatTestDataProvider() {
        return new Object[][] {
                {"basePath", "path", "basePath/path"},
                {"basePath/", "path", "basePath/path"},
                {"basePath", "/path", "basePath/path"},
                {"basePath/", "/path", "basePath/path"},
        };
    }

    @Test(dataProvider = "concatTestDataProvider")
    public void testConcat(final String basePath,
                           final String path,
                           final String expectedResult) {
        final String result = ResourceUtils.concat(basePath, path);

        assertEquals(result, expectedResult);
    }
}