package MyHEMS;
/**
 * ʱ�䣺2013��11��28��
 * �Ľ��������˽��棬����ˮ��������������֮���������ٴγ��İ�ť
 * ע�����Ϊ�˱��ڲ��ԣ�ʱ��һ�����˸��ģ�ʵ��Ӧ����ֻ��ɾȥ�����õ�ʱ����룬��ԭʵ�ʵ�ʱ��Ĵ��뼴��
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Control
{
	static final int fiveMinutes = 1000;	//��Ӧ��300,000��������
	static int currentHour;					//��ǰʱ�䣬11�㵽12��[11,12��֮����11
	static int countfiveMinutes = 0;			//�����������˶��ٸ�5����
	//����
	static AirCondition airCondition;	
	static ClothesDry clothesDry;
	static ElectronicVehicle electronicVehicle;
	static WaterHeater waterHeater;
	//������������
	static Appliance[] appliance;

	static int totalPower = 0;		//�ܹ���
	static java.util.Date date;		//ʱ�䣬ʵ����Ҫ�õ����������ڲ��Ե�ʱ�����Ҫȫ��ɾȥ
	static final int delay_max = 5;		//������ʱ������������ʱʱ��
	static int delayTime;			
	static final int demandLimit = 5000;		//��������
	
	static int calEVTime = 0;		//�������ʱ�������12Ϊһ��Сʱ
	
	static Boolean isStart = false;		//ϵͳ�Ƿ���
	static boolean isInit = false;		//ϵͳ�Ƿ�����˳�ʼ��
	
	public static void main(String[] args)	//���߳�
	{
		System.out.println("�������߳�...................................");
		MyFrame myFrame = new MyFrame("�Ҿ����ܿ���ϵͳ");	//ʵ��һ���˻���������
		
		//������
		Control.currentHour = 10;
		TimeTest timeTestThread = new TimeTest();
		timeTestThread.start();
		//������	

		System.out.println("�ȴ����� .......................................................");
		//�洢��Դ����ʱ
		while(true)
		{
			while (isStart)
			{
	/*			//ʵ����ʱ���ȡֵ������ʵ��Ӧ�ã�ʱ�����������У���"TimeTest.java"��"Control.currentHour = 10;"ɾȥ��
				date = new java.util.Date();
				currentHour = date.getHours();*/				
				appliance = init();		//��ʼ��
				try
				{
					Thread.sleep(1000);	//1���Ӽ��һ��
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				for (int i = 0; i < 4; i++) 	//�Ӹ����ȼ���ʼ�ж���һ������������ʱ���ȴ�
				{	
					if ((appliance[i].delayTime == currentHour) && appliance[i].isDelayStart)	//��ʱʱ�䵽����ҵ���������ʱ����״̬
					{
						System.out.println("��ǰʱ��.currentHour:"+Control.currentHour+"����@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
						System.out.println("������ʱʱ�䣬���ڳ��Դ�........");
						appliance[i].isDelayStart = false;
						appliance[i].beToOn = false;
						totalPower = getToStartTotalPower(i);
												
						if (totalPower < demandLimit) //�ܹ���С�ڿ��������
						{
							System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");		
							System.out.println("�򿪵���......");
							appliance[i].isOn = true;
							showAppOn(i);				//��ʾ��δ򿪵ĵ���
						}
						else //�ܹ��ʴ��ڿ��������
						{										
							System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");	
							System.out.println("���ڴӵ����ȼ��رյ���......");
							System.out.println("�򿪵���......");
							openApp(appliance);			//�رյ����ȼ�����������ʱ׼�������ĵ���
						}
					}
				}
			}	
		}			
	}	
	static int delayStartOnPeriod(int i)	//��ʱ��ĳ��ʱ�̿��������ؿ�����ʱ���
	{					
		System.out.println("-----------------------------------------------------------------");
		totalPower = getToStartTotalPower(i);
		System.out.println("��ǰʱ��.currentHour:"+currentHour+"����@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		//ʱ���Ǹ߷���ʱ���������ʶȣ����������������ʶȣ���appliance
		if ((currentHour >= 6 && currentHour <12) || (currentHour >= 18 && currentHour < 24)) 
		{
			System.out.println("ʱ���: on_peak");
			delayTime = 24 - currentHour; //��ʱ���ͷ�ʱ�����ʱʱ��
			System.out.println("��ʱ���ͷ�ʱ�����ʱʱ����:"+delayTime+"Сʱ");
			if (delayTime <= delay_max) //��ʱ���ͷ�ʱ��С��������ʱʱ��
			{		
				System.out.println("������ʱ��off_peak,�ȴ���������㿪����");
				appliance[i].isDelayStart = true;
				return 0;					
			}
			else //��ʱ���ͷ�ʱ�����������ʱʱ��
			{
				System.out.println("�޷���ʱ��off_peak��������ʱ��mid_peak");
				if (currentHour < 12)  //ʱ�����з�ʱ��֮ǰ���Ƕ�
				{
					System.out.println("����ʱ�䴦������on_peakʱ��");
					delayTime = 12 - currentHour;
					System.out.println("��ʱ��mid_peakʱ�����ʱʱ����: "+delayTime+"Сʱ");
					if (delayTime < delay_max)    //��ʱ���з�ʱ��С��������ʱʱ��
					{		
						System.out.println("������ʱ��mid_peakʱ�Σ��ȴ�������12�㿪����");
						appliance[i].isDelayStart = true;
						return 12;
					}
					else 
					{
						System.out.println("�޷���ʱ��mid_peakʱ�Σ�����ֱ�Ӵ�........");
						if (totalPower < demandLimit) //�ܹ���С�ڿ��������
						{
							System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");		
							System.out.println("�򿪵���......");
							openApp(appliance);
						}
						else //�ܹ��ʴ��ڿ��������
						{										
							System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");	
							System.out.println("���ڴӵ����ȼ��رյ���......");
							System.out.println("�򿪵���......");
							openApp(appliance);
						}
						return 25;
					}
				}
				else  //ʱ�����з�ʱ��֮��ͷ���֮ǰ���Ƕ�
				{
					System.out.println("�޷���ʱ��mid_peak������ֱ�Ӵ�.........");
					if (totalPower < demandLimit) //�ܹ���С�ڿ��������
					{
						System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");		
						System.out.println("�򿪵���......");
						openApp(appliance);
					}
					else //�ܹ��ʴ��ڿ��������
					{										
						System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");	
						System.out.println("���ڴӵ����ȼ��رյ���......");
						System.out.println("�򿪵���......");
						openApp(appliance);
					}
					appliance[i].isDelayStart = false;
					return 25;
				}
			}
		}
		//ʱ�����з���ʱ��
		else if (currentHour >= 12 && currentHour <18) //�Ƿ�Ҫ����demandLimit��������
		{
			System.out.println("ʱ���: mid_peak");
			delayTime = 24 - currentHour;	//��ʱ��off_peak������ʱ
			if (delayTime > delay_max)
			{
				System.out.println("�޷���ʱ��off_peak�����Դ򿪵���.......");
				openApp(appliance);
				appliance[i].isDelayStart = false;
				return 25;
			}	
			else 
			{
				System.out.println("������ʱ��off_peak���ȴ����������򿪵���.......");
				appliance[i].isDelayStart = true;
				return 0;						
			}
		}
		//ʱ���ǵͷ���ʱ��
		else if (currentHour >= 0 && currentHour < 6)  //�Ƿ�Ҫ����demandLimit��������
		{
			System.out.println("ʱ���: off_peak");
			if (totalPower < demandLimit) //�ܹ���С�ڿ��������
			{
				System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");		
				System.out.println("�򿪵���......");
				openApp(appliance);
				appliance[i].isDelayStart = false;
				return 25;
			}
			else //�ܹ��ʴ��ڿ��������
			{										
				System.out.println("�򿪵�������ܹ���totalPowerΪ: "+totalPower+"����");	
				System.out.println("���ڴӵ����ȼ��رյ���......");
				System.out.println("�򿪵���......");
				openApp(appliance);
				appliance[i].isDelayStart = false;
				return 25;
			}
		}
		return 25;					
	}	
	
	static Appliance[] init()					//������ʼ��
	{
		if (!isInit)
		{
			isInit = true;
			System.out.println("��ʼ��.....");
			Appliance[] app = new Appliance[4];
			airCondition = new AirCondition();
			clothesDry = new ClothesDry();
			electronicVehicle = new ElectronicVehicle();
			waterHeater = new WaterHeater();
			app[0] = waterHeater;
			app[1] = airCondition;
			app[2] = clothesDry;
			app[3] = electronicVehicle;
			waterHeater.priority = 4;
			waterHeater.roomWaterTempature = 30;
			waterHeater.highWaterTempature = 90;
			waterHeater.currentWaterTempature = waterHeater.roomWaterTempature;
			waterHeater.reRunInOnTempature = 60;
			waterHeater.power = 900;
			
			airCondition.priority = 3;
			airCondition.roomTempature = 32;
			airCondition.setTempature = 22;
			airCondition.currentTempature = airCondition.roomTempature;
			airCondition.reRunInOnTempature = 26;
			airCondition.power = 1500;
			airCondition.beToOn =false;
			
			clothesDry.priority = 2;
			clothesDry.needDryTime = 50;	//��ʮ����
			clothesDry.currentDryTime = 0;
			clothesDry.power = 1000;

			electronicVehicle.priority = 1;	
			electronicVehicle.currentChargeTime = 0;
			electronicVehicle.needChargeTime = 13;	//13��Сʱ
			electronicVehicle.power = 2000;
			
			AppThread myThread = new AppThread();		
			Thread thread = new Thread(myThread);
			thread.start();	
			TimeThread timeThread = new TimeThread();
			Thread thread2 = new Thread(timeThread);
			thread2.start();
			return app;
		}
		else {
			return appliance;
		}
	}
	static int getToStartTotalPower(int i)					//�õ���ǰ������������Ҫ�򿪵ĵ��������ܹ���
	{
		int total = getTotalPower(appliance);
		total += appliance[i].power;
		return total;
	}
	static int getTotalPower(Appliance[] app)				//�õ���ǰ�Ѿ��򿪵ĵ������ܹ���
	{
		int total = 0;
		for (int i = 0; i < app.length; i++)
		{
			if (app[i].isOn)
			{
				total += app[i].power;
			}
		}
		return total;
	}
	static boolean allGood()		//�������ʶȾ�����
	{
		if (airCondition.isGood == true && clothesDry.isGood == true && waterHeater.isGood == true && electronicVehicle.isGood == true)
		{
			return true;	
		}
		else {
			return false;
		}
	}
	static void openApp(Appliance[] app)	//����������Ƶ������°������ȼ��򿪵���
	{
		for (int i = 0; i < 4; i++) 
		{
			if (app[i].beToOn && !app[i].isOn) //�������ڹر�׼����
			{	
				System.out.println("���ڴ���##################################");
				if (app[i].isGood) 
				{
					System.out.println("�Ѿ��������ʶȣ�����򿪣�");
				}
				else 
				{
					if (totalPower > demandLimit) 
					{
						System.out.println("�򿪺󽫳����������ƣ����ڳ��Թر������������ٴδ�..........");
						while (totalPower > demandLimit)
						{
							closeFromLower(app);
						}
						if (totalPower <= demandLimit) 
						{
							app[i].isOn = true;
							app[i].beToOn = false;
							showAppOn(i);
						}
						break;
					}
					else 
					{
						app[i].isOn = true;
						app[i].beToOn = false;
						showAppOn(i);
					}
				}	
			}			
		}
	}
	static void showStartFailed(int i)		//��ʾ��ʧ�ܵĵ���
	{
		switch (i)
		{
		case 0:
			System.out.println("��ˮ�����´�ʧ��!");
			break;
		case 1:
			System.out.println("�յ����´�ʧ�ܣ�");
			break;
		case 2:
			System.out.println("��ϴ�����´�ʧ��!");
			break;
		case 3:
			System.out.println("�������³��ʧ�ܣ�");
			break;
		default:
			break;
		}
	}
	static void closeFromLower(Appliance[] app)//�ȹر��Ѿ������������ʶȵģ�����������ɸ��ڵ������ƣ��ٹر��Ѿ�����Ϊ����ĵ��������������ȼ�
	{
		System.out.println("���ڳ��Թرսϵ����ȼ��ĵ���........");
		for (int i = 3; i >= 0; i--) 
		{
			if (app[i].isOn && app[i].isGood) 
			{
				app[i].isOn = false;
				showAppOff(i);
				totalPower -= app[i].power;
				if (totalPower < demandLimit) 
				{			
					System.out.println("�رս���,���򿪵ĵ���������........");
					return;
				}
			}			
		}
		for (int i = 3; i >= 0; i--) 
		{
			if (app[i].isOn)
			{
				app[i].isOn = false;
				app[i].beToOn =false;
				showAppOff(i);
				totalPower -= app[i].power;
				if (totalPower < demandLimit) 
				{
					System.out.println("�رս���,���򿪵ĵ���������........");
					return;
				}
			}
		}
		System.out.println("�ӵ����ȼ��رյ���ʧ�ܣ�");
	}
	static void showAppOn(int i)				//��ʾ�������¿�������
	{
		switch (i)
		{
		case 0:
			System.out.println("��ˮ��.waterHeater is turned on����ʼ����!");
			break;
		case 1:
			System.out.println("�յ�.airCondition is turned on����ʼ����!");
			break;
		case 2:
			System.out.println("��ϴ��.clothesDry is turned on����ʼ����!");
			break;
		case 3:
			System.out.println("����.electronicVehicle is turned on����ʼ����!");
			break;
		default:
			break;
		}
	}
	static void showAppOff(int i)				//��ʾ�������¹رն���
	{
		switch (i)
		{
		case 0:
			System.out.println("��ˮ��.waterHeater is turned off��ֹͣ����!");
			break;
		case 1:
			System.out.println("�յ�.airCondition is turned off��ֹͣ����!");
			break;
		case 2:
			System.out.println("��ϴ��.clothesDry is turned off��ֹͣ����!");
			break;
		case 3:
			System.out.println("����.electronicVehicle is turned off��ֹͣ����!");
			break;
		default:
			break;
		}
	}

}
