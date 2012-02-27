package raytracing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RayTracingRMI extends Remote {
    clojure.lang.Keyword ping() throws RemoteException;
    java.awt.Color getPixel(Object root_object, 
    	                    clojure.lang.PersistentVector lights, 
    	                    Object projection, 
    	                    clojure.lang.PersistentVector coords) throws RemoteException;
}