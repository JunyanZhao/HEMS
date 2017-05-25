package MyHEMS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
/**
 * ������������ʱ�׶Σ��رյ����Ƿ�ֹͣ��ʱ��ֱ�ӹر��أ������Ҫ����beToOn��Ϊfalse���ɡ�
 *
 */

public class MyFrame extends JFrame
{
	static TextField textField;
	MyFrame(String title)		//���һ���˻��������
	{
		super(title);
		setBounds(200, 100, 300, 300);
		setVisible(true);
		setLayout(new BorderLayout());

		textField = new TextField();
		textField.setEditable(false);
		add(textField, BorderLayout.NORTH);
		
		Panel panel_1 = new Panel();
		panel_1.setLayout(new GridLayout(4, 2, 5, 5));
		

		JButton buttonCD = new JButton("������ϴ��");
		MonitotButtonCD monitotButtonCD = new MonitotButtonCD();
		buttonCD.addActionListener(monitotButtonCD);
		JButton buttonCDKill = new JButton("�رո�ϴ��");
		MonitotButtonCDKill monitotButtonCDKill = new MonitotButtonCDKill();
		buttonCDKill.addActionListener(monitotButtonCDKill);
		
		JButton buttonEV = new JButton("�����������");
		MonitorButtonEV monitorButtonEV = new MonitorButtonEV();
		buttonEV.addActionListener(monitorButtonEV);
		JButton buttonEVKill = new JButton("�ر��������");
		MonitorButtonEVKill monitorButtonEVKill = new MonitorButtonEVKill();
		buttonEVKill.addActionListener(monitorButtonEVKill);
		
		JButton buttonAC = new JButton("�����յ�");
		MonitorButtonAC monitorButtonAC = new MonitorButtonAC();
		buttonAC.addActionListener(monitorButtonAC);
		JButton buttonACKill = new JButton("�رտյ�");
		MonitorButtonACKill monitorButtonACKill = new MonitorButtonACKill();
		buttonACKill.addActionListener(monitorButtonACKill);
		
		JButton buttonWH = new JButton("������ˮ��");
		MonitorButtonWH monitorButtonWH = new MonitorButtonWH();
		buttonWH.addActionListener(monitorButtonWH);
		JButton buttonWHKill = new JButton("�ر���ˮ��");
		MonitorButtonWHKill monitorButtonWHKill = new MonitorButtonWHKill();
		buttonWHKill.addActionListener(monitorButtonWHKill);
				
		JButton buttonStart = new JButton("����ϵͳ");
		MonitorButtonStart monitorButtonStart = new MonitorButtonStart();
		buttonStart.addActionListener(monitorButtonStart);
		JButton buttonStop = new JButton("ֹͣϵͳ");
		MonitorButtonStop monitorButtonStop = new MonitorButtonStop();
		buttonStop.addActionListener(monitorButtonStop);
		panel_1.add(buttonWH);
		panel_1.add(buttonWHKill);
		panel_1.add(buttonAC);
		panel_1.add(buttonACKill);	
		panel_1.add(buttonCD);
		panel_1.add(buttonCDKill);
		panel_1.add(buttonEV);
		panel_1.add(buttonEVKill);
		add(panel_1, BorderLayout.CENTER);
		Panel panel_2 = new Panel();
		panel_2.add(buttonStart);
		panel_2.add(buttonStop);
		add(panel_2, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	class MonitotButtonCD implements ActionListener			//������ϴ������������ʱ����ʱ����������������ͬ
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && !Control.clothesDry.isOn)
			{
				Control.clothesDry.beToOn = true;
				Control.clothesDry.isDelayStart = false;
				Control.clothesDry.isGood = false;
				System.out.println("��ϴ��.׼������###############################");
				if (Control.clothesDry.currentDryTime >= 50)
				{
					Control.clothesDry.currentDryTime = 0;
					Control.clothesDry.isGood = false;
				}
				tryToOpenApp(2);
			}			
		}
		
	}
	class MonitotButtonCDKill implements ActionListener			//�رո�ϴ��������������ͬ
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart)
			{
				if (Control.clothesDry.isOn)
				{
					Control.clothesDry.isOn = false;					
					System.out.println("��ϴ��.clothesDry is turned off��ֹͣ����!");
				}
			}			
		}
		
	}
	class MonitorButtonEV implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && !Control.electronicVehicle.isOn)
			{
				Control.electronicVehicle.beToOn = true;
				Control.electronicVehicle.isDelayStart = false;
				Control.electronicVehicle.isGood = false;
				System.out.println("�������.׼������###############################");				
				if (Control.electronicVehicle.currentChargeTime >= 13)
				{
					Control.electronicVehicle.isGood = false;
					Control.electronicVehicle.currentChargeTime = 0;
				}
				tryToOpenApp(3);
			}			
		}
	}
	class MonitorButtonEVKill implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && Control.electronicVehicle.isOn)
			{
				Control.electronicVehicle.isOn = false;
				System.out.println("����.electronicVehicle is turned off��ֹͣ����!");
			}				
		}
	}
	class MonitorButtonAC implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && !Control.airCondition.isOn)
			{
				Control.airCondition.beToOn =true;
				Control.airCondition.isGood = false;
				Control.airCondition.isDelayStart = false;
				System.out.println("�յ�.׼������###############################");
				tryToOpenApp(1);
			}				
		}
	}
	class MonitorButtonACKill implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && Control.airCondition.isOn)
			{
				Control.airCondition.isOn = false;
				System.out.println("�յ�.airCondition is turned off��ֹͣ����!");
			}
		}
	}
	class MonitorButtonWH implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && ! Control.waterHeater.isOn)
			{
				Control.waterHeater.beToOn = true;
				Control.waterHeater.isGood = false;
				Control.waterHeater.isDelayStart = false;
				System.out.println("��ˮ��.׼������###############################");
				tryToOpenApp(0);
			}					
		}
	}
	class MonitorButtonWHKill implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart && Control.waterHeater.isOn)
			{
				Control.waterHeater.isOn = false;
				Control.totalPower -= Control.waterHeater.power;
				System.out.println("��ˮ��.waterHeater is turned off��ֹͣ����!");
			}
		}
	}
	class MonitorButtonStart implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{				
			if (!Control.isStart)
			{
				System.out.println("ϵͳ��������...");
				Control.isStart = true;
				System.out.println("ϵͳ�Ѿ�������");
			}				
		}
	}
	class MonitorButtonStop implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Control.isStart)
			{
				Control.isStart = false;
				System.out.println("ϵͳ���ڹر�...");
				try
				{
					Thread.sleep(5000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("ϵͳ�Ѿ��رգ�");			
				Control.isInit = false;
			}			
		}
	}
	
	static void tryToOpenApp(int i)		//���Դ򿪵��������������ʱ�򿪣�����ʱ��
	{
		if (Control.appliance[i].beToOn && !Control.appliance[i].isDelayStart)
		{
			Control.appliance[i].delayTime = Control.delayStartOnPeriod(i);
		}		
	}
}





