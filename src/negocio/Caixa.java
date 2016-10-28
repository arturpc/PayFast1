package negocio;

import java.io.IOException;

import cartao.OperacoesEspecificas;
import dominio.Cartao;

public class Caixa {
	OperacoesEspecificas opEsp;
	//Cria um cartao
	Cartao cartao;
	float valor; //valor que est� sendo operado
	public Caixa() {
		//inicializa as variaveis
		cartao = new Cartao();
		valor = 0; //come�a zerado
	}
	
	public void iniciaLeitora(){
		//instancia a classe operacao especifica do pacote cartao
		opEsp = new OperacoesEspecificas();
	}
	
	public String aguardaCartao(int opcao){
		boolean testeAutenticado  = false;
		String retorno = "";
		opEsp.aguardaCartao(); //aguardando qualquer cartao
		
		if(opcao == 3) //inicializacao
			testeAutenticado = opEsp.testeAutenticacaoNaoInicializado();
		else
			testeAutenticado = opEsp.testeAutenticacaoInicializado();
		
		if (testeAutenticado) {
			cartao.setId(opEsp.getId());
			retorno = "";
		}
		else {
			cartao.setId(0);
			retorno = "Cart�o inv�lido!";
		}
		
		return retorno;			
	}
	
	public float leSaldo(){
		//chama a fun��o le saldo
		return opEsp.leSaldo();
	}
	
	public void depositaCartao(float saldo, int valor2) throws IOException{
		//faz as operacoes e regras de deposito
		cartao.setSaldo(saldo + valor2);
		//chama a fun��o de gravarSaldo
		opEsp.gravaSaldo(cartao.getSaldo());
	}

	public String debitaCartao(float saldo, float valor2) throws IOException{
		//faz as operacoes e regras de debito
		if (valor2 > saldo)
			return "Saldo insufici�nte"; //Saldo insufic�nte
		else {
			cartao.setSaldo(saldo - valor2);
			//chama a fun��o de gravarSaldo
			opEsp.gravaSaldo(cartao.getSaldo());
			return "";
		}		
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public void inicializarCartao() throws IOException {
		cartao.inicializaCartao(cartao.getId());
		opEsp.inicializaCartao();
	}
}
