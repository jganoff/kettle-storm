/*
 * Author: cbedford
 * Date: 11/24/13
 * Time: 8:23 PM
 */



import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

@Test
public class KtrIntegrationTest {

    @BeforeClass
    public static void setSystemProperties() {
        System.setProperty("kettle-storm-local-mode", "true");
    }

    @Test(enabled = true)
    public void testWithJavaScriptStep() throws Exception {
        String expectedOutput  = runTransformation("classpath:javaScriptTest.ktr", "/tmp/MyHello.xml.xml");
        assert  expectedOutput.contains("<Row><msg>Hello, hank");
        assert  expectedOutput.contains("<Row><msg>Hello, joe");
    }

    /**
     *      Run transformation with simple string manipulation and verify that the output
     *      file is in the correct place and has the expected content.
     */
    @Test
    public void  testWithSimpleStringModification() throws Exception {
        String expectedOutput  = runTransformation("classpath:changeString.ktr", "/tmp/string.test.out.txt");
        assert  expectedOutput.contains("Goo; 3");
    }

    private String runTransformation(String resourceLocation, String expectedOutputFilePath) throws Exception {
        File expectedOutputFile =  new File(expectedOutputFilePath);
        expectedOutputFile.delete();

        File ktrFile = ResourceUtils.getFile(resourceLocation);
        System.out.println("path is :" + ktrFile.getAbsolutePath());

        String args[] = {ktrFile.getAbsolutePath()};
        org.pentaho.kettle.engines.storm.KettleStorm.main(args);

        return FileUtils.readFileToString(expectedOutputFile, "utf-8");
    }
}

