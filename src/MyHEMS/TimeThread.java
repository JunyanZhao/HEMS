package MyHEMS;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeThread implements Runnable
{
	public void run()
	{			
		while (Control.isStart)
		{
			try
			{
				Thread.sleep(1000);//1����ʱ��
				
				showTime();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}		
		}	
	}
	static void showTime()
	{
		Date date = (Date) new java.util.Date();
		MyFrame.textField.setText("��ǰʱ�䣺 "+date.toLocaleString());
	}
}