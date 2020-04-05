package view;

import java.io.IOException;
import java.util.Scanner;


import negocio.Caixa;

public class Tela {
	String TITULO = "PAYFAST"; //Titulo do sistema
	String tagSair, menu, msgSaldo, msgValor; //mensagens apresentadas em tela
	float saldo, valor; //variaveis para guardar os valores de interação com o usuario
	int tela; //controla qual tela será apresentada (controler)
	
	Caixa cx;
	
	public Tela() throws IOException, InterruptedException {
		//inicializa as variaveis
		//chama o medoto inicializar a leitora do negocio
		cx = new Caixa();
		cx.iniciaLeitora();
		tela = 0; //Menu principal
		//tela comeca no menu inicial
		telaMenu();
	}
	
	//Monta a tela inicial, omenu do sistema.
	public void telaMenu() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		String tela = "\t\t\t\t\t" + TITULO + "\n\n\n";
		tela += " 1 - Deposito\n";
		tela += " 2 - Comprar\n";
		tela += " 3 - Inicializar\n";
		tela += "-1 - Sair\n\n\n";
		tela += "opcao: ";
		System.out.println(tela);
		//fica aguardando retorno do usuario
		int opcao = new Scanner(System.in).nextInt();
		
		//redireciona o usuario para aguardando com o parametro da tela desejada ou sai
		if (opcao != -1)
			telaAguardandoCartao(opcao);
		else
			//-->senao terminar programa
			System.exit(0);		
	}

	//Monta a tela de deposito e apresenta ao usuario
	public void telaDeposito() throws IOException, InterruptedException{
		int valor = 0;
		while(valor != -1) {
			//mostra a tela de deposito
			saldo = cx.leSaldo();
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			
			String tela = "informe -1 para voltar ao menu principal.\n\n";
			tela += "\t\t\t\t\t" + TITULO + "\n\n\n";
			tela += " Cartao ID: " + cx.getCartao().getId() + "\n";
			tela += " Saldo Atual: " + saldo + "\n";
			tela += " Informe o valor a ser creditado: ";
			System.out.println(tela);
			//fica aguardando retorno do usuario
			valor = new Scanner(System.in).nextInt();
			if (valor != -1) 
				//chama o metodo do pacote negocio de deposito
				cx.depositaCartao(saldo, valor);
		}
		//volta menu
		telaMenu();		
	}

	//Monta a tela de compra e apresenta ao usuario
	public void telaCompra() throws IOException, InterruptedException{
		int valor = 0;
		String retorno = "";
		while(valor != -1) {
			//mostra a tela de compra
			saldo = cx.leSaldo();
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			
			String tela = "informe -1 para voltar ao menu principal.\n\n";
			tela += "\t\t\t\t\t" + TITULO + "\n" + retorno + "\n";			
			tela += " Cartao ID: " + cx.getCartao().getId() + "\n";
			tela += " Saldo Atual: " + saldo + "\n";
			tela += " Informe o valor a ser debitado: ";
			System.out.println(tela);
			//fica aguardando retorno do usuario	
			valor = new Scanner(System.in).nextInt();
			if (valor != -1) {
				retorno = "";
				//chama o metodo do pacote negocio de compra
				retorno = cx.debitaCartao(saldo, valor);
			}
		}
		//volta menu
		telaMenu();	
	}

	//Monta a tela de aguardando cartao e apresenta ao usuario. Ela sempre eh chamada
	//antes de mostrar as telas de compra ou deposito 
	public void telaAguardandoCartao(int opcao) throws IOException, InterruptedException{
		String retorno = "";

		//fica aguardando o cartao interagir com leitora
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		String tela = "\t\t\t\t\t" + TITULO + "\n\n\n";
		tela += "Aguardando cartao...";
		System.out.println(tela);
			
		retorno = cx.aguardaCartao(opcao);
		
		if (retorno.equals("")) {			
			//e então passa para a tela de destino
			if (opcao == 1) 
				telaDeposito();
			else if(opcao == 2)
				telaCompra();
			else if(opcao == 3)
				telaInicializar();
		}
		else {
			System.out.println(retorno);
			new ProcessBuilder("cmd", "/c", "pause").inheritIO().start().waitFor();
			telaMenu();
		}
	}
	
	private void telaInicializar() throws InterruptedException, IOException {
		//TODO TELA DE INICIALIZAÇÃO
		cx.inicializarCartao();		
		MensagemAguarde();		
		MensagemSucesso();
		telaMenu();
	}

	private void MensagemAguarde() throws InterruptedException, IOException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		String tela = "\t\t\t\t\t" + TITULO + "\n\n\n";
		tela += "Gravando cartao, AGUARDE";
		System.out.printf(tela);
		//efeito de tela
		for (int i = 0; i < 3; i++) {
			Thread.sleep(500);
			System.out.printf(".");
		}
	}

	private void MensagemSucesso() throws InterruptedException, IOException {
		String tela;
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		tela = "\t\t\t\t\t" + TITULO + "\n\n\n";
		tela += "Cartao gravado com sucesso!";
		System.out.println(tela);
		new ProcessBuilder("cmd", "/c", "pause").inheritIO().start().waitFor();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		//execução do programa
		//instancia Tela chamando mostraTela
		new Tela();
	}	
}
