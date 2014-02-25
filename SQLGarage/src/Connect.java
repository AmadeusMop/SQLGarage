import java.sql.*;

public class Connect {
	private Connection con = null;
	private final String url = "jdbc:sqlserver://";
	private final String serverName = "fourwaylo.com";
	private final String portNumber = "8889";
	private final String databaseName = "csproj";
	private final String userName = "csproj";
	private final String password = "DoYourHomework";
	
	private final String selectMethod = "cursor";
	
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
		ResultSet rs = dbTest.query("SELECT * FROM cillian.CGarage INNER JOIN cillian.CGasGarage ON CGarage.ID = CGasGarage.ID;");
		if(rs != null) {
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			for(int i = 1; i <= columns; i++) {
				System.out.print(md.getColumnName(i) + "\t");
			}
			while(rs.next()) {
				System.out.println();
				for(int i = 1; i <= columns; i++)
					System.out.print(rs.getString(i) + "\t");
			}
			rs.close();
			rs = null;
		}
		dbTest.closeConnection();
	}
}
