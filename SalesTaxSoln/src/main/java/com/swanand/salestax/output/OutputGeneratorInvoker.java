package com.swanand.salestax.output;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.process.ConfigCache;
import com.swanand.salestax.utility.ExceptionHandler;


/**
 * 
 * @author swanand.ganapatye
 *This singleton thread is responsible for consuming the output from output pool 
 * and passing it to configurable outputgenerator threads.
 * 
 *  It also implements Observer design pattern to check system shut down event to end 
 *  processing threads.
 */
public class OutputGeneratorInvoker implements Runnable, Observer{

	private static OutputGeneratorInvoker outputGeneratorInvoker;
	private ExecutorService outputThreadExecutor = Executors.newFixedThreadPool(5);

	private BlockingQueue<Future<IOrder>> outputProcessQueue ;
	
	private boolean isShutDownCalled;
	
	private OutputGeneratorInvoker(){
		
	}
	
	public static OutputGeneratorInvoker getInstance(){
		if (outputGeneratorInvoker == null){
			synchronized (OutputGeneratorInvoker.class) {
				if (outputGeneratorInvoker == null){
					outputGeneratorInvoker = new OutputGeneratorInvoker();
				}
			}
		}
		return outputGeneratorInvoker;
	}


	/**
	 * @param outputProcessQueue the outputProcessQueue to set
	 */
	public void setOutputProcessQueue(
			BlockingQueue<Future<IOrder>> outputProcessQueue) {
		this.outputProcessQueue = outputProcessQueue;
	}

	/**
	 * @param isShutDown the isShutDown to set
	 */
	public void setShutDown(boolean isShutDown) {
		this.isShutDownCalled = isShutDown;
	}

	public void run() {
		while(!isShutDownCalled){
			try{
				try {
					Future<IOrder> future = outputProcessQueue.take();
					while(!future.isDone()){
						Thread.sleep(500);
					}
					IOutputGenerator outputGenerator = ConfigCache.getOutputGenerator();
					outputGenerator.setOrder(future.get());
					outputThreadExecutor.submit( outputGenerator);
				} catch (InterruptedException e) {
					throw new SalesTaxException("Something went wrong while processing output",e);
				} catch (ExecutionException e) {
					throw new SalesTaxException("Something went wrong while processing output",e);
				}
			}catch(Throwable e){
				ExceptionHandler.getInstance().handleThrowable(e);
			}
		}
	}

	public void update(Observable arg0, Object arg1) {
		try{
			isShutDownCalled = Boolean.TRUE;
			System.out.println("Shutting down output generator");
			outputThreadExecutor.shutdown();
		}catch(Throwable e){
			ExceptionHandler.getInstance().handleThrowable(e);
		}
	}

	
	

}
