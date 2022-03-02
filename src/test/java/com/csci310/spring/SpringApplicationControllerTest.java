package com.csci310.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringApplicationControllerTest {

    @Test
    public void testApplicationContextLoads() {
        APIApplication.main(new String[] {});
    }
}
