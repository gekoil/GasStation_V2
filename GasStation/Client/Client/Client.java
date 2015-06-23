package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.LinkedList;

import BL.ClientCar;
import Listeners.ClientListener;

public class Client extends Thread{
	private static int SERVER_PORT = 9090;

	private Socket socket = null;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private LinkedList<ClientListener> listeners;
	private boolean endOfConnection = false;
	
	public Client() {
		listeners = new LinkedList<ClientListener>();
	}
	
	public void run() {
		try {
			socket = new Socket("localhost", SERVER_PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			for(ClientListener l : listeners)
				l.updateConectionStatus(true);
			while (!endOfConnection) {
				Runnable runInput = new Runnable() {
					@Override
					public void run() {
						carReceiver();
					}
				};
				new Thread(runInput).start();
				join();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	}
	
	public void sendCar(ClientCar car) {
		try {
			outputStream.reset();
			outputStream.writeObject(car);
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	private void carReceiver() {
		try {
			Object temp = inputStream.readObject();
			if (temp == null) {
				endOfConnection = true;
			} else if(temp instanceof ClientCar)  {
				ClientCar car =(ClientCar) temp;
				for(ClientListener l : listeners)
					l.updateCarInfo(car);
			}
			else {
				for(ClientListener l : listeners)
					l.fireIlligalObject();
			}
		} catch (ClassNotFoundException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void registerListener(ClientListener lis) {
		listeners.add(lis);
	}
	
	public void endOfConection() {
		sendCar(null);
	}
}
