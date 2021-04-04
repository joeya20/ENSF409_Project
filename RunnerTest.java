import org.junit.*;
import static org.junit.Assert.*;

public class PetTest {
    
    public PetTest() {
    }
    
    @Test
    public void testCalculateAge_BirthdayPast() {
        
        int[] birthDate = new int[]{2011,1,1};
        
        Pet petDog = new Pet("Pongo", "Dog", "Dalmatian", 4, "Spotted", "Fur", birthDate);
        
        System.out.println("calculateAge");
        int expResult = 10;
        int result = petDog.calculateAge();
        assertEquals("Calculated age was incorrect: ", expResult, result);
    }


    @Test
    public void testCalculateAge_BirthdayFuture() {
        
        int[] birthDate = new int[]{2011,7,1};
        
        Pet petDog = new Pet("Pongo", "Dog", "Dalmatian", 4, "Spotted", "Fur", birthDate);
        
        System.out.println("calculateAge");
        int expResult = 9;
        int result = petDog.calculateAge();
        assertEquals("Calculated age was incorrect: ", expResult, result);
    }

    @Test
    public void testCalculateAge_BirthdayToday() {
        
        int[] birthDate = new int[]{2011,2,28};
        
        Pet petDog = new Pet("Pongo", "Dog", "Dalmatian", 4, "Spotted", "Fur", birthDate);
        
        System.out.println("calculateAge");
        int expResult = 10;
        int result = petDog.calculateAge();
        assertEquals("Calculated age was incorrect: ", expResult, result);
    }
	
}
