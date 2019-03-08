/**
 * 
 */
package com.swanand.salestax.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.input.IInputListener;
import com.swanand.salestax.output.OutputGeneratorInvoker;
import com.swanand.salestax.utility.ExceptionHandler;

/**
 * @author swanand.ganapatye
 *Main class responsible for Initiation and execution of all modules. 
 *This is also responsible to end all the threads before shutting down the application.
 */
public class Main {
	
	private static BlockingQueue<Future<IOrder>> inputQueue ;
	private static BlockingQueue<Future<IOrder>> outputQueue ;
	private static Observable shutDownObservable = new ShutDownObservable();
	
	/**
	 * @return the inputQueue
	 */
	public static BlockingQueue<Future<IOrder>> getInputQueue() {
		return inputQueue;
	}

	/**
	 * @return the outputQueue
	 */
	public static BlockingQueue<Future<IOrder>> getOutputQueue() {
		return outputQueue;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			Properties properties = ConfigCache.getPropertis();
			String queueSizeStr = properties.getProperty("queueSize");
			int queueSize;
			try{
				queueSize = Integer.valueOf(queueSizeStr);
			}catch(Exception e){
				queueSize = 5;
			}
			
			inputQueue = new ArrayBlockingQueue<Future<IOrder>>(queueSize);
			outputQueue = new ArrayBlockingQueue<Future<IOrder>>(queueSize);
			
			IInputListener inputListener = ConfigCache.getInputListener(); 
			inputListener.setInputQueue(inputQueue);
			new Thread(inputListener).start();
			shutDownObservable.addObserver(inputListener);
			
			SalesTaxProcessorInvoker salesTaxProcessorInvoker =  SalesTaxProcessorInvoker.getInstance();
			salesTaxProcessorInvoker.setInputProcessQueue(inputQueue);
			salesTaxProcessorInvoker.setOutputProcessQueue(outputQueue);
			new Thread(salesTaxProcessorInvoker).start();
			shutDownObservable.addObserver(salesTaxProcessorInvoker);
			
			OutputGeneratorInvoker outputGeneratorInvoker = OutputGeneratorInvoker.getInstance();
			outputGeneratorInvoker.setOutputProcessQueue(outputQueue);
			new Thread(outputGeneratorInvoker).start();
			shutDownObservable.addObserver(outputGeneratorInvoker);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					System.out.println("Application is shutting down ...");
					shutDownObservable.notifyObservers();
				}
			});
			
			BufferedReader br;
			
			br = new BufferedReader(new InputStreamReader(System.in));
			try{
				System.out.println("Enter 'Exit' to shut down the application");
				while (true) {
		            String input = br.readLine();
		
		            if ("Exit".equalsIgnoreCase(input)) {
		                System.exit(0);
		            }
		
		        }
			} catch (IOException e) {
				throw new SalesTaxException("Something went wrong while reading console ",e);
			}finally{
				br.close();
			}
		}catch(Throwable e){
			ExceptionHandler.getInstance().handleThrowable(e);
		}


	}

}
