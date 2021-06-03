package gov.pnnl.svf.test;

import org.junit.Test;

/**
 *
 * @author D3X573
 */
public class PerformanceStatsTest {

    /**
     * Test of write method, of class PerformanceStats.
     */
    @Test
    public void testWrite() {
        PerformanceStats.write("gov.pnnl.svf.test.PerformanceStatsTest.testWrite()", 1, 1);
        PerformanceStats.write("gov.pnnl.svf.test.PerformanceStatsTest.testWrite()", 1, 2);
    }

}
