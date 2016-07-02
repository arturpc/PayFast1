package cartao;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;  

public class OperacaoBasica {
	//variaveis de cartao/leitora
	CardTerminal t;	
	TerminalFactory f;
	List<CardTerminal> ts;
	Card c;
	CardChannel cch;
	
	String resinv;
	
	String retorno; //retorno do send
	
	long id; //id do cartao
	
	public OperacaoBasica() {
		//inicializa as variaveis de leitora/cartao
		t = null;
		//chama o metodo inicializaLeitora		
		iniciaLeitora();
	}
	
	public void iniciaLeitora(){
		//inicializa leitora
		f = TerminalFactory.getDefault();
		try {
			ts = f.terminals().list();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t = (CardTerminal) ts.get(1);
	}

	public long aguardaCartao() {

		//aguarda cartao	
		try {
			t.waitForCardPresent(0);
			c = t.connect("T=1");
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cch = c.getBasicChannel();
		
		//pega o ID
		byte[] bID;

		bID = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00,
                               (byte) 0x00, (byte) 0x00};
		
		send(bID,cch);
		setId(Long.valueOf(resinv.substring(6).replace(" ","").trim(),16));
		
		return getId();
	}
	
	public String autenticaCartao(int bloco){
		//autentica o cartao conforme parametros
		byte[] bAute;

		bAute = new byte[]{(byte) 0xFF, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x05,
						(byte) 0x01, (byte) 0x00, (byte) bloco, (byte) 0x60 /*A*/, (byte) 0x00
		};
		
		retorno = send(bAute,cch);
		if (!retorno.equals("90 00 ")) {
			System.out.println("Erro de autenticacao!");
		}
		return retorno;
	}
	
	public String leCartao(int bloco){
		//le cartao conforme parametros
		byte[] bLe;

		bLe = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00, (byte) bloco, (byte) 0x00};
		
		//TODO Fazer o teste se deu certo então continua
		retorno = sendDEC(bLe,cch);
		
		return retorno;
	}
	
	public void gravaCartao(int bloco, byte[] bData, int indexByte, boolean crypt) throws IOException{
		//grava cartao conforme parametros
		byte[] bDados = new byte[16];
		
		for (int i = 0; i < 16; i++) {
			bDados[i] = (byte) 0xFF;
		}	
		
		if (crypt)
			bData = Enigma.criptografa(bData); //criptografa
		
		int idx = 0;
		for (int i = indexByte; i < indexByte + bData.length; i++) {
			bDados[i] = bData[idx];
			idx++;
		}

		byte[] bGrava = new byte[21];
		
		bGrava[0] = (byte) 0xFF;
		bGrava[1] = (byte) 0xD6;
		bGrava[2] = (byte) 0x00;
		bGrava[3] = (byte) bloco;
		bGrava[4] = (byte) 0x10;

		for (int i = 5; i < 21; i++) {
			bGrava[i] = bDados[i-5];
		}
		
			retorno = send(bGrava,cch);
		
		if (!retorno.equals("90 00 ")) {
			System.out.println("Erro de gravação!");
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	


	public String sendDEC(byte[] cmd, CardChannel channel) {

	       String res = "";

	       byte[] baResp = new byte[258];
	       ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
	       ByteBuffer bufResp = ByteBuffer.wrap(baResp);

	       // output = The length of the received response APDU
	       int output = 0;
	       resinv = "";
	       try {
	           output = channel.transmit(bufCmd, bufResp);   
	           //baResp = Enigma.decriptografa(baResp);  //decriptografa
	       } catch (CardException /*| IOException*/ ex) {
	           ex.printStackTrace();
	       }
	       
	       for (int i = 0; i < output; i++) {
	           res += String.format("%02X ", baResp[i]);
	           // The result is formatted as a hexadecimal integer
	       }

	       for (int i = (output-1); i >= 0; i--) {
	           resinv += String.format("%02X ", baResp[i]);
	           // The result is formatted as a hexadecimal integer
	       }

	       return res;
	}
	
	public String send (byte[] cmd, CardChannel channel) {

	       String res = "";

	       byte[] baResp = new byte[258];
	       ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
	       ByteBuffer bufResp = ByteBuffer.wrap(baResp);

	       // output = The length of the received response APDU
	       int output = 0;
	       resinv = "";
	       try {
	           output = channel.transmit(bufCmd, bufResp);   
	       } catch (CardException ex) {
	           ex.printStackTrace();
	       }
	       
	       for (int i = 0; i < output; i++) {
	           res += String.format("%02X ", baResp[i]);
	           // The result is formatted as a hexadecimal integer
	       }

	       for (int i = (output-1); i >= 0; i--) {
	           resinv += String.format("%02X ", baResp[i]);
	           // The result is formatted as a hexadecimal integer
	       }

	       return res;
	   }
   
   public boolean autentica(byte[] chave){
	   byte[] bSobe;
	   
	   boolean sobchave = false;
	   
		bSobe = new byte[11];
		bSobe[0] = (byte) 0xFF;
		bSobe[1] = (byte) 0x82;
		bSobe[2] = (byte) 0x20/*nao volatil*/;
		bSobe[3] = (byte) 0x00 /*chave A*/;
		bSobe[4] = (byte) 0x06;		
		int i = 5;
		for (byte bt : chave) {
			bSobe[i] = bt;
			i++;
		}		
		
		//TODO Fazer o teste se deu certo então continua
		String resultado = send(bSobe,cch);
		
		if (resultado.equals("90 00 ") || resultado.equals("91 00 ")) 
			sobchave = true;
		else
			sobchave = false;	
		
		if (sobchave) {
			resultado = autenticaCartao(36);
			if (resultado.equals("90 00 ") || resultado.equals("91 00 "))
				return true;
			else
				return false;
		}
		else
			return false;
		
   }
   
}
