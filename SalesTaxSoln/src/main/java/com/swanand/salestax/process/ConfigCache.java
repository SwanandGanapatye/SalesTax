package com.swanand.salestax.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import com.swanand.salestax.exception.SalesTaxError;
import com.swanand.salestax.exception.SalesTaxException;
import com.swanand.salestax.input.IInputListener;
import com.swanand.salestax.output.IOutputGenerator;
import com.swanand.salestax.utility.ExceptionHandler;

public class ConfigCache {
	
	private static Properties propertis = new Properties();
	/**
	 * @return the propertis
	 */
	public static Properties getPropertis() {
		return propertis;
	}

	static{
		try{
			InputStream fileInputStream = null;
			try {
				
				fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.properties");//new FileInputStream("configuration.properties");
				if(fileInputStream!= null){
					propertis.load(fileInputStream);
				}else{
					propertis.put("exemptedList", "chocolate,book,pills");//default hardcoded properties.
				}
			} catch (FileNotFoundException e) {
				throw new SalesTaxException("configuartion file not found Default settings are used", e);
			} catch (IOException e) {
				throw new SalesTaxException("configuartion file not readable. Default settings are used", e);
			}finally{
				if (fileInputStream != null){
					fileInputStream.close();
				}
			}
		}catch(Throwable t){
			System.out.println("Configuration cannot be loaded. Default settings are used");
			ExceptionHandler.getInstance().handleThrowable(t);
		}
	}

	public static IInputListener getInputListener() {
		IInputListener iInputListener = null ;
		try{
			try {
				String inputGeneratorClassName = (propertis.getProperty("inputGenerator")!= null)?propertis.getProperty("inputGenerator").trim()
						:"com.swanand.salestax.input.file.DirectoryListener";
				Class<?> inputListnerClass = Class.forName(inputGeneratorClassName);
				Method method = inputListnerClass.getMethod("getInstance");
				iInputListener = (IInputListener)method.invoke(null, (Object[])null);
		
			} catch (ClassNotFoundException e) {
				throw new SalesTaxError("Input listener class configured does not exist. ",e);
			} catch (SecurityException e) {
				throw new SalesTaxError("Input listener initialization failed ",e);
			} catch (NoSuchMethodException e) {
				throw new SalesTaxError("Input listener initialization failed ",e);
			} catch (IllegalArgumentException e) {
				throw new SalesTaxError("Input listener initialization failed ",e);
			} catch (IllegalAccessException e) {
				throw new SalesTaxError("Input listener initialization failed ",e);
			} catch (InvocationTargetException e) {
				throw new SalesTaxError("Input listener initialization failed ",e);
			} catch (ClassCastException e){
				throw new SalesTaxError("Input listener class configured does not implement IInputListener interface",e);
			}catch (Exception e) {
				throw new SalesTaxError("Input listener initialization failed ",e);
			} 
		}catch(Throwable t){
			if(propertis.getProperty("inputGenerator") != null){
				propertis.remove("inputGenerator");
				iInputListener = getInputListener();
				ExceptionHandler.getInstance().showPopUp("Input listener initialization failed, Switching to default input",t);
			}else{
				ExceptionHandler.getInstance().handleThrowable(t);
			}
		}
		return iInputListener;
	}

	public static IOutputGenerator getOutputGenerator() {
		IOutputGenerator outputGenerator = null;
		try{
			try {
				String outputGeneratorClassName = (propertis.getProperty("outputGenerator")!= null)?propertis.getProperty("outputGenerator").trim()
						:"com.swanand.salestax.output.file.FileOutputGenerator";
				Class<?> outputGeneratorClass = Class.forName(outputGeneratorClassName);
				outputGenerator = (IOutputGenerator) outputGeneratorClass.newInstance();
			} catch (ClassNotFoundException e) {
				throw new SalesTaxError("Output generator class configured does not exist. ",e);
			} catch (SecurityException e) {
				throw new SalesTaxError("Output generator initialization failed ",e);
			}  catch (IllegalArgumentException e) {
				throw new SalesTaxError("Output generator initialization failed ",e);
			} catch (IllegalAccessException e) {
				throw new SalesTaxError("Output generator initialization failed ",e);
			} catch (ClassCastException e){
				throw new SalesTaxError("Output generator class configured does not implement IOutputGenerator interface",e);
			} catch (InstantiationException e) {
				throw new SalesTaxError("Output generator initialization failed ",e);
			} catch (Exception e) {
				throw new SalesTaxError("Output generator initialization failed ",e);
			}
		}catch(Throwable t){
			if(propertis.getProperty("outputGenerator") != null){
				propertis.remove("outputGenerator");
				outputGenerator = getOutputGenerator();
				ExceptionHandler.getInstance().showPopUp("Output generator initialization failed, Switching to default input",t);
			}else{
				ExceptionHandler.getInstance().handleThrowable(t);
			}
		}
		return outputGenerator;
	}

}
