package dev.s24.fluidlauncher;

import android.appwidget.*;
import android.content.*;

/** Owns the platform widget host. Views remain live at rest; LauncherView snapshots them only while deforming. */
public final class WidgetController {
    public static final int HOST_ID=240124;
    private final AppWidgetHost host; private final AppWidgetManager manager;
    public WidgetController(Context c){host=new AppWidgetHost(c,HOST_ID);manager=AppWidgetManager.getInstance(c);}
    public void start(){host.startListening();} public void stop(){host.stopListening();}
    public int allocate(){return host.allocateAppWidgetId();}
    public void delete(int id){host.deleteAppWidgetId(id);}
    public AppWidgetHostView create(Context c,int id,AppWidgetProviderInfo info){return host.createView(c,id,info);}
    public AppWidgetManager manager(){return manager;}
}
