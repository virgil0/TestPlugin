package org.elasticsearch.plugin.test;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugin.test.CustomScoreScript;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.rest.RestModule;
import org.elasticsearch.script.ScriptModule;

public class TestPlugin extends AbstractPlugin {
    
//	public Map<String, Float> storeMap;
	
	@Override public void processModule(Module module) {
        if (module instanceof RestModule) {
            ((RestModule) module).addRestAction(HelloWorldAction.class);
        }
    }
	
    public void onModule(ScriptModule module) {
    	
        module.registerScript(CustomScoreScript.SCRIPT_NAME, CustomScoreScript.Factory.class);
        
    }

	public String name() {		
		return "tCustomScorePlugin";
	}

	public String description() {
		return "Test Custom Score Plugin";
	}
}
