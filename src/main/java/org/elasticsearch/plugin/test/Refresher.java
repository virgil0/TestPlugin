package org.elasticsearch.plugin.test;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Refresher {
	Timer timer;
	RefresherTask rt;
	//Map<String, Float> storeMap_real;
    public Refresher(int seconds) {  
        timer = new Timer();
        rt = new RefresherTask();
        timer.schedule(rt, 0, seconds*1000);
    }
    
    public Refresher(){
    	timer = new Timer();
    	rt = new RefresherTask();
    	timer.schedule(rt, 0);
    }
    
    class RefresherTask extends TimerTask{ 
    	
    	private final String binFilePath = "/home/elasticsearch/plugin/data";
    	
        public RefresherTask() {
			// TODO Auto-generated constructor stub
        
		}


		public void run(){  
            System.out.println("Begin Loading HashMap");  
            init_producttype();
            init();
            init_demotion();
            System.out.println("Finished Loading HashMap");
        }
        
	    public void init_producttype(){
	    		DataInputStream inputStream = null;
	    		HashMap<String, Float> swapPTypeMap = new HashMap<String, Float>();
	        	try {
	        		File file = new File(binFilePath + "/ProductTypeBoost.bin");
	    			inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	    			long memberid;
	    			int stringSize;
	    			String producttype;
	    			float boost;
	    			
	    			while(inputStream.available() > 0){
	    				memberid = inputStream.readLong();
	    				stringSize = inputStream.readInt();
	    				byte[] t = new byte[stringSize + 1];
	    				inputStream.read(t, 0, stringSize + 1);
	    				producttype = new String(t);
	    				producttype = producttype.replace("\n", "").trim();
	    				t = null;
	    				boost = inputStream.readFloat();
	    				swapPTypeMap.put(String.valueOf(memberid) + "|"+ producttype, boost);	
	    			}
	    			inputStream.close();
	    			CustomScoreScript.Factory.pTypeMap = swapPTypeMap;
	    			System.out.println("Product type loaded");
	    			
	    		} catch (FileNotFoundException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (EOFException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	}
		
		
        public void init_demotion(){
    		DataInputStream inputStream = null;
    		Set<String> swapDemotionSet= new HashSet<String>();
        	try {
        		File file = new File(binFilePath + "/ProductDemotion.bin");
        		long size = file.length();
        		long pairs = size / 16;  // memberid(long) productid(long)
        		System.out.println(pairs);
    			inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
    			long memberid;
    			long productid;
    			
    			while(pairs != 0){
    				pairs--;
    				memberid = inputStream.readLong();
    				productid = inputStream.readLong();
    				swapDemotionSet.add(String.valueOf(memberid) + "|"+ String.valueOf(productid));	
    			}
    			inputStream.close();
    			CustomScoreScript.Factory.demotionSet = swapDemotionSet;
    			System.out.println("Demotion loaded");
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (EOFException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
    	
    	public void init(){
    		DataInputStream inputStream = null;
    		HashMap<String, Float> swapStoreMap = new HashMap<String, Float>();
        	try {
        		
        		File file = new File(binFilePath + "/StoreBoost.bin");
        		long size = file.length();
        		long pairs = size / (16+4);   // memberid(long) storeid(long) boost(float)
        		pairs = pairs / 3;              // 1/3
        		System.out.println(pairs);
    			inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
    			long memberid;
    			long storeid;
    			float boost;
    			
    			while(pairs != 0){
    				pairs--;
    				memberid = inputStream.readLong();
    				storeid = inputStream.readLong();
    				boost = inputStream.readFloat();
    				swapStoreMap.put(String.valueOf(memberid) + "|"+ String.valueOf(storeid), Float.valueOf(boost));
    			}
    			inputStream.close();
    			CustomScoreScript.Factory.storeMap = swapStoreMap;
    			System.out.println("Store Map Loaded");
    			
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (EOFException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }  
}
