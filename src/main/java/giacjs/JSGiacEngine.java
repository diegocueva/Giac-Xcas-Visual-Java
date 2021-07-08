package giacjs;

import com.diegocueva.giacvisualjava.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author dcueva
 */
public class JSGiacEngine {

    private static final String JS_LIB = "/giac.js";
    private ScriptEngine scriptEngine;
    
    public void load() throws ScriptException{
        Log.init("GJS");
        Long t1 = System.currentTimeMillis();
        Log.info("Loading... "+JS_LIB);
        InputStream in = JSGiacEngine.class.getResourceAsStream(JS_LIB);
        if(in==null){
            throw new IllegalStateException("Lib "+JS_LIB+" not found in classpath");
        }
        InputStreamReader inr = new InputStreamReader(in);
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        scriptEngine.eval(inr);
        Long t2 = System.currentTimeMillis();
        Log.info("Loaded "+(t2-t1));
        Log.info("Starting...");
        scriptEngine.eval("var docaseval = Module.cwrap('caseval', 'string', ['string'])");
        scriptEngine.eval("var aa = docaseval('1+1')");
        scriptEngine.eval("var bb = docaseval('solve(x^2-1=0,x)')");
        Long t3 = System.currentTimeMillis();
        Log.info("Started "+(t3-t2));
    }
    
    public static void main(String[] arg) throws ScriptException{
        JSGiacEngine jSGiacEngine = new JSGiacEngine();
        jSGiacEngine.load();
    }
    
}
