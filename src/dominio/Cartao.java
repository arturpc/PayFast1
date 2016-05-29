package dominio;

public class Cartao {
	private long id;
	private float saldo;

	public Cartao() {
		// TODO Auto-generated constructor stub
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
	
}
