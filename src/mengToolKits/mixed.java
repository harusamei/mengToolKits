package mengToolKits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//����
public class mixed {
	
	public static void main(String[] args) {
		
		String[] allStr=getAllSubstr("abcde");
		for(String x:allStr){
			System.out.println(x);
		}
	}
	public static void tryCalendar(){
		
		String [] myWeek;
		Calendar curDay=Calendar.getInstance();
		myWeek=new String[curDay.getActualMaximum(Calendar.DAY_OF_WEEK)+1];
		String [] cnNum="һ;��;��;��;��;��".split(";");
		for(int i=Calendar.MONDAY, indx=0; i<=Calendar.SATURDAY; i++){
			myWeek[i]="����"+cnNum[indx++];
		}
		myWeek[Calendar.SUNDAY]="������";
		System.out.println(curDay.get(Calendar.DAY_OF_WEEK));
		System.out.println(myWeek[curDay.get(Calendar.DAY_OF_WEEK)]);
		curDay.roll(Calendar.DAY_OF_YEAR, 3);
		System.out.println(curDay.getTime().toString());
		System.out.println(myWeek[curDay.get(Calendar.DAY_OF_WEEK)]);
		//���ڸ�ֵ
		curDay.set(2019, Calendar.JANUARY, 23);
		Date today=curDay.getTime();
		
		System.out.println(today.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� hhʱmm��ss��"/*,java.util.Locale.CHINA*/);
		SimpleDateFormat df7 = new SimpleDateFormat("yyyy��MM��dd��");

		try {
			today = df7.parse("2003��09��23�� 11ʱ01��15��");
			System.out.println(today.toString());
			today = sdf.parse("2003��05��23�� 11ʱ01��15��");
			System.out.println(today.getClass().toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	public static String[] getAllSubstr(String oneStr){
		List<String> allSub=new ArrayList<String>();
		if(oneStr.length()==1) return new String[]{oneStr};
		String tStr="";
		for(int i=0; i<oneStr.length(); i++){
			tStr+=oneStr.charAt(i);
			allSub.add(tStr);
		}
		allSub.addAll(Arrays.asList(getAllSubstr(oneStr.substring(1))));
		return allSub.toArray(new String[0]);
	}
}
