package fhw.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author fwelland
 */
public class ICSRateCardTest
{
    
    public ICSRateCardTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testCompute()
    {
        System.out.println("compute");
        Integer a = null;
        Integer b = null;
        ICSRateCard instance = new ICSRateCard();
        Integer expResult = null;
        Integer result = instance.compute(a, b);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
