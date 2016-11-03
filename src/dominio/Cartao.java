package dominio;

import java.sql.ResultSet;
import java.sql.SQLException;

import persistencia.Conexao;

public class Cartao {
	private long id;
	private float saldo;
	private long idNegocial;
	private int transacao;
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

	public long getIdNegocial() {
		return idNegocial;
	}

	public void setIdNegocial(long idNegocial) {
		this.idNegocial = idNegocial;
	}

	public long getTransacao() {
		return transacao;
	}

	public void setTransacao(int transacao) {
		this.transacao = transacao;
	}

	public long inicializaCartao(long id_cartao) {
		System.out.println(id_cartao);
		setId(id_cartao);
		try {
			c.getCon().createStatement().executeUpdate("INSERT INTO public.\"TB_CARTAO\" (\"ID_CARTAO\") VALUES (" + id_cartao + ")");
			ResultSet rs = c.getCon().createStatement().executeQuery("SELECT \"ID_NEGOCIAL\" FROM public.\"TB_CARTAO\" WHERE \"ID_CARTAO\" = " + id_cartao + ";");
			
			setIdNegocial(0);
			setTransacao(0);
			rs.next();
			setIdNegocial(rs.getLong("ID_NEGOCIAL"));

			if (getIdNegocial() == 0) 
				throw new SQLException("Nao encontrou o cartao no banco de dados");			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getIdNegocial();
	}
	
}
