package cartao;

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
	
	public void gravaSaldo(float saldo){
		//autentica setor
		//autentica no setor 10
		int bloco = 36;
		opBas.autenticaCartao(bloco);
		//passa o endereco da gravaçao do saldo chamando a funcao basica de leitura
		 float f = saldo;
		 int i = (int) f;
	     int dec = i;
	     String hex = decToHex(dec);
	     String temp;
	     byte[] bData = new byte[5];
	     for(int j = 0; j < 5; j++){
	    		temp = hex.substring(j*2, (j*2)+2);	    		
	    		bData[j] = Integer.valueOf(temp, 16).byteValue();
	     }
	     
		opBas.gravaCartao(bloco,bData);
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
}
