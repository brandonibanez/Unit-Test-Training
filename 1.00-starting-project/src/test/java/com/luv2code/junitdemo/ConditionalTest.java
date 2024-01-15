package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTest {

    @Test
    @Disabled("Don't run for the meantime")
    void basicTest() {

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowsOnly() {

    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly() {

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testForLinuxOnly() {

    }

    @Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void testForMacAndWindowsOnly() {

    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testForJava17() {

    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_13,max=JRE.JAVA_17)
    void testForJava13To17() {

    }

    @Test
    @EnabledIfEnvironmentVariable(named="brandon_env", matches="DEV")
    void testOnlyForDevEnvironment() {

    }

    @Test
    @EnabledIfSystemProperty(named="brandon_prop", matches="CI_CD_DEPLOY")
    void testOnlyForSystemProperty() {

    }


}
