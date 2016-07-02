package cartao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Enigma {
	public static byte[] criptografa (byte[] bo) throws IOException{
		 byte[] bd = new byte[bo.length];
		 for (int i = 0; i < bd.length; i++) {
			bd[i] = (byte) (bo[i] - ((byte) 00000001));
		}
		return bd;
	}
	
	public static byte[] decriptografa (byte[] bo) throws IOException{
		 byte[] bd = new byte[bo.length];
		 for (int i = 0; i < bd.length; i++) {
			bd[i] = (byte) (bo[i] + ((byte) 00000001));
			
		}
		return bd;
	}
	public static void main(String[] args) throws IOException {
		byte[] bchave = new byte[]{
				(byte) 0xFA, (byte) 0x08, (byte) 0x11, (byte) 0x85, (byte) 0xCA, (byte) 0xB0 //CHAVE
				}; 
	   byte[] xacalacabum = Enigma.decriptografa(bchave);
	   String resolvendoAMerdaQueEuFizBebao = "";
       for (int i = 0; i < 6; i++) {
    	   resolvendoAMerdaQueEuFizBebao += String.format("%02X ", xacalacabum[i]);
           // The result is formatted as a hexadecimal integer
       }
       System.out.println(resolvendoAMerdaQueEuFizBebao);		
	}
}
 