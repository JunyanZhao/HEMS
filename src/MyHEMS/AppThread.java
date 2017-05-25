package MyHEMS;

public class AppThread implements Runnable  //���������߳�
{
	public synchronized void run()
	{
		while(Control.isStart) 
		{
			System.out.println("��������߳�.....................");
			try {
				Thread.sleep(Control.fiveMinutes);	//����5����һ��
				//�յ�
				if (Control.airCondition.isOn == true) //�յ����ڴ�״̬
				{
					if (!Control.airCondition.isSleep)	//�յ����ڹ���״̬
					{
						Control.airCondition.currentTempature -= 1;	//ÿʮ�������½���2��		
						//�������ʶ�
						System.out.println("�յ�.��ǰ����(������): "+Control.airCondition.currentTempature+"��");
						if (Control.airCondition.currentTempature <= Control.airCondition.setTempature)  
						{
							Control.airCondition.isGood = true;
							Control.airCondition.currentTempature = Control.airCondition.setTempature;
							Control.airCondition.isSleep = true;
							System.out.println("�յ�.������********************************************");
						}										
					}
					else 				//�յ���������״̬
					{
						Control.airCondition.currentTempature += 1;	//ÿʮ������������2��	
						System.out.println("�յ�.��ǰ����(������): "+Control.airCondition.currentTempature+"��");	
						if (Control.airCondition.currentTempature >= Control.airCondition.reRunInOnTempature) 
						{
							Control.airCondition.isGood = false;
							Control.airCondition.isSleep = false;
							System.out.println("�յ�.׼����������****************************************");
						}
						if (Control.airCondition.currentTempature >= Control.airCondition.roomTempature)
						{
							Control.airCondition.currentTempature = Control.airCondition.roomTempature;	
						}
					}
				}
				else		//�յ����ڹر�״̬
				{
					Control.airCondition.currentTempature += 1;	//ÿʮ������������2��	
					if (Control.airCondition.currentTempature >= Control.airCondition.roomTempature)
					{
						Control.airCondition.currentTempature = Control.airCondition.roomTempature;	
					}
				}
				//�·���ϴ����
				if (Control.clothesDry.isOn == true) //��ϴ�������ڹ���״̬
				{
					//�յ���ˮ������ϴ��������һ������ϴ�������Ǽ�ʱ��
					Control.clothesDry.currentDryTime += 10;	//ʮ���ӣ���ϴ����50����
					System.out.println("��ϴ��.��ǰ��ϴʱ��(������): "+Control.clothesDry.currentDryTime+"����");												
					if (Control.clothesDry.currentDryTime >= Control.clothesDry.needDryTime)
					{
						Control.clothesDry.isOn = false;
						Control.clothesDry.isGood = true;
						System.out.println("Work finished, ��ϴ��.clothesDry is turned off!");
					}
					else {
						Control.clothesDry.isGood = false;
					}
				}
				//����
				if (Control.electronicVehicle.isOn == true) 
				{
					Control.calEVTime++;
					if (Control.calEVTime == 6)	//һ��Сʱ����繲��13��Сʱ
					{
						Control.calEVTime = 0;
						Control.electronicVehicle.currentChargeTime++;
					}
					System.out.println("����.��ǰ���ʱ��(������)***************************************:"+Control.electronicVehicle.currentChargeTime+"Сʱ");									
					if (Control.electronicVehicle.currentChargeTime >= Control.electronicVehicle.needChargeTime)
					{
						Control.electronicVehicle.isOn = false;
						Control.electronicVehicle.isGood = true;
						Control.totalPower -= Control.electronicVehicle.power;
						System.out.println("Work finished, ����.electronicVehicle is turned off!");
					}	
					else {
						Control.electronicVehicle.isGood = false;
					}
				}
				//��ˮ��
				if (Control.waterHeater.isOn == true) 
				{
					if (!Control.waterHeater.isSleep)
					{
						Control.waterHeater.currentWaterTempature += 10;	//ÿ10����ˮ������20��
						if(Control.waterHeater.currentWaterTempature >= 100)
						{
							Control.waterHeater.currentWaterTempature = 100;
							Control.waterHeater.isGood = true;
						}
						System.out.println("��ˮ��.��ǰˮ��(������): "+Control.waterHeater.currentWaterTempature+"��");
						//���ʶ��ж�
						if (Control.waterHeater.currentWaterTempature > Control.waterHeater.reRunInOnTempature && Control.waterHeater.currentWaterTempature < Control.waterHeater.highWaterTempature)
						{
							Control.waterHeater.isGood = true;
						}
						else {
							if (Control.waterHeater.currentWaterTempature >= Control.waterHeater.highWaterTempature)
							{
								Control.waterHeater.isSleep = true;
								Control.waterHeater.isGood = true;
								System.out.println("��ˮ��.����������********************************************");
							}
						}
					}
					else 
					{
						if (Control.waterHeater.currentWaterTempature > Control.waterHeater.roomWaterTempature) 
						{
							Control.waterHeater.currentWaterTempature -= 3;	//ÿ5����ˮ�½���3��
							System.out.println("��ˮ��.��ǰˮ��(������): "+Control.waterHeater.currentWaterTempature+"��");
							if (Control.waterHeater.currentWaterTempature <= Control.waterHeater.reRunInOnTempature) {
								Control.waterHeater.isSleep = false;
								Control.waterHeater.isGood = false;
								System.out.println("��ˮ��.׼����������****************************************");
							}
						}
						else {
							Control.waterHeater.currentWaterTempature = Control.waterHeater.roomWaterTempature;
						}
					}
				}		
				else     //ͣ��
				{
					if (Control.waterHeater.currentWaterTempature > Control.waterHeater.roomWaterTempature) 
					{
						Control.waterHeater.currentWaterTempature -= 3;	//ÿ5����ˮ�½���3��
						if (Control.waterHeater.currentWaterTempature <= Control.waterHeater.roomWaterTempature) 
						{
							Control.waterHeater.currentWaterTempature = Control.waterHeater.roomWaterTempature;
						}
					}
				}								
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
}
