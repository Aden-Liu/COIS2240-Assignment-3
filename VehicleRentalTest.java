import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleRentalTest {
	private Vehicle testCarOne;
	private Vehicle testCarTwo;
	private Vehicle testCarThree;
	private Vehicle testCarFour;
	
	private Customer CustomerOne;
	
	private RentalSystem rentalSystem;
	
	@BeforeEach
	public void setUp() {
		testCarOne = new Car("Toyota", "Corolla", 2019, 4);
		testCarTwo = new Car("Honda", "Civic", 2021, 4);
		testCarThree = new Car("Ford", "Focus", 2024, 4);
		testCarFour = new Car("Volkswagen", "Beetle", 2003, 4);
		CustomerOne = new Customer(12345, "Aden");
		rentalSystem = RentalSystem.getInstance();
	}
	
	@Test
	public void testLicensePlateValidation() {
		testCarOne.setLicensePlate("AAA100");
		testCarTwo.setLicensePlate("ABC567");
		testCarThree.setLicensePlate("ZZZ999");
		
		assertTrue(testCarOne.getLicensePlate() != null && !testCarOne.getLicensePlate().isEmpty());
		assertTrue(testCarTwo.getLicensePlate() != null && !testCarTwo.getLicensePlate().isEmpty());
		assertTrue(testCarThree.getLicensePlate() != null && !testCarThree.getLicensePlate().isEmpty());
		
		assertThrows(IllegalArgumentException.class, () -> testCarOne.setLicensePlate(""));
		assertThrows(IllegalArgumentException.class, () -> testCarTwo.setLicensePlate(null));
		assertThrows(IllegalArgumentException.class, () -> testCarThree.setLicensePlate("AAA1000"));
		assertThrows(IllegalArgumentException.class, () -> testCarFour.setLicensePlate("ZZ999"));
	}
	
	@Test
	public void testRentAndReturn() {
		assertTrue(testCarOne.getStatus() == Vehicle.VehicleStatus.AVAILABLE);
		
		rentalSystem.addCustomer(CustomerOne);

		assertTrue(rentalSystem.rentVehicle(testCarOne, CustomerOne, LocalDate.now(), 100));
		assertTrue(testCarOne.getStatus() == Vehicle.VehicleStatus.RENTED);
		assertFalse(rentalSystem.rentVehicle(testCarOne, CustomerOne, LocalDate.now(), 100));
		
		assertTrue(rentalSystem.returnVehicle(testCarOne, CustomerOne, LocalDate.now(), 100));
		assertTrue(testCarOne.getStatus() == Vehicle.VehicleStatus.AVAILABLE);
		assertFalse(rentalSystem.returnVehicle(testCarOne, CustomerOne, LocalDate.now(), 100));
	}
}
