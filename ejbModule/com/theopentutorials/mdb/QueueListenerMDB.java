package com.theopentutorials.mdb;
 
import java.io.IOException;
import java.util.Date;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.pureblue.quant.quantAPI.YahooHistoryStock;
import com.pureblue.quant.util.ConfigPropValue;
import com.pureblue.quant.util.HttpUtil;
import com.theopentutorials.mdb.to.Employee;
 
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
        propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(
        propertyName = "destination", propertyValue = "queue/MyQueue") })
public class QueueListenerMDB implements MessageListener {
    public QueueListenerMDB() {
    }
 
    public void onMessage(Message message) {
    	Logger logger = Logger.getLogger(getClass());
        try {
            if (message instanceof TextMessage) {
                System.out.println("Queue: I received a TextMessage at "
                                + new Date());
                TextMessage msg = (TextMessage) message;
                System.out.println("Message is : " + msg.getText());
            } else if (message instanceof ObjectMessage) {
                System.out.println("Queue: I received an ObjectMessage at "
                                + new Date());
                ObjectMessage msg = (ObjectMessage) message;
                Employee employee = (Employee) msg.getObject();
                System.out.println("Employee Details: ");
                System.out.println(employee.getId());
                System.out.println(employee.getName());
                System.out.println(employee.getDesignation());
                System.out.println(employee.getSalary());
                try {
					System.out.println("all versions of log4j Logger: " + getClass().getClassLoader().getResources("org/apache/log4j/Logger.class") );
				} catch (IOException e) {
					e.printStackTrace();
				}
                
                logger.fatal("YahooHistoryStock::fetchActions: start");
        		YahooHistoryStock api = new YahooHistoryStock();
        		api.fetchActions();
            } else {
                System.out.println("Not a valid message for this Queue MDB");
            }
 
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}