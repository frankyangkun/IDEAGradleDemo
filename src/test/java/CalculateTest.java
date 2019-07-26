
import org.junit.Test;


import static org.junit.Assert.*;

public class CalculateTest
{
    @Test
    public void testAdd()
    {
        assertEquals(3,new Calculate().add(1,2));
    }

    @Test
    public void testMinus()
    {
        assertEquals(1,new Calculate().minus(3,2));
    }
}