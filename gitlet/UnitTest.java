package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the gitlet package.
 *
 * @author Jeongsu Kim
 */
public class UnitTest {

    /**
     * Run the JUnit tests in the loa package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /**
     * A dummy test to avoid complaint.
     */
    @Test
    public void placeholderTest() {
    }

    @Test
    public void initial() {
        int count = 0;
        for (int k = 1; k < 20; k *= 3) {
            for (int j = 0; j < k; j += 1) {
                count++;
            }
        }
        System.out.println(count);
    }

    @Test
    public void initialcommit() {
        Commit newcommit = new Commit();
        Commit secondcommit = new Commit();
        assertNotEquals(newcommit, secondcommit);
        assertEquals("initial commit", newcommit.message());
        assertEquals("initial commit", secondcommit.message());
    }

    @Test
    public void comparecommit() {
        Commit newcommit = new Commit();
        Commit secondcommit = new Commit();
        assertNotEquals(newcommit, secondcommit);
        assertEquals(secondcommit.message(), newcommit.message());
    }

    @Test
    public void hashtest() {
        Commit newcommit = new Commit();
        Commit secondcommit = new Commit();
        assertEquals(newcommit.hash(), secondcommit.hash());
    }

    @Test
    public void time() {
        Commit newcommit = new Commit();
        Commit secondcommit = new Commit();
        assertEquals(newcommit.timestamp(), secondcommit.timestamp());
    }

    @Test
    public void parent() {
        Commit newcommit = new Commit();
        Commit secondcommit = new Commit();
        assertEquals(newcommit.parent(), secondcommit.parent());
    }

    @Test
    public void itself() {
        Commit newcommit = new Commit();
        Commit secondcommit = new Commit();
        assertNotEquals(newcommit, secondcommit);
    }

    @Test
    public void non() {
        String[] test = new String[]{""};
        Commands comm = new Commands(test);
    }

    @Test
    public void wronginit() {
        String[] test = new String[]{"innit"};
        Commands comm = new Commands(test);
    }

    @Test
    public void blob3() {
        String[] test = new String[]{"add"};
        Commands comm = new Commands(test);
    }

    @Test
    public void blob4() {
        String[] test = new String[]{"commit"};
        Commands comm = new Commands(test);
    }


}


