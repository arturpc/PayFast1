package dominio;

import java.sql.SQLException;

import persistencia.Conexao;

public class Cartao {
	private long id;
	private float saldo;
	Conexao c;

	public Cartao() {
		c = new Conexao();
	}
	
	public Cartao(float saldo) {
		setSaldo(saldo);
	}
	
	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void inicializaCartao(long id_cartao) {
		System.out.println(id_cartao);
		try {
			c.getCon().createStatement().executeUpdate("INSERT INTO public.\"TB_CARTAO\" (\"ID_CARTAO\") VALUES (" + id_cartao + ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
