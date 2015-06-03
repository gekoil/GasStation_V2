package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import BL.ClientCar;
import Listeners.ClientListener;

public class Client {
	
	private Socket socket = null;
	private ObjectInputStream inputStream;
	private ObjectOutput outputStream;
	private LinkedList<ClientListener> listeners = new LinkedList<ClientListener>();
	
	public Client() {
		try {
			socket = new Socket("localhost", 7000);
			inputStream = new ObjectInputStream(socket.getInputStream()); 
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {	System.err.println(e);
		} 
	}
	
	public void sendCar(ClientCar car) {
		try {
			outputStream.writeObject(car);
			carReceiver();
		} catch (IOException e) {
			for(ClientListener l : listeners)
				l.fireMessage(e.getMessage());
		}
	}
	
	public void carReceiver() {
		ClientCar car;
		try {
			Object temp = inputStream.readObject();
			if(temp instanceof ClientCar)  {
				car =(ClientCar) temp;
			}
			else if(temp instanceof String)
				for(ClientListener l : listeners)
					l.fireMessage((String)temp);
			else {
				for(ClientListener l : listeners)
					l.fireIlligalObject();
			}
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			
		}
		
	}
	
	public void registerListener(ClientListener lis) {
		listeners.add(lis);
	}
	
}