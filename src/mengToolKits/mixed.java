package mengToolKits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//杂类
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
		String [] cnNum="一;二;三;四;五;六".split(";");
		for(int i=Calendar.MONDAY, indx=0; i<=Calendar.SATURDAY; i++){
			myWeek[i]="星期"+cnNum[indx++];
		}
		myWeek[Calendar.SUNDAY]="星期日";
		System.out.println(curDay.get(Calendar.DAY_OF_WEEK));
		System.out.println(myWeek[curDay.get(Calendar.DAY_OF_WEEK)]);
		curDay.roll(Calendar.DAY_OF_YEAR, 3);
		System.out.println(curDay.getTime().toString());
		System.out.println(myWeek[curDay.get(Calendar.DAY_OF_WEEK)]);
		//日期赋值
		curDay.set(2019, Calendar.JANUARY, 23);
		Date today=curDay.getTime();
		
		System.out.println(today.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒"/*,java.util.Locale.CHINA*/);
		SimpleDateFormat df7 = new SimpleDateFormat("yyyy年MM月dd日");

		try {
			today = df7.parse("2003年09月23日 11时01分15秒");
			System.out.println(today.toString());
			today = sdf.parse("2003年05月23日 11时01分15秒");
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
