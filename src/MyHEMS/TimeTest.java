package MyHEMS;

public class TimeTest extends Thread
{
	public void run()
	{
	//����ʱ����*************************************	
		while (true)
		{
			while (Control.isStart)
			{
				try
				{
					Thread.sleep(Control.fiveMinutes);			//5����ִ��һ��
					Control.countfiveMinutes++;
					if (Control.countfiveMinutes == 12)//5����
					{
						Control.countfiveMinutes = 0;
						Control.currentHour++;
					 	if (Control.currentHour == 24)
					 	{
					 		Control.currentHour = 0;
					 	}
					}
				//	System.out.println("��ǰʱ��.currentHour:"+Control.currentHour+"����@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
		}		
	//����ʱ����*************************************	
	}
	
}
