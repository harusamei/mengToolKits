package mengToolKits;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

public class fileRW {

	public static void main(String[] args) {
		
		try {
			fRead();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void fWrite() throws IOException{
		PrintWriter fOut;
		fOut=new PrintWriter("test.out","utf-8");
		fOut.println("hello one sentence");
		fOut.println("中文英文都可以");
		fOut.close();
	}
	public static void fRead() throws IOException{
		Scanner fIn=new Scanner(Paths.get("test.out"),"utf-8");
		String oneStr;
		while(fIn.hasNext()){
			oneStr=fIn.nextLine();
			System.out.println(oneStr);
		}
		System.out.println("finished");
		fIn.close();
	}

}
