/**
 * 
 */
package com.swanand.salestax.process;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.utility.ExceptionHandler;

/**
 * @author swanand.ganapatye
 * This singleton thread is responsible for consuming the inputs from input pool 
 * and passing it to salesTaxProcessor threads.
 * 
 * It also puts processed output in output pool which then will be consumed by output generator.
 * 
 *  It also implements Observer design pattern to check system shut down event to end 
 *  processing threads.
 * 
 */
public class SalesTaxProcessorInvoker implements Runnable, Observer {
	
	private static SalesTaxProcessorInvoker salesTaxProcessorInvoker;
	private ExecutorService processThreadExecutor = Executors.newFixedThreadPool(5);

	private BlockingQueue<Future<IOrder>> inputProcessQueue ;
	private BlockingQueue<Future<IOrder>> outputProcessQueue ;
	
	private boolean isShutDownCalled;
	

	private SalesTaxProcessorInvoker(){}
	/**
	 * @param inputProcessQueue the inputProcessQueue to set
	 */
	public void setInputProcessQueue(BlockingQueue<Future<IOrder>> inputProcessQueue) {
		this.inputProcessQueue = inputProcessQueue;
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

/**
 * instance method for singleton class
 * @return
 */
	public static SalesTaxProcessorInvoker getInstance(){
		if (salesTaxProcessorInvoker == null){
			synchronized (SalesTaxProcessorInvoker.class) {
				if (salesTaxProcessorInvoker == null){
					salesTaxProcessorInvoker = new SalesTaxProcessorInvoker();
				}
			}
		}
		return salesTaxProcessorInvoker;
	}
/**
 * This thread takes input from input pool passes it to processor and then put processed output 
 * to output pool 
 */

	public void run() {

		while(!isShutDownCalled){
			try{
				try {
					Future<IOrder> future = inputProcessQueue.take();
					SalesTaxProcessor salesTaxProcessor = new SalesTaxProcessor();
					while(!future.isDone()){
						Thread.sleep(500);
					}	
					if (future.get() != null){
						salesTaxProcessor.setOrder(future.get());
						future = processThreadExecutor.submit(salesTaxProcessor); //reusing same reference variable for output
						outputProcessQueue.put(future);
					}else{
						throw new SalesTaxException("Something went wrong while processing order");
					}
				} catch (InterruptedException e) {
					throw new SalesTaxException("Something went wrong while processing order",e);
				} catch (ExecutionException e) {
					throw new SalesTaxException("Something went wrong while processing order",e);
				}
			}catch(Throwable e){
				ExceptionHandler.getInstance().handleThrowable(e);
			}
		}
	}
	public void update(Observable arg0, Object arg1) {
		try{
			isShutDownCalled = Boolean.TRUE;
			System.out.println("Shutting down process executor");
			processThreadExecutor.shutdown();
		}catch(Throwable e){
			ExceptionHandler.getInstance().handleThrowable(e);
		}
	}
}
