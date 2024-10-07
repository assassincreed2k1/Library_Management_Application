package com.library;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {
    
    @Test
    public void testHelloWorld() {
        HelloWorld hello = new HelloWorld();
        String res = hello.hello();
        assertEquals("HelloWorld", res);
    }
}
