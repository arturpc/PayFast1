package cartao;

import java.io.IOException;

public class OperacoesEspecificas {
	//variavel de operacoes basicas
	  //converção para hexa
	  private static final int sizeOfIntInHalfBytes = 10;
	  private static final int numberOfBitsInAHalfByte = 4;
	  private static final int halfByte = 0x0F;
	  private static final char[] hexDigits = { 
	    '0', '1', '2', '3', '4', '5', '6', '7', 
	    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	  };
	
	OperacaoBasica opBas;
	public OperacoesEspecificas() {
		//instancia operacoes basicas
		opBas = new OperacaoBasica();
	}
	
	public float leSaldo(){
		//autentica no setor 10
		int bloco = 36;
		opBas.autenticaCartao(bloco);
		//passa o endereco da leitura do saldo chamando a funcao basica de leitura
		String r = opBas.leCartao(bloco);

		
		if(r.substring(0,15).replace(" ","").trim().equals("FFFFFFFFFF"))
			return 0;		
		else
			return Long.valueOf(r.substring(0,15).replace(" ","").trim(),16);
	}	
	
	public void gravaSaldo(double d) throws IOException{
		//autentica setor
		//autentica no setor 10
		int bloco = 36;
		opBas.autenticaCartao(bloco);
		//passa o endereco da gravaçao do saldo chamando a funcao basica de leitura
		 double f = d;
		 int i = (int) f;
	     int dec = i;
	     String hex = decToHex(dec);
	     String temp;
	     byte[] bData = new byte[5];
	     for(int j = 0; j < 5; j++){
	    		temp = hex.substring(j*2, (j*2)+2);	    		
	    		bData[j] = Integer.valueOf(temp, 16).byteValue();
	     }
	     
		opBas.gravaCartao(bloco,bData,0, true);
	}

	public long aguardaCartao() {
		return opBas.aguardaCartao();
	}	
	
	public static String decToHex(int dec) {
	    StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
	    hexBuilder.setLength(sizeOfIntInHalfBytes);
	    for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
	    {
	      int j = dec & halfByte;
	      hexBuilder.setCharAt(i, hexDigits[j]);
	      dec >>= numberOfBitsInAHalfByte;
	    }
	    return hexBuilder.toString(); 
	}

	public boolean testeAutenticacaoNaoInicializado() {
		byte[] bchave = new byte[]{
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF //CHAVE				
				};  
		return opBas.autentica(bchave);
	}

	public boolean testeAutenticacaoInicializado() {
		byte[] bchave = new byte[]{
				(byte) 0xFA, (byte) 0x08, (byte) 0x11, (byte) 0x85, (byte) 0xCA, (byte) 0xB0 //CHAVE
				}; 
		return opBas.autentica(bchave);
	}

	public void inicializaCartao() throws IOException {
		//troca chave de segurança
		int bloco = 39;
		opBas.autenticaCartao(bloco);
		//passa o endereco da gravaçao do saldo chamando a funcao basica de leitura
		
		gravaSaldo(0);
		
		byte[] bData = new byte[]{
				(byte) 0xFA, (byte) 0x08, (byte) 0x11, (byte) 0x85, (byte) 0xCA, (byte) 0xB0, //CHAVE
				(byte) 0xFF, (byte) 0x07, (byte) 0x80, (byte) 0x69, //PADRÃO				
				(byte) 0xFA, (byte) 0x08, (byte) 0x11, (byte) 0x85, (byte) 0xCA, (byte) 0xB0, //CHAVE
				};
		opBas.gravaCartao(bloco,bData, 0, false); //mudar a função de gravar cartão para ser passado o index de inicio da gravação no bloco
	}
}
