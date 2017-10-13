package sample;

import groovy.util.GroovyTestCase;

/**
 * Created by kis on 23/04/2017.
 */
public class TeacherTest extends GroovyTestCase {
    public void testInit() throws Exception {
        Teacher teacher = new Teacher("test name", "0900", "1900");
    }
}