package dev.s24.fluidlauncher;

import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import java.text.Collator;
import java.util.*;

public final class AppRepository {
    public record App(String label, ComponentName component, Drawable icon) {}
    public static List<App> load(Context context){
        PackageManager pm=context.getPackageManager(); Intent query=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<App> out=new ArrayList<>();
        for(ResolveInfo r:pm.queryIntentActivities(query,PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL))){
            if(r.activityInfo.packageName.equals(context.getPackageName())) continue;
            out.add(new App(r.loadLabel(pm).toString(),new ComponentName(r.activityInfo.packageName,r.activityInfo.name),r.loadIcon(pm)));
        }
        Collator c=Collator.getInstance();out.sort((a,b)->c.compare(a.label,b.label));return out;
    }
    public static void launch(Context c,App app){try{c.startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(app.component).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));}catch(ActivityNotFoundException ignored){}}
}
