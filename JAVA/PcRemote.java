/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PC_remote;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import jssc.SerialPortException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 *
 * @author Sid
 */
public class PcRemote {

    /**
     * @param args the command line arguments
     */
   JFrame jf1;
   Container cnt;
   Dimension size;
   Insets insets;
   JComboBox cb;
   int ind;
   SerialPort comPort;
   String portNames[];
   jssc.SerialPort serialPort;
   
   boolean threadExit,threadExit2;
   String code;
   T t;
   //T1 t1;
   
   //final int CLICK_TIME=500;
   int MOUSE_MOVE_CONS=3;
   float MOUSE_MOVE_MUL_FAC=1f;
   final float MOUSE_MOVE_MUL_FAC_CONS=2.0f;
   int prevCode;
   long sys_time_key;
   
   Robot rb;
   
   String rCode[]={
    "ff02fd",
    "ff827d",
    "ffba45",
    "ff3ac5",
    "ff22dd",
    "ffa25d",
    "ff9a65",
    "ff1ae5",
    "ff12ed",
    "ff926d",
    "ffaa55",
    "ff2ad5",
    "ff32cd",
    "ffb24d",
    "ff8a75",
    "ff0af5",
    "ffc23d",
    "ff629d",
    "ff52ad",
    "ff42bd",
    "ffe21d",
    "ffb847",
    "ff7887",
    "fff807",
    "ff38c7",
    "ffd827",
    "ff58a7",
    "ff9867",
    "ff18e7",
    "ffe817",
    "ff6897",
    "ffa857",
    "ff28d7",
    "ffc837",
    "ff48b7",
    "ff8877",
    "ff08f7",
    "fff00f",
    "ff708f",
    "ffb04f",
    "ffb04f",
    "ff30cf",
    "ffd02f",
    "ff50af",
    "ff906f",
    "ff10ef",
    "ffe01f",
    "ff609f",
    "ffa05f",
    "ff20df",
    "ffc03f",
    "ff40bf",
    "ff807f",
    "ff00ff"
    };

    public PcRemote() {
       try {
           rb=new Robot();
       } catch (AWTException ex) {
           rb=null;
           System.exit(-1);
       }
        threadExit=false;
        threadExit2=true;
        ind=0;
        code="";
        size=new Dimension();
        jf1=new JFrame("Remote");
        cnt=jf1.getContentPane();			
        cnt.setLayout(null);
        insets = cnt.getInsets();
        SerialPort comPort2[] = SerialPort.getCommPorts();
        portNames = jssc.SerialPortList.getPortNames();        
        
        cb=new JComboBox();  
        //System.out.println(portNames[0]+" : "+comPort2[0].getSystemPortName());        
        for(int i=0;i<portNames.length;i++){
            for(int j=0;j<comPort2.length;j++){
                if(portNames[i].compareToIgnoreCase(comPort2[j].getSystemPortName())==0){
                    cb.addItem(comPort2[j].getDescriptivePortName());                        
                }
            }
        } 
        
        cnt.add(cb);
        
        Al ai=new Al();
        cb.addActionListener(ai);
        
         T1 t1=new T1();
         t1.start();
        
        
        
       
        size = cb.getPreferredSize();
       	cb.setBounds(10 + insets.left, 10 + insets.top,
                                    size.width, size.height);		 
        
        jf1.setBounds( 450+ insets.left, 100+ insets.top,300,100);   
        jf1.setVisible(true);
        jf1.show();
        jf1.setResizable(false);
        //jf1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        jf1.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("close");
                threadExit=false;
                threadExit2=false;
                t1.stop();                
                    if(comPort!=null){
                        comPort.closePort();
                        comPort.removeDataListener();
                    }
                    if(serialPort!=null){
                        try {
                            serialPort.closePort();
                        } 
                        catch (Exception ex) {
                        }
                        
                }                
            }
        });
                
    }
    
    private  class Al implements ActionListener{
	   	public void actionPerformed(ActionEvent event){  
                    
                    ind=cb.getSelectedIndex();     
                    cb.enable(false);
                   /*
                    comPort = SerialPort.getCommPorts()[0];                    
                    System.out.println(ind+"    "+comPort.getDescriptivePortName());
                    comPort.openPort();
                    
                    comPort.addDataListener(new SerialPortDataListener() {
                       @Override
                       public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                       @Override
                       public void serialEvent(SerialPortEvent event)
                       {
                           
                          try{
                          if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
                             return;
                          }
                          //byte[] newData = new byte[comPort.bytesAvailable()];
                          //int numRead = comPort.readBytes(newData, newData.length);
                           //for (int i = 0; i < numRead; i++) {
                            //   System.out.print(Integer.toHexString(new Integer(new String(newData))));
                           //}
                           //System.out.println("");
                           
                           
                                
                           
                           byte[] newData = new byte[comPort.bytesAvailable()];
                           
                           int numRead = comPort.readBytes(newData, newData.length);
                           String tem="";
                           for (int i = 0; i < numRead; ++i){                             
                              tem+=(char)newData[i];
                           }
                           //System.out.println(tem);
                           if(tem.charAt(tem.length()-1)=='\n'){
                               System.out.println(code);
                               code="";
                           }
                           code+=tem;

                       }
                       catch(Exception e){}
                       }                       
                    }); */           
                    
                    serialPort = new jssc.SerialPort(portNames[ind]);                         
                        
                        System.out.println(ind+" : "+portNames[ind]);                        
                        
                        try{
                                                  
                            
                            threadExit=true;
                            t=new T();
                            t.start();
                            //t1=new T1();
                            //t1.start();
                        }
                        catch(Exception e){}
                }
    }
    
    class T extends Thread{

        @Override
        public void run() {
            super.run(); 
            if(serialPort!=null) {                
            
                try {
                    serialPort.openPort();                        
                    
                            serialPort.setParams(jssc.SerialPort.BAUDRATE_9600, 
                                             jssc.SerialPort.DATABITS_8,
                                             jssc.SerialPort.STOPBITS_1,
                                             jssc.SerialPort.PARITY_NONE);
                     
                        String x="";
                        
                        while(threadExit){
                            /*if(serialPort!=null || (!serialPort.isOpened())) {
                                continue;
                            }*/
                            String s=serialPort.readString();   
                            
                            if(s!=null && s!="" && s.compareToIgnoreCase("")!=0 && !s.equals("")){  
                                for (int i = 0; i < s.length(); i++) {
                                    //System.out.print(s.charAt(i)+" ("+(int)s.charAt(i)+") ");                                                
                                    if(s.charAt(i)==10){
                                        /*if(x.length()!=6){
                                            System.out.println("\t\t\t\t\t\t\t\t\t Error");
                                        }
                                        System.out.println("\n=======================\n"+x+" ("+x.length()+")\n=======================\n ");
                                        */
                                        action(x);
                                        x="";
                                    }
                                    else{
                                        x+=String.valueOf(s.charAt(i));
                                    }
                                }
                            }
                        }
                }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                }
            }
        }                      
        
    
    
    void action(String code){        
        //System.out.print("code: "+code);
        try{
            for(int i=0;i<rCode.length;i++){                
                if(code.compareTo(rCode[i])==0){
                    //System.out.println("code pos: "+i+"   ;    pc: "+i+"   ;   prec: "+prevCode+"   ;  "+MOUSE_MOVE_MUL_FAC+"              ;     "+sys_time_key+"     ;    "+System.currentTimeMillis());
                    sys_time_key=System.currentTimeMillis();
                    int mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                    int mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY();
                    if(i==0){
                        rb.keyPress(KeyEvent.VK_ENTER);
                        rb.keyRelease(KeyEvent.VK_ENTER);
                    }
                    else if(i==1){
                        rb.keyPress(KeyEvent.VK_BACK_SPACE);
                        rb.keyRelease(KeyEvent.VK_BACK_SPACE);
                    } 
                    else if(i==2){
                        rb.keyPress(KeyEvent.VK_ESCAPE);
                        rb.keyRelease(KeyEvent.VK_ESCAPE);
                    } 
                    else if(i==3){
                        rb.keyPress(KeyEvent.VK_SPACE);
                        rb.keyRelease(KeyEvent.VK_SPACE);
                    }                                        
                    else if(i==16){
                        //System.out.println("Right click hold");
                        rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    }
                    else if(i==17){
                        //System.out.println("Right click realeas");
                        rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }
                    else if(i==18){
                        //System.out.println("Up"); 
                        if(prevCode==18){
                            MOUSE_MOVE_MUL_FAC+=MOUSE_MOVE_MUL_FAC_CONS;
                        }
                        else{
                            MOUSE_MOVE_MUL_FAC=1f;
                        }
                        rb.mouseMove(mouseX, mouseY-(int)(MOUSE_MOVE_CONS*MOUSE_MOVE_MUL_FAC));
                       
                    }
                    else if(i==19){
                        //System.out.println("left");
                        if(prevCode==19){
                            MOUSE_MOVE_MUL_FAC+=MOUSE_MOVE_MUL_FAC_CONS;
                        }
                        else{
                            MOUSE_MOVE_MUL_FAC=1f;
                        }
                        rb.mouseMove(mouseX-(int)(MOUSE_MOVE_CONS*MOUSE_MOVE_MUL_FAC), mouseY);
                    }
                    else if(i==20){                    
                        //System.out.println("click");                    
                        rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }
                    else if(i==21){
                        //System.out.println("right");
                        if(prevCode==21){
                            MOUSE_MOVE_MUL_FAC+=MOUSE_MOVE_MUL_FAC_CONS;
                        }
                        else{
                            MOUSE_MOVE_MUL_FAC=1f;
                        }
                        rb.mouseMove(mouseX+(int)(MOUSE_MOVE_CONS*MOUSE_MOVE_MUL_FAC),mouseY);
                    }
                    else if(i==22){
                        //System.out.println("down");
                        if(prevCode==22){
                            MOUSE_MOVE_MUL_FAC+=MOUSE_MOVE_MUL_FAC_CONS;
                        }
                        else{
                            MOUSE_MOVE_MUL_FAC=1f;
                        }
                        rb.mouseMove(mouseX, mouseY+(int)(MOUSE_MOVE_CONS*MOUSE_MOVE_MUL_FAC));
                    }
                    else if(i==23){
                        //System.out.println("Right double Click");
                        rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(10);
                        rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }
                    else if(i==24){
                        //System.out.println("Left Click");
                        rb.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        rb.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    }
                    else if(i==26){
                        rb.keyPress(KeyEvent.VK_SHIFT);
                    }
                    else if(i==27){                        
                        rb.keyRelease(KeyEvent.VK_SHIFT);
                    }
                    else if(i==46){                        
                        
                    }
                    else if(i==47){
                        
                    }
                    else if(i==48){
                        //scrool up
                        rb.mouseWheel(1);
                    }
                    else if(i==49){
                        //schroll down
                        rb.mouseWheel(-1);
                    }
                    else if(i==50){   
                        //nav key
                        rb.keyPress(KeyEvent.VK_UP);
                        rb.keyRelease(KeyEvent.VK_UP);
                    }
                    else if(i==51){
                        rb.keyPress(KeyEvent.VK_DOWN);
                        rb.keyRelease(KeyEvent.VK_DOWN);
                    }
                    else if(i==52){                  
                        rb.keyPress(KeyEvent.VK_LEFT);
                        rb.keyRelease(KeyEvent.VK_LEFT);
                    }
                    else if(i==53){                        
                        rb.keyPress(KeyEvent.VK_RIGHT);                        
                        rb.keyRelease(KeyEvent.VK_RIGHT);                        
                    }                 
                    
                    prevCode=i;
                }
            }
        }
        catch(Exception e){}
    }
    
    class T1 extends Thread{        
        
        @Override
        public void run() {            
            while(threadExit2){
                if(System.currentTimeMillis()-sys_time_key>250){
                    prevCode=0;
                }                
            }
        }        
    }
    
    
    public static void main(String[] args) {
        //SerialPort comPort[] = SerialPort.getCommPorts();
        //System.out.println(comPort[1].getDescriptivePortName());
        new PcRemote();
    }
    
}
