package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import BL.ClientCar;
import Listeners.ClientListener;

public class Client extends Thread{
	private static int SERVER_PORT = 9090;

	private Socket socket = null;
	private ObjectInputStream inputStream;
	private ObjectOutput outputStream;
	private LinkedList<ClientListener> listeners = new LinkedList<ClientListener>();
	
	public void run() {
		try {
			socket = new Socket("localhost", SERVER_PORT);
			inputStream = new ObjectInputStream(socket.getInputStream()); 
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {	System.err.println(e);
		} 
	}
	
	public void sendCar(ClientCar car) {
		try {
			outputStream.writeObject(car);
			if(car.getId() > 0) {
				carReceiver();
			} else {
				socket.close();
			}

		} catch (IOException e) {
			for(ClientListener l : listeners)
				l.fireEndOfConection();
		}
	}
	
	public void carReceiver() {
		ClientCar car;
		try {
			Object temp = inputStream.readObject();
			if (temp == null) {
				return;
			} else if(temp instanceof ClientCar)  {
				car =(ClientCar) temp;
			}
			else {
				for(ClientListener l : listeners)
					l.fireIlligalObject();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

	}
	
	public void registerListener(ClientListener lis) {
		listeners.add(lis);
	}
	
	public void endOfConection() {
		sendCar(null);
	}
}
