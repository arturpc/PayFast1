package cartao;

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
		
		//sobe a chave de autenticação de cartao
		byte[] bSobe;

		bSobe = new byte[]{(byte) 0xFF, (byte) 0x82, (byte) 0x20/*nao volatil*/,
                               (byte) 0x00 /*chave A*/, (byte) 0x06,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF //CHAVE
		};
		
		//TODO Fazer o teste se deu certo então continua
		send(bSobe,cch);
		return getId();
	}
	
	public void autenticaCartao(int bloco){
		//autentica o cartao conforme parametros
		byte[] bAute;

		bAute = new byte[]{(byte) 0xFF, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x05,
						(byte) 0x01, (byte) 0x00, (byte) bloco, (byte) 0x60 /*A*/, (byte) 0x00
		};
		
		retorno = send(bAute,cch);
		if (!retorno.equals("90 00 ")) {
			System.out.println("Erro de autenticacao!");
		}
	}
	
	public String leCartao(int bloco){
		//le cartao conforme parametros
		byte[] bLe;

		bLe = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00, (byte) bloco, (byte) 0x00};
		
		//TODO Fazer o teste se deu certo então continua
		retorno = send(bLe,cch);
		
		return retorno;
	}
	
	public void gravaCartao(int bloco, byte[] bData){
		//grava cartao conforme parametros
		byte[] bGrava;

		bGrava = new byte[]{(byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) bloco, (byte) 0x10, //PARAMETROS
				
												bData[0],bData[1],bData[2],bData[3],bData[4], //VALOR
												
												(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,		  //VAZIO
												(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,
												(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF
		};
		
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
	


   public String send(byte[] cmd, CardChannel channel) {

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
}
