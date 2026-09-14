package dev.s24.fluidlauncher;

import android.content.*;
import org.json.*;
import java.util.*;

/** Compact JSON persistence; missing/uninstalled components are safely ignored by the view. */
public final class LayoutStore {
    public record Placement(String component,int page,int cell){}
    private final SharedPreferences prefs;
    public LayoutStore(Context c){prefs=c.getSharedPreferences("desktop",0);}
    public List<Placement> load(){List<Placement> out=new ArrayList<>();try{JSONArray a=new JSONArray(prefs.getString("placements","[]"));for(int i=0;i<a.length();i++){JSONObject o=a.getJSONObject(i);out.add(new Placement(o.getString("c"),o.getInt("p"),o.getInt("i")));}}catch(JSONException ignored){}return out;}
    public void save(List<Placement> list){JSONArray a=new JSONArray();for(Placement p:list){JSONObject o=new JSONObject();try{o.put("c",p.component).put("p",p.page).put("i",p.cell);a.put(o);}catch(JSONException ignored){}}prefs.edit().putString("placements",a.toString()).apply();}
    public int pages(){return Math.max(1,prefs.getInt("pages",2));} public void pages(int n){prefs.edit().putInt("pages",Math.max(1,n)).apply();}
}
