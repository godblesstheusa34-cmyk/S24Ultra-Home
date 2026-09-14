package dev.s24.fluidlauncher;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.view.*;import android.widget.*;

public final class MainActivity extends Activity {
    private LauncherView launcher; private WidgetController widgets;
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.TRANSPARENT);getWindow().setNavigationBarColor(Color.TRANSPARENT);getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);widgets=new WidgetController(this);launcher=new LauncherView(this);setContentView(launcher);}
    @Override protected void onStart(){super.onStart();widgets.start();} @Override protected void onStop(){widgets.stop();super.onStop();}
    public void openSettings(){startActivity(new Intent(this,SettingsActivity.class));}
    @Override protected void onResume(){super.onResume();if(launcher!=null)launcher.reloadConfig();}
}
