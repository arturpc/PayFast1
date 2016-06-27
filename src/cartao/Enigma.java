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
}
