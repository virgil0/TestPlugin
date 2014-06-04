package org.elasticsearch.plugin.test;


import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.search.lookup.IndexField;
import org.elasticsearch.search.lookup.IndexFieldTerm;
import org.elasticsearch.index.fielddata.ScriptDocValues;


public class CustomScoreScript extends AbstractSearchScript {

    int fieldValue;
    // terms that are used for scoring, must be unique
    ArrayList<String> terms = null;
    // weights, in case we want to put emphasis on a specific term. In the most
    // simple case, 1.0 for every term.
    ArrayList<Double> weights = null;

    final static public String SCRIPT_NAME = "custom_script_score";

    public Set<String> demotionSet;
    public Map<String, Float> storeMap;
    public Map<String, Float> pTypeMap;
    /**
     * Factory that is registered in
     * {@link org.elasticsearch.examples.nativescript.plugin.NativeScriptExamplesPlugin#onModule(org.elasticsearch.script.ScriptModule)}
     * method when the plugin is loaded.
     */
    public static class Factory implements NativeScriptFactory {

   
        /**
         * This method is called for every search on every shard.
         * 
         * @param params
         *            list of script parameters passed with the query
         * @return new native script
         */
    	public static Map<String, Float> storeMap;
    	public static Set<String> demotionSet;
    	public static Map<String, Float> pTypeMap;
    	
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new CustomScoreScript(storeMap, demotionSet, pTypeMap, params);
        }
    }

    public CustomScoreScript(Map<String, Float> storeMap2,  Set<String> demotionSet2, Map<String, Float> pTypeMap2, Map<String, Object> params) {
		// TODO Auto-generated constructor stub
    	storeMap = storeMap2;
    	demotionSet = demotionSet2;
    	pTypeMap = pTypeMap2;
	}

	/**
     * @param params
     *            terms that a scored are placed in this parameter. Initialize
     *            them here.
     */
 
    public Object run() {
    		float score = 1;
    		long memberId = ((ScriptDocValues.Longs)doc().get("ownerId")).getValue();
    		long storeId = ((ScriptDocValues.Longs)doc().get("storeId")).getValue();
    		String storeKey = memberId + "|" + storeId;
            if(storeMap.containsKey(storeKey)){
            	score = 1000;//100 * storeMap.get(storeKey);
            }
            
            String productTypeKey = memberId + "|" + ((ScriptDocValues.Strings)doc().get("chvProductType")).getValue();
            if(pTypeMap.containsKey(productTypeKey)){
            	score = 10;//pTypeMap.get(productTypeKey) * 100;
            }
            
            String demotionKey = memberId + "|" + ((ScriptDocValues.Longs)doc().get("id")).getValue();
            if(demotionSet.contains(demotionKey)){
            	score = 100; //-1;
            }
            return ((ScriptDocValues.Longs)doc().get("sc")).getValue();
           }

}