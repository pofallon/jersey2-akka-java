package com.ofallonfamily.jersey2akka;

import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertTrue;

/**
 * Test to verify that the src/main/webapp directory exists.
 * This addresses the issue where the Maven Tomcat plugin was failing
 * with IllegalArgumentException due to missing webapp directory.
 */
public class WebappDirectoryTest {

    @Test
    public void testWebappDirectoryExists() {
        // Verify that src/main/webapp directory exists
        File webappDir = new File("src/main/webapp");
        assertTrue("src/main/webapp directory should exist for WAR packaging", 
                   webappDir.exists() && webappDir.isDirectory());
    }
}