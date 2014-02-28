import java.sql.*;
import java.util.MissingFormatArgumentException;

public class Connect {
	private Connection con = null;
	private final String url = "jdbc:sqlserver://";
	private final String serverName = "fourwaylo.com";
	private final String portNumber = "8889";
	private final String databaseName = "csproj";
	private final String userName = "csproj";
	private final String password = "DoYourHomework";
	
	private final String selectMethod = "cursor";
	
	private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
	private static final String GARAGE_TABLE = "cillian.Garage";
	private static final String GARAGE_PARAMS = "VehicleType, Make, Model, Year";
	
	public Connect() {}
	
	private String getConnectionURL() {
		return String.format("%s%s:%s;databaseName=%s;selectMethod=%s;", url, serverName, portNumber, databaseName, selectMethod);
	}
	
	private Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerConnection");
			con = DriverManager.getConnection(getConnectionURL(), userName, password);
			if(con != null) System.out.println("Connection Successful!");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error Trace in getConnection(): " + e.getMessage());
		}
		return con;
	}
	
	public void displayDbProperties() {
		DatabaseMetaData dm = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			if(con != null) {
				dm = con.getMetaData();
				System.out.println("Driver Information");
				System.out.println("\tDriver Name: " + dm.getDriverName());
				System.out.println("\tDriver Version: " + dm.getDriverVersion());
				System.out.println("\nDatabase Information");
				System.out.println("\tDatabase Name: " + dm.getDatabaseProductName());
				System.out.println("\tDatabase Version: " + dm.getDatabaseProductVersion());
				System.out.println("\nAvailable Catalogs");
				rs = dm.getCatalogs();
				while(rs.next()) {
					System.out.println("\tcatalog: " + rs.getString(1));
				}
			}
			rs.close();
			rs = null;
			closeConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String repeatString(String s, int n) {
		return new String(new char[n]).replace("\0", s);
	}
	
	private boolean addVehicle(String make, String model, int year, int radius, VehicleType type, int... vehicleArgs) throws SQLException {
		if(!type.verifyArgs(vehicleArgs)) return false;
		con = this.getConnection();
		if(con != null) {
			Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String garageQuery = INSERT_TEMPLATE;
			String garageQueryArgs = String.format("%d, '%s', '%s', %d, %d", type.typeID, make, model, year, radius);
			String vehicleQuery = type.createInsertQuery("SELECT SCOPE_IDENTITY()", vehicleArgs);
			garageQuery = String.format(garageQuery, GARAGE_TABLE, GARAGE_PARAMS, garageQueryArgs);
			statement.execute(garageQuery + vehicleQuery);
			statement.close();
		}
		closeConnection();
		return true;
	}
	
	private ResultSet query(String query) throws SQLException {
		con = this.getConnection();
		ResultSet rs = null;
		if(con != null) {
			Statement statement = con.createStatement();
			rs = statement.executeQuery(query);
		}
		return rs;
	}
	
	private void closeConnection() {
		try {
			if(con != null) {
				con.close();
				con = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Connect dbTest = new Connect();
		//dbTest.displayDbProperties();
		dbTest.addVehicle("Toyota", "Notatoyota", 2002, 187, VehicleType.GAS, 7, 140, 53, 6000);
		Connection con = dbTest.getConnection();
		Statement statement = con.createStatement();
		statement.executeQuery("SELECT 0; SELECT * FROM cillian.Garage INNER JOIN cillian.GasGarage ON Garage.ID = GasGarage.ID;");
		ResultSet rs;
		while(statement.getMoreResults()) {
			rs = statement.getResultSet();
				printTable(rs);
			rs.close();
			System.out.println();
			System.out.println();
		}
		rs = null;
		dbTest.closeConnection();
	}
	
	public static void printTable(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		for(int i = 1; i <= columns; i++) {
			System.out.print(md.getColumnName(i) + "\t");
		}
		while(rs.next()) {
			System.out.println();
			for(int i = 1; i <= columns; i++) {
				System.out.print(rs.getString(i) + "\t");
			}
		}
		md = null;
	}
	
	private static enum VehicleType {
		GAS(0, "cillian.GasGarage", "ID, Doors, FuelCapacity, FuelEfficiency, Mileage", 4),
		ELECTRIC(1, "cillian.ElectricGarage", "ID, Doors, FuelCapacity, FuelEfficiency, Mileage", 4),
		BOAT(2, "cillian.BoatGarage", "ID, Range", 2);
		
		VehicleType(int typeID, String table, String params, int argsLength) {
			this.typeID = typeID;
			this.params = params;
			this.table = table;
			this.argsLength = argsLength;
		}
		
		private final String params;
		private final String table;
		private final int typeID;
		private final int argsLength;
		
		public String createInsertQuery(int ID, int... args) {
			return createInsertQuery(Integer.toString(ID), args);
		}
		
		public String createInsertQuery(String ID, int... args) {
			String query = INSERT_TEMPLATE;
			String queryArgs = "%s" + repeatString(", %d", args.length);
			queryArgs = queryArgs.substring(0, queryArgs.length() - 2);
			System.out.println(queryArgs);
			try {
				return String.format(query, table, params, args);
			} catch(MissingFormatArgumentException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		public boolean verifyArgs(int... args) {
			return args.length == argsLength;
		}
	}
}
