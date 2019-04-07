package mengToolKits;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class xlsRW {
	
	public static Map<String, String> dupMap = new HashMap<String, String>();
    public static Map<String, String> sentMap = new HashMap<String, String>();

	public static void main(String [] args){
	
		readXls();
		writeXls();
//		String[] sList={"a"};
//		ArrayList<String> aList=new ArrayList<String>(Arrays.asList(new String[]{"a"}));
//		System.out.println(aList.toString());
//		System.out.println(Arrays.toString(sList));

	}
	
	public static void writeXls(){
		
		try{
			
			WritableWorkbook book= Workbook.createWorkbook(new File("dup-new.xls"));
			//������Ϊ����һҳ���Ĺ���������0��ʾ���ǵ�һҳ
			WritableSheet sheet=book.createSheet("oneSheet",0);		
			//��Label����Ĺ�������ָ����Ԫ��λ���ǵ�һ�е�һ��(0,0)		
			//�Լ���Ԫ������Ϊtest		
			Label label=new Label(0,0,"test");		
			//������õĵ�Ԫ����ӵ���������		
			sheet.addCell(label);		
			/*����һ���������ֵĵ�Ԫ��		
			����ʹ��Number��������·�����������﷨����		
			��Ԫ��λ���ǵڶ��У���һ�У�ֵΪ789.123*/		
			jxl.write.Number number = new jxl.write.Number(1,0,789.123);		
			sheet.addCell(number);		
			//д�����ݲ��ر��ļ�		
			book.write();		
			book.close();
		}catch(Exception e){	
			System.out.println(e);	
		}
	}
	
	public static void readXls(){
		
		try{
			Workbook workbook=Workbook.getWorkbook(new File("WanFangData.xls")); 
		
	        //2:��ȡ��һ��������sheet
	        Sheet sheet=workbook.getSheet(0);
	        System.out.println(workbook.getNumberOfSheets());
	        //3:��ȡ����
	        System.out.println("�У�"+sheet.getRows());
	        System.out.println("�У�"+sheet.getColumns());
	        Cell oneCell;
	        for(int i=0;i<sheet.getRows();i++){
	            for(int j=0;j<sheet.getColumns();j++){
	            	oneCell=sheet.getCell(j,i);
	                System.out.print(oneCell.getContents()+" ");
	            }
	            System.out.println();
	        }
	        //���һ�����ر���Դ
	        workbook.close();
		}catch(Exception e){	
			System.out.println(e);	
		}
	}
	
}
