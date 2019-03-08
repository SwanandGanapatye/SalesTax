package com.swanand.salestax.process;

import java.util.Observable;

/**
 * 
 * @author swanand.ganapatye
 *This class is observable that is used by other non demon threads to look for application shut down event.
 */
public class ShutDownObservable extends Observable {

	@Override
	public void notifyObservers(){
		setChanged();
		super.notifyObservers();
	}
}
