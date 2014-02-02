package opticnav.web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class ResponseObject {
    private Map<String, Object> map;
    
    public ResponseObject() {
        this.map = new HashMap<>();
    }
    
    public void addInteger(String key, Integer value) {
        this.map.put(key, value);
    }

    public void addBoolean(String key, Boolean value) {
        this.map.put(key, value);
    }
    
    public void addDouble(String key, Double value) {
        this.map.put(key, value);
    }
    
    public void addString(String key, String value) {
        this.map.put(key, value);
    }
    
    public ResponseObject addObject(String key) {
        ResponseObject value = new ResponseObject();
        this.map.put(key, value);
        return value;
    }
    
    public Map<String, Object> getMap() {
        return this.map;
    }
    
    private void stringify(String s, OutputStreamWriter out) throws IOException {
        // TODO - yuck. escaping is probably more involved than this
        out.write('"');
        
        out.write(s.replace("\"", "\\\"").replace("\n", "\\\n"));
        
        out.write('"');
    }
    
    public void toJSON(OutputStreamWriter out) throws IOException {
        out.write("{\n");
        
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            
            stringify(entry.getKey(), out);
            out.write(":");

            /* instanceof is evil, but we're using it anyway */
            Object val = entry.getValue();
            if (val == null) {
                out.write("null");
            } else {
                if (val instanceof Integer) {
                    out.write(Integer.toString((Integer)val));
                } else if (val instanceof Boolean) {
                    out.write((Boolean)val ? "true" : "false");
                } else if (val instanceof Double) {
                    out.write(Double.toString((Double)val));
                } else if (val instanceof String) {
                    stringify((String)val, out);
                } else if (val instanceof ResponseObject) {
                    ((ResponseObject)val).toJSON(out);
                } else {
                    throw new IllegalStateException("Unexpected JSON object");
                }
            }
            
            if (it.hasNext()) {
                out.write(",");
            }
            out.write("\n");
        }

        out.write("}");
    }
    
    public void setRequestAttributes(HttpServletRequest req) {
        // TODO
        req.setAttribute("ro", this);
    }
}