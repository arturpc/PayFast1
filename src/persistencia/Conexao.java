package persistencia;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexao {
	Connection con;
	public Conexao() {
		try {
			con = DriverManager.getConnection("jdbc:postgresql://192.168.206.129:5432/payfast","usr_payfast","payfast123");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getCon() {
		return con;
	}
	public void setCon(Connection con) {
		this.con = con;
	}	
}
