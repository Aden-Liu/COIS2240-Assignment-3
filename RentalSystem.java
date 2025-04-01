import java.util.List;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    private static RentalSystem instance;
    
    private RentalSystem() {
    	loadData();
    }
    
    public static RentalSystem getInstance() {
    	if (instance == null) {
    		instance = new RentalSystem();
    	}
    	
    	return instance;
    }

    public boolean addVehicle(Vehicle vehicle) {
    	if (findVehicleByPlate(vehicle.getLicensePlate()) != null)
    		return false;
    	
        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    public boolean addCustomer(Customer customer) {
    	if (findCustomerById(Integer.toString(customer.getCustomerId())) != null)
    		return false;
    	
        customers.add(customer);
        saveCustomer(customer);
        return true;
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            saveRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
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
            saveRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
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
    		File vehicles = new File("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\vehicles.txt");
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
    		FileWriter myWriter = new FileWriter("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\vehicles.txt", true);
    		String vehicleType = vehicle.getClass().toString().substring(5);
    		myWriter.write("|" + vehicleType + vehicle.getInfo() + "\n");
    		myWriter.close();
    	} catch (IOException e) {
    		System.out.println("An error occured in writing save file.");
    		e.printStackTrace();
    	}
    	
    }
    
    public void saveCustomer(Customer customer) {
    	try {
    		File customers = new File("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\customers.txt");
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
    		FileWriter myWriter = new FileWriter("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\customers.txt", true);
    		myWriter.write("|" + customer.toString() + "\n");
    		myWriter.close();
    	} catch (IOException e) {
    		System.out.println("An error occured in writing save file.");
    		e.printStackTrace();
    	}
    }
    
    public void saveRecord(RentalRecord record) {
    	try {
    		File records = new File("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\rental_records.txt");
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
    		FileWriter myWriter = new FileWriter("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\rental_records.txt", true);
    		myWriter.write("|" + record.toString() + "\n");
    		myWriter.close();
    	} catch (IOException e) {
    		System.out.println("An error occured in writing save file.");
    		e.printStackTrace();
    	}
    }
    
    private void loadData() {
		Pattern pattern = Pattern.compile("\\|([^|]+)");

    	try {
    		File vehicles = new File("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\vehicles.txt");
    		Scanner myReader = new Scanner(vehicles);
    		Vehicle vehicle;
    		
    		while (myReader.hasNextLine()) {
    			String data = myReader.nextLine().replace(" ", "");
        		Matcher matcher = pattern.matcher(data);
        		List<String> vehicleData = new ArrayList<>();

    			while (matcher.find()) 
    				vehicleData.add(matcher.group(1));
    			
    			String make = vehicleData.get(2);
    			String model = vehicleData.get(3);
    			int year = Integer.parseInt(vehicleData.get(4));
    			
    			if (vehicleData.get(0).equals("Car")) {
        			int numSeats = Integer.parseInt(vehicleData.get(6).substring(6));
    				vehicle = new Car(make, model, year, numSeats);
    			}
    			else if (vehicleData.get(0).equals("Motorcycle")) {
        			boolean hasSidecar = vehicleData.get(6).substring(8) == "Yes" ? true : false;
    				vehicle = new Motorcycle(make, model, year, hasSidecar);
    			}
    			else {
        			double capacity = Double.parseDouble(vehicleData.get(6).substring(14));
    				vehicle = new Truck(make, model, year, capacity);
    			}
    			
				vehicle.setLicensePlate(vehicleData.get(1));
				this.vehicles.add(vehicle);

    		}
    		myReader.close();
    	} catch (FileNotFoundException e) {
    		System.out.println("Save file for vehicle not found");
    	}
    	
    	try {
    		File customers = new File("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\customers.txt");
    		Scanner myReader = new Scanner(customers);
    		
    		while (myReader.hasNextLine()) {
    			String data = myReader.nextLine().replace(" ", "");
    			Matcher matcher = pattern.matcher(data);
    			List<String> customerData = new ArrayList<>();
    			
    			while (matcher.find())
    				customerData.add(matcher.group(1));
    			
    			int Id = Integer.parseInt(customerData.get(0).substring(11));
    			String name = customerData.get(1).substring(5);
    			
    			this.customers.add(new Customer(Id, name));
    		}
    		myReader.close();
    	} catch (FileNotFoundException e) {
    		System.out.println("Save file for customers not found");
    	}
    	
    	try {
    		File records = new File("C:\\Users\\longw\\eclipse-workspace\\Assignment 3\\src\\rental_records.txt");
    		Scanner myReader = new Scanner(records);
    		
    		while (myReader.hasNextLine()) {
    			String data = myReader.nextLine().replace(" ", "");
    			Matcher matcher = pattern.matcher(data);
    			List<String> recordData = new ArrayList<>();
    			
    			while (matcher.find())
    				recordData.add(matcher.group(1));
    			
    			String plate = recordData.get(1).substring(6);
    			String Id = recordData.get(2).substring(11);
    			String type = recordData.get(0);
    			double amount = Double.parseDouble(recordData.get(5).substring(8));
    			LocalDate date = LocalDate.parse(recordData.get(4).substring(5));
    			
    			rentalHistory.addRecord(new RentalRecord(findVehicleByPlate(plate), findCustomerById(Id), date, amount, type));
    			
    			if (type.equals("RENT"))
    				findVehicleByPlate(plate).setStatus(Vehicle.VehicleStatus.RENTED);
    			else if (type.equals("RETURN"))
    				findVehicleByPlate(plate).setStatus(Vehicle.VehicleStatus.AVAILABLE);
    		}
    		
    	} catch (FileNotFoundException e) {
    		System.out.println("Save file for records not found");
    	}
    }
}