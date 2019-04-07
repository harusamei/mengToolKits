package mengToolKits;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.csvreader.CsvReader;

//关于字符串,反射，文件读写
class myTestData{
	int id;
	String name;
	List<String> other;
	
	myTestData(int x, String y){
		id=x;
		name=y;
		other=new LinkedList<>();
		other.add("item1");
	}
	public String creatJson(){
		JSONObject object = new JSONObject();
		try {
			object.put("id", this.id);
			object.put("other", this.other);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object.toString();
	}
}
public class stringProc {
	
	private static Zone [] zoneList;
	
	public static void main(String [] args){

		myTestData aData=new myTestData(1,"test");
		aData.other.add("oneOther");
		System.out.println(aData.creatJson());
		
		array2List();
	}
	//String[]  2  ArrayList<String>
	public static void array2List(){
		
		String [] a=null;
		List<String> bList=new ArrayList<String>();
		bList.add("one");
		a=bList.toArray(new String[0]);
		System.out.println(a[0]);
		
		a=new String[]{"one","two"};
		System.out.println(a[1]);
		
		bList=Arrays.asList(a);
		System.out.println(bList);
	}
	public static void fileIO(String finName, String foutName){
		try {
			Scanner in=new Scanner(Paths.get("test.txt"),"utf-8");
			PrintWriter out=new PrintWriter("test.out","utf-8");
			String temLine;
			while(in.hasNext()){
				temLine=in.nextLine();
				out.println(temLine);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//枚举 count个UNICODE 符号
	public static String outUnicodeStr(int begUcode, int count){
		//hexCode
		StringBuilder builder=new StringBuilder();
		for(int i=0; i<count; i++){
			builder.appendCodePoint(begUcode++);
		}
		return builder.toString();
	}
	//返回字符串的unicode 数组
	public static int[] splitUnicodeList(String tStr){
		
		return tStr.codePoints().toArray();
	}
	//关于反射，其实我就是想要 obj[key]=val， JAVA居然没有
	public static void getFeatVal(Object x){
		
		Class<?> c1=x.getClass();
		Field[] f=c1.getDeclaredFields();
		String tyStr="";
		int intVal;
		List<String> listVal;
		try{
			for(Field f1:f){
				tyStr=f1.getGenericType().toString();
				if(tyStr.indexOf("int")>=0){
					intVal=f1.getInt(x);
				}else if(tyStr.indexOf("List")>=0){
					listVal=(List<String>)f1.get(x);
					System.out.println(listVal.toString());
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public static void loadZoneList(String inFileName){
		
		LinkedList<String> lines= new LinkedList<>();
		try{
			CsvReader csvReader = new CsvReader(inFileName,',', Charset.forName("Utf-8"));
		// 读表头
	        csvReader.readHeaders();
	        while (csvReader.readRecord()){
	            // 读一整行
	        	lines.add(csvReader.getRawRecord());
	        }
		} catch (IOException e) {
            e.printStackTrace();
        }
		
		zoneList=new Zone[lines.size()];;
		//Random random = new Random();
		for(int i=0; i<lines.size(); i++){
			zoneList[i]=new Zone(lines.get(i));
		}
		Arrays.sort(zoneList,Comparator.comparingInt(Zone::getBeg));
	}
	public static int getCodeZone(char aCh){
		
		int indx=-1;
		Zone tKey=new Zone(aCh,aCh,""+aCh);
		indx=Arrays.binarySearch(zoneList, tKey, Comparator.comparingInt(Zone::getBeg));
		if(indx>=0){
			return indx;
		}
		return -indx-2;
	}
	public static int getCodeZone(int uCode){
		int indx=-1;
		StringBuilder builder=new StringBuilder();
		Zone tKey=new Zone(uCode,uCode,builder.append(uCode).toString());
		indx=Arrays.binarySearch(zoneList, tKey, Comparator.comparingInt(Zone::getBeg));
		if(indx>=0){
			return indx;
		}
		return -indx-2;
	}
	
	public void testStr(){
		
		String xStr="ab中文";
		System.out.println(xStr.length()+": "+xStr.codePointCount(0,xStr.length()));
		char xCh=xStr.charAt(2);
		char yCh=xStr.charAt(3);
		int zInt1=Integer.parseInt(Integer.toHexString(xCh), 16);

		System.out.println(xCh+":"+xStr.charAt(3));
		System.out.println(Integer.toHexString(xCh)+":"+Integer.toHexString(yCh)+":"+zInt1);
		System.out.printf("%s,%s",xStr.indexOf("b中"),xStr.indexOf(20013));		//index by unicode or string
		
		String tStr="\u2122";
		StringBuilder builder=new StringBuilder();
		builder.appendCodePoint(0x10330);
		builder.appendCodePoint(0x2122);
		String tS=builder.toString();
		int len=tS.length();
		int ithZi=tS.offsetByCodePoints(0,1);
		System.out.printf("%d ,%s ,%x\n",ithZi,tS.substring(0, 2),tS.codePointAt(ithZi));
		System.out.println(tStr+":"+builder.toString());
		System.out.printf("%d %d",len,tS.codePointCount(0,len));	//codePointCount<len
	}
	public void testRegExp(){
		
		String tStr="C：2AC77 C：2C";
		int uCode=0;
		Pattern re=Pattern.compile(".*：");
		Matcher mat=re.matcher(tStr);
		if(mat.find()){
			mat.replaceAll("");
			System.out.println(Integer.parseInt(mat.replaceAll(""),16));
			uCode=Integer.parseInt(mat.replaceAll(""),16);
			System.out.println((char)uCode);

		}
		String temS="(10)说明书";
		Pattern r = Pattern.compile("\\(\\d+\\)\\s*");
		Matcher m;
		m=r.matcher(temS);
		boolean isMat=Pattern.matches("\\(\\d+\\)\\s*",temS.toString());
		System.out.println(m.find());
	}
	public void testFileInOut(){
		
		String tStr="hello world";
		try{
		 	FileOutputStream fos = new FileOutputStream("d:\\output.txt"); 
	        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
	        osw.write(tStr); 
	        osw.flush(); 
	 	} catch (Exception e) { 
	        e.printStackTrace(); 
	    }
	}

}

class Zone{
	
	int beg;
	int end;
	String zoneName;
	String other;
	
	public Zone(){
		beg=end=0;
		zoneName="";
	}
	public Zone(String aLine){
		String[] items=aLine.split(",");
		if(items.length<3){
			beg=end=0;
			zoneName=other="";
			System.out.println("wrong with "+aLine);
			return;
		}
		beg=Integer.parseInt(items[0], 16);
		end=Integer.parseInt(items[1], 16);
		zoneName=items[2];
		other="";
		if(items.length==4){
			other=items[3];
		}
	}
	public Zone(int b, int e, String tN){
		beg=b;
		end=e;
		zoneName=tN;
	}
	int getBeg(){
		return beg;
	}
	public String toString(){
		return "{"+Integer.toHexString(beg)+","+Integer.toHexString(end)+","+zoneName+","+other+"}";
	}
}
