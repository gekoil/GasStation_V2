package Listeners;

import BL.Car;
import com.sun.istack.internal.Nullable;
import java.net.InetAddress;
import java.net.Socket;

public interface UICarCreatorListener {
	Car createNewCar(int liters, boolean wash, int pump, @Nullable Socket owner);
}
