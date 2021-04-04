import org.junit.*;
import static org.junit.Assert.*;

public class PetTest {
    
    public PetTest() {
    }
    
    @Test
    public void test() {
        
        int[] birthDate = new int[]{2011,1,1};
        
        Pet petDog = new Pet("Pongo", "Dog", "Dalmatian", 4, "Spotted", "Fur", birthDate);
        
        System.out.println("calculateAge");
        int expResult = 10;
        int result = petDog.calculateAge();
        assertEquals("Calculated age was incorrect: ", expResult, result);
    }	
}
