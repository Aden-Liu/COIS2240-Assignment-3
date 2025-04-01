import java.util.List;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    private static RentalSystem instance;
    
    private RentalSystem() {}
    
    public static RentalSystem getInstance() {
    	if (instance == null) {
    		instance = new RentalSystem();
    	}
    	
    	return instance;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(boolean onlyAvailable) {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (!onlyAvailable || v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(String id) {
        for (Customer c : customers)
            if (c.getCustomerId() == Integer.parseInt(id))
                return c;
        return null;
    }
    
    public void saveVehicle(Vehicle vehicle) {
    	try {
    		File vehicles = new File("vehicles.txt");
    		if (vehicles.createNewFile()) {
    			System.out.println("New save file created.");
    		}
    		else {
    			System.out.println("Save file already exists.");
    		}
    	} catch (IOException e) {
    		System.out.println("An error occured in creating save file.");
    		e.printStackTrace();
    	}
    	
    	try {
    		FileWriter myWriter = new FileWriter("vehicles.txt", true);
    		myWriter.write(vehicle.getInfo() + "\n");
    		myWriter.close();
    	} catch (IOException e) {
    		System.out.println("An error occured in writing save file.");
    		e.printStackTrace();
    	}
    	
    }
    
    public void saveCustomer(Customer customer) {
    	try {
    		File customers = new File("customers.txt");
    		if (customers.createNewFile()) {
    			System.out.println("New save file created.");
    		}
    		else {
    			System.out.println("Save file already exists.");
    		}
    	} catch (IOException e) {
    		System.out.println("An error occured in creating save file.");
    		e.printStackTrace();
    	}
    	
    	try {
    		FileWriter myWriter = new FileWriter("customers.txt", true);
    		myWriter.write(customer.toString() + "\n");
    		myWriter.close();
    	} catch (IOException e) {
    		System.out.println("An error occured in writing save file.");
    		e.printStackTrace();
    	}
    }
    
    public void saveRecord(RentalRecord record) {
    	try {
    		File records = new File("rental_records.txt");
    		if (records.createNewFile()) {
    			System.out.println("New save file created.");
    		}
    		else {
    			System.out.println("Save file already exists.");
    		}
    	} catch (IOException e) {
    		System.out.println("An error occured in creating save file.");
    		e.printStackTrace();
    	}
    	
    	try {
    		FileWriter myWriter = new FileWriter("rental_records.txt", true);
    		myWriter.write(record.toString() + "\n");
    		myWriter.close();
    	} catch (IOException e) {
    		System.out.println("An error occured in writing save file.");
    		e.printStackTrace();
    	}
    }
    
}