public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { AVAILABLE, RESERVED, RENTED, MAINTENANCE, OUTOFSERVICE }

    public Vehicle(String make, String model, int year) {
    	this.make = capitalize(make);
    	this.model = capitalize(model);   	
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
        if (!isValidPlate(plate))
        	throw new IllegalArgumentException("Invalid license plate.");
        
        licensePlate = plate;
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }
    
    private String capitalize(String input) {
    	if (input == null || input.isEmpty())
    		return null;
    	else
    		input = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    	
    	return input;
    }
    
    private boolean isValidPlate(String plate) {
    	if (plate == null || plate.isEmpty())
    		return false;
    	
    	if (plate.matches("[a-zA-Z]{3}\\d{3}")) //[a-zA-Z] means every letter, \\d means every integer. {3} means exactly 3 preceeding
    		return true;
    	
    	return false;
    }

}
