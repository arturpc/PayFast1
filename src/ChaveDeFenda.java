import java.io.IOException;

import cartao.OperacaoBasica;
import cartao.OperacoesEspecificas;
import dominio.Cartao;

public class ChaveDeFenda {
  //converção para hexa
  private static final int sizeOfIntInHalfBytes = 10;
  private static final int numberOfBitsInAHalfByte = 4;
  private static final int halfByte = 0x0F;
  private static final char[] hexDigits = { 
    '0', '1', '2', '3', '4', '5', '6', '7', 
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };
	public static void main (String[] args) throws IOException {
		Cartao cartao = new Cartao();		
		boolean testeAutenticado  = false;		
		String retorno = "";
		OperacaoBasica opBas = new OperacaoBasica();
		opBas.aguardaCartao();
		byte[] bchave = new byte[]{
				(byte) 0xFA, (byte) 0x08, (byte) 0x11, (byte) 0x85, (byte) 0xCA, (byte) 0xB0 //CHAVE
				}; 
		testeAutenticado = opBas.autentica(bchave);
				
		int bloco = 36;		
		opBas.autenticaCartao(bloco);
		
	    String hex = decToHex((int) 0);
	    String temp;
	    byte[] bData = new byte[5];
	    for(int j = 0; j < 5; j++){
	    		temp = hex.substring(j*2, (j*2)+2);	    		
	    		bData[j] = Integer.valueOf(temp, 16).byteValue();
	    }
	     
		opBas.gravaCartao(bloco,bData,0, true);
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
