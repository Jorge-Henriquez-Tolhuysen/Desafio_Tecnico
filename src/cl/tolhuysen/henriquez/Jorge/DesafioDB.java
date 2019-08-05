package cl.tolhuysen.henriquez.Jorge;

import java.sql.*;
import java.util.ArrayList;

public class DesafioDB {
	private static final String connstr  = "jdbc:oracle:thin:@//192.168.1.253:1521/sistemas.localdomain";
	private static final String user     = "DESAFIO";
	private static final String password = "DesafiO.123456";
	
	public static String getPassword(String user) {
		String password = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT PASSWORD FROM USERS WHERE UPPER(USERID) = ?");
				stmt.setString(1, user.toUpperCase());
				ResultSet rs=stmt.executeQuery();
				if (rs.next()) {
					password = rs.getString(1);  			  
				}
				rs.close();
				con.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password;
	}
	
	public static User getUserData(String userEmail) {
		DesafioDB.User usr = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT USERID, FNAME, LNAME, PASSWORD, ISBORROWER, ISLENDER FROM USERS WHERE UPPER(EMAIL) = ?");
				stmt.setString(1, userEmail.toUpperCase());
				ResultSet rs=stmt.executeQuery();
				if (rs.next()) {
					usr            = new DesafioDB.User();
					usr.id         = rs.getInt(1);
					usr.eMail      = userEmail;
					usr.fName      = rs.getString(2);
					usr.lName      = rs.getString(3);
					usr.password   = rs.getString(4);
					usr.isBorrower = rs.getString(5).contentEquals("S");
					usr.isLender   = rs.getString(6).contentEquals("S");
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usr;
	}
	
	public static int getAmountNeeded(int userId) {
		int amounNeeded = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT NVL(SUM(MONEY),0) AS AMOUNTNEEDED FROM BORROWDATA WHERE USERID = ?");
				stmt.setInt(1, userId);
				ResultSet rs=stmt.executeQuery();
				if (rs.next()) {
					amounNeeded = rs.getInt(1);
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return amounNeeded;
	}
	
	public static int getAmountRaised(int userId) {
		int amountRaised = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT NVL(SUM(MONEY),0) AS AMOUNTRAISED FROM LEDGER WHERE BORROWERID = ?");
				stmt.setInt(1, userId);
				ResultSet rs=stmt.executeQuery();
				if (rs.next()) {
					amountRaised = rs.getInt(1);
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return amountRaised;
	}
	
	public static int getAcountBalance(int userId) {
		int accountBalance = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT (LN.MONEY - (SELECT NVL(SUM(LG.MONEY),0) FROM LEDGER LG WHERE LG.LENDERID = LN.USERID)) AS ACCOUNTBALANCE FROM LENDDATA LN WHERE LN.USERID = ?");
				stmt.setInt(1, userId);
				ResultSet rs=stmt.executeQuery();
				if (rs.next()) {
					accountBalance = rs.getInt(1);
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accountBalance;
	}
	
	public static ArrayList<Lent> getLentsToBorrower(int userId) {
		ArrayList<Lent> lents = new ArrayList<Lent>();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT L.LEDGERID, L.LENDERID, U.FNAME, U.LNAME, U.EMAIL, L.MONEY FROM LEDGER L INNER JOIN USERS U ON L.LENDERID = U.USERID WHERE L.BORROWERID = ?");
				stmt.setInt(1, userId);
				ResultSet rs=stmt.executeQuery();
				while (rs.next()) {
					Lent l = new Lent();
					l.ledgerId   =  rs.getInt(1);
					l.lenderId   =  rs.getInt(2);
					l.fName      =  rs.getString(3);
					l.lName      =  rs.getString(4);
					l.eMail      =  rs.getString(5);
					l.amountLent =  rs.getInt(6);
					lents.add(l);
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lents;
	}
	
	public static ArrayList<UserInNeedAndLent> getLentsByLender(int userId) {
		ArrayList<UserInNeedAndLent> lents = new ArrayList<UserInNeedAndLent>();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt=con.prepareStatement("SELECT L.BORROWERID, U.FNAME, U.LNAME, B.NEEDMONEYFOR, B.DESCRIPTION, (SELECT SUM(MONEY) FROM BORROWDATA BD WHERE BD.USERID = L.BORROWERID) AS NEEDED, (SELECT SUM(LG.MONEY) FROM LEDGER LG WHERE LG.BORROWERID = L.BORROWERID)AS RAISED, SUM(L.MONEY) AS LENT FROM LEDGER L INNER JOIN USERS U ON L.BORROWERID = U.USERID INNER JOIN BORROWDATA B ON L.BORROWERID = B.USERID WHERE L.LENDERID = ? GROUP BY L.BORROWERID, U.FNAME, U.LNAME, B.NEEDMONEYFOR, B.DESCRIPTION");
				stmt.setInt(1, userId);
				ResultSet rs=stmt.executeQuery();
				while (rs.next()) {
					UserInNeedAndLent l = new UserInNeedAndLent();
					l.fName        =  rs.getString(2);
					l.lName        =  rs.getString(3);
					l.needMoneyFor =  rs.getString(4);
					l.description  =  rs.getString(5);
					l.amountNeeded =  rs.getInt(6);
					l.amountRaised =  rs.getInt(7);
					l.amountLent    =  rs.getInt(8);
					lents.add(l);
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lents;
	}
	
	public static ArrayList<UserInNeed> getUsersInNeed() {
		ArrayList<UserInNeed> usersInNeed = new ArrayList<UserInNeed>();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				Statement stmt=con.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT B.BORROWID, B.USERID, U.FNAME, U.LNAME, B.NEEDMONEYFOR, B.DESCRIPTION, B.MONEY AS NEEDED, (SELECT NVL(SUM(L.MONEY),0) FROM LEDGER L WHERE L.BORROWERID = B.USERID ) AS RISED FROM BORROWDATA B INNER JOIN USERS U ON B.USERID = U.USERID");
				while (rs.next()) {
					UserInNeed u = new UserInNeed();
					u.id           = rs.getInt(2);
					u.fName        = rs.getString(3);
					u.lName        = rs.getString(4);
					u.needMoneyFor = rs.getString(5);
					u.description  = rs.getString(6);
					u.amountNeeded = rs.getInt(7);
					u.amountRaised = rs.getInt(8);
					usersInNeed.add(u);
				}
				rs.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usersInNeed;
	}

	public static int createUser(String userFistName,
			                     String userLastName,
			                     String userEmail,
			                     String userPassword,
			                     boolean userIsLender,
			                     boolean userIsBorrower) {
		int retval;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt;
				ResultSet rs;
				stmt=con.prepareStatement("SELECT COUNT(EMAIL) FROM USERS WHERE UPPER(EMAIL) = ?");
				stmt.setString(1, userEmail.toUpperCase());
				rs=stmt.executeQuery();
				rs.next();
				int count = rs.getInt(1);
				rs.close();
				if (count==0) {
					stmt=con.prepareStatement("INSERT INTO USERS(EMAIL, PASSWORD, FNAME, LNAME, ISLENDER, ISBORROWER) VALUES(?, ?, ?, ?, ?, ?)");
					stmt.setString(1, userEmail);
					stmt.setString(2, userPassword);
					stmt.setString(3, userFistName);
					stmt.setString(4, userLastName);
					stmt.setString(5, (userIsLender)?"S":"N");
					stmt.setString(6, (userIsBorrower)?"S":"N"); 
					if (stmt.executeUpdate()!=0) {
						stmt=con.prepareStatement("SELECT USERID FROM USERS WHERE UPPER(EMAIL) = ?");
						stmt.setString(1, userEmail.toUpperCase());
						rs=stmt.executeQuery();
						rs.next();
						retval = rs.getInt(1);
						rs.close();
					}
					else {
						retval = -2;
					}	
				}
				else {
					retval = -10;
				}
				con.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retval = -3;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retval = -4;
		}
		return retval;
	}
	
	
	public static int saveBorrowData(int    borrowerId,
            						 String borrowerNeedMoneyFor,
            						 String borrowerDescription,
            						 int    borrowerMoney) {
		int retval;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt;
				stmt=con.prepareStatement("INSERT INTO BORROWDATA(USERID, NEEDMONEYFOR, DESCRIPTION, MONEY) VALUES(?, ?, ?, ?)");
				stmt.setInt(1, borrowerId);
				stmt.setString(2, borrowerNeedMoneyFor);
				stmt.setString(3, borrowerDescription);
				stmt.setInt(4, borrowerMoney);
				retval = stmt.executeUpdate();
				con.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retval = -1;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retval = -2;
		}
		return retval;
	}
	

	public static int saveLendData(int lenderId,
			 					   int lenderMoney) {
		int retval;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt;
				stmt=con.prepareStatement("INSERT INTO LENDDATA(USERID, MONEY) VALUES(?, ?)");
				stmt.setInt(1, lenderId);
				stmt.setInt(2, lenderMoney);
				retval = stmt.executeUpdate();
				con.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retval = -1;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retval = -2;
		}
		return retval;
	}
	
	public static int registerLentMoney(int lenderId,
										 int borrowerId,
										 int money) {
		int retval = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con;
			try {
				con = DriverManager.getConnection(connstr, user, password);
				PreparedStatement stmt;
				stmt=con.prepareStatement("INSERT INTO LEDGER(LENDERID, BORROWERID, MONEY) VALUES(?, ?, ?)");
				stmt.setInt(1, lenderId);
				stmt.setInt(2, borrowerId);
				stmt.setInt(3, money);
				retval = stmt.executeUpdate();
				con.close(); 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retval = -1;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retval = -2;
		}
		return retval;
	}
	
	public static class User {
		public int     id;
		public String  fName;
		public String  lName;
		public String  eMail;
		public String  password;
		public boolean isLender;
		public boolean isBorrower;
	}
	
	public static class UserInNeed {
		public int     id;
		public String  fName;
		public String  lName;
		public String  needMoneyFor;
		public String  description;
		public int     amountNeeded;
		public int     amountRaised;
	}
	
	public static class UserInNeedAndLent extends  UserInNeed {
		public int     amountLent;
	}
	
	public static class Lent {
		public int    ledgerId;
		public int    lenderId;
		public String fName;
		public String lName;
		public String eMail;
		public int    amountLent;
	}
	
	
}
