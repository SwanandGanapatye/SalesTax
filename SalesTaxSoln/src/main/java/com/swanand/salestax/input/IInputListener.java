package com.swanand.salestax.input;

import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import com.swanand.salestax.dto.IOrder;

public interface IInputListener extends Runnable, Observer{

	void setInputQueue(BlockingQueue<Future<IOrder>> inputQueue);

	void setShutDownCalled(boolean isShutDownCalled);

}
