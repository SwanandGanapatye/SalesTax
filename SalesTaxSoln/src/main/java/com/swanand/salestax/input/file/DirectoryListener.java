/**
 * 
 */
package com.swanand.salestax.input.file;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swanand.salestax.dto.IOrder;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.input.IInputListener;
import com.swanand.salestax.process.ConfigCache;
import com.swanand.salestax.utility.ExceptionHandler;


/**
 * @author swanand.ganapatye
 *
 *This configurable singleton class is responsible for reading input files from configured directory
 *and passing it to fileinputProcessor for processing. 
 *
 *It acts as producer for input pool.
 *
 *It also implements Observer design pattern to check system shut down event to end 
 *  processing threads
 */
public final class DirectoryListener implements IInputListener {

	private String inputDirectoryPath ;
	private String archiveDirectoryPath ;
	private String fileExtension ;
	private DateFormat dateFormat;
	private ExecutorService inputThreadExecutor = Executors.newFixedThreadPool(5);
	private boolean isShutDownCalled;
	private  BlockingQueue<Future<IOrder>> inputQueue ;
	private long  directoryListnerSleepTime;
	
	private static DirectoryListener directoryListener = null;
	
	private DirectoryListener(){
		Properties properties = ConfigCache.getPropertis();
		
		 inputDirectoryPath = (properties.getProperty("inputDirectoryPath")!= null)?properties.getProperty("inputDirectoryPath").trim():".";
		 archiveDirectoryPath = (properties.getProperty("archiveDirectoryPath")!= null)?properties.getProperty("archiveDirectoryPath").trim():".";
			
		 fileExtension = (properties.getProperty("inputFileExtension")!= null)?properties.getProperty("inputFileExtension").trim():"input"; //handle PatternSyntaxException
		 try{
			 String dateFormatString = (properties.getProperty("dateFormatString")!= null)?properties.getProperty("dateFormatString").trim():"yyyy-MM-dd-HH-mm-ss-SSS";
			 dateFormat = new SimpleDateFormat(dateFormatString);
			 String time = (properties.getProperty("directoryListnerSleepTime")!= null)?properties.getProperty("directoryListnerSleepTime").trim():"2000";
			directoryListnerSleepTime = Long.valueOf(time);
		 }catch(Exception e){
			 dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
			 directoryListnerSleepTime = 2000;
		 }
	}
	
	public static DirectoryListener getInstance(){
		if (directoryListener == null){
			synchronized (DirectoryListener.class) {
				if (directoryListener == null){
					directoryListener = new DirectoryListener();
				}
			}
		}
		return directoryListener;
	}
	

	/**
	 * @param inputQueue the inputQueue to set
	 */
	public void setInputQueue(BlockingQueue<Future<IOrder>> inputQueue) {
		this.inputQueue = inputQueue;
	}

	/**
	 * @param isShutDownCalled the isShutDownCalled to set
	 */
	public void setShutDownCalled(boolean isShutDownCalled) {
		this.isShutDownCalled = isShutDownCalled;
	}

	public void run() {
		try{
			File inputDir = new File (this.inputDirectoryPath);
			if(inputDir.exists() && inputDir.isDirectory()){
				while(!isShutDownCalled){
					try{
						try {
							File[] listFiles = inputDir.listFiles();
							for( File file : listFiles){
								if(matchPattern("^.*\\."+fileExtension+"$", file.getName())){
									File fileToProcess = moveFile(file);
									FileInputReader fileInputReader = new FileInputReader(); 
									fileInputReader.setFile(fileToProcess);
									Future<IOrder> future = inputThreadExecutor.submit(fileInputReader);
									inputQueue.put(future);
								}
							}
							Thread.sleep(directoryListnerSleepTime);
						} catch (InterruptedException e) {
							throw new SalesTaxException("DirectoryListner thread iturrupted ",e);
						}catch (Exception e){
							throw new SalesTaxException("Somthing went wrong while listning input directory \n "+inputDirectoryPath,e);
						}
					}catch (Throwable e) {
							ExceptionHandler.getInstance().handleThrowable(e);
					}
				}
			}else{
				throw new Error("Input directory path "+inputDirectoryPath+"' does not exist. \n Please configure it properly in configuration.properties.");
			}
		}catch(Throwable t){
			ExceptionHandler.getInstance().handleThrowable(t);
		}
		
	}
	
	private File moveFile(File file) {
		Date date = new Date();
		File fileToProcess = new File(this.archiveDirectoryPath+file.getName()+"_"+dateFormat.format(date));
		file.renameTo(fileToProcess);
		return fileToProcess;
		
	}

	private static boolean matchPattern(String pattern, String stringToMatch) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(stringToMatch);
		return m.matches();
	}
	

	public void update(Observable arg0, Object arg1) {
		try{
			isShutDownCalled = Boolean.TRUE;
			System.out.println("Shutting down directory listener");
			inputThreadExecutor.shutdown();
		}catch(Throwable e){
			ExceptionHandler.getInstance().handleThrowable(e);
		}
	}
	
	

}
