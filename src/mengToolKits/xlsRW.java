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
			//生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet=book.createSheet("oneSheet",0);		
			//在Label对象的构造子中指名单元格位置是第一列第一行(0,0)		
			//以及单元格内容为test		
			Label label=new Label(0,0,"test");		
			//将定义好的单元格添加到工作表中		
			sheet.addCell(label);		
			/*生成一个保存数字的单元格		
			必须使用Number的完整包路径，否则有语法歧义		
			单元格位置是第二列，第一行，值为789.123*/		
			jxl.write.Number number = new jxl.write.Number(1,0,789.123);		
			sheet.addCell(number);		
			//写入数据并关闭文件		
			book.write();		
			book.close();
		}catch(Exception e){	
			System.out.println(e);	
		}
	}
	
	public static void readXls(){
		
		try{
			Workbook workbook=Workbook.getWorkbook(new File("WanFangData.xls")); 
		
	        //2:获取第一个工作表sheet
	        Sheet sheet=workbook.getSheet(0);
	        System.out.println(workbook.getNumberOfSheets());
	        //3:获取数据
	        System.out.println("行："+sheet.getRows());
	        System.out.println("列："+sheet.getColumns());
	        Cell oneCell;
	        for(int i=0;i<sheet.getRows();i++){
	            for(int j=0;j<sheet.getColumns();j++){
	            	oneCell=sheet.getCell(j,i);
	                System.out.print(oneCell.getContents()+" ");
	            }
	            System.out.println();
	        }
	        //最后一步：关闭资源
	        workbook.close();
		}catch(Exception e){	
			System.out.println(e);	
		}
	}
	
}
