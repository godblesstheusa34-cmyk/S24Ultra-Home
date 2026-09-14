package dev.s24.fluidlauncher;

import android.content.*;
import java.util.*;

/** Single source of truth for every renderer and physics control. */
public final class TransitionConfig {
    public float waveAmplitude=1f, waveWidth=.18f, wavelength=.34f, propagationVelocity=1.35f, damping=.88f,
      verticalSpread=.38f, horizontalDisplacement=.17f, zElevation=42f, curvature=.62f, meshDeformation=.28f,
      perspective=.72f, tilt=18f, shadowOffset=18f, shadowSoftness=20f, shadowScale=.12f, shadowOpacity=.30f,
      incomingTiming=.46f, outgoingTiming=.54f, springStiffness=260f, settlingDamping=28f, flingSensitivity=900f,
      gestureThreshold=.28f, intensity=1f, drawerCurvature=.12f, drawerPerspective=.10f, drawerEdgeRotation=11f,
      drawerEdgeScale=.08f, drawerDepth=18f, drawerShadow=.16f, drawerInertia=.08f;
    public boolean fps=true, slowMotion=false;
    public static final String[] NAMES={"Wave amplitude","Wave width","Wavelength","Propagation velocity","Wave damping","Finger vertical spread","Horizontal displacement","Z elevation","Curvature","Mesh bow","Perspective","Tilt","Shadow offset","Shadow softness","Shadow scale","Shadow opacity","Incoming timing","Outgoing timing","Spring stiffness","Settling damping","Fling sensitivity","Gesture threshold","Animation intensity","Drawer curvature","Drawer perspective","Drawer edge rotation","Drawer edge scale","Drawer Z depth","Drawer shadow","Drawer inertia"};
    private static final float[] MAX={2, .5f,1,3,1,.8f,.4f,100,1,1,1,35,50,50,.3f,.8f,1,1,500,60,2500,.6f,2,.5f,.5f,30,.3f,60,.5f,.5f};
    public float[] values(){return new float[]{waveAmplitude,waveWidth,wavelength,propagationVelocity,damping,verticalSpread,horizontalDisplacement,zElevation,curvature,meshDeformation,perspective,tilt,shadowOffset,shadowSoftness,shadowScale,shadowOpacity,incomingTiming,outgoingTiming,springStiffness,settlingDamping,flingSensitivity,gestureThreshold,intensity,drawerCurvature,drawerPerspective,drawerEdgeRotation,drawerEdgeScale,drawerDepth,drawerShadow,drawerInertia};}
    public float max(int i){return MAX[i];}
    public void set(int i,float v){switch(i){case 0->waveAmplitude=v;case 1->waveWidth=v;case 2->wavelength=v;case 3->propagationVelocity=v;case 4->damping=v;case 5->verticalSpread=v;case 6->horizontalDisplacement=v;case 7->zElevation=v;case 8->curvature=v;case 9->meshDeformation=v;case 10->perspective=v;case 11->tilt=v;case 12->shadowOffset=v;case 13->shadowSoftness=v;case 14->shadowScale=v;case 15->shadowOpacity=v;case 16->incomingTiming=v;case 17->outgoingTiming=v;case 18->springStiffness=v;case 19->settlingDamping=v;case 20->flingSensitivity=v;case 21->gestureThreshold=v;case 22->intensity=v;case 23->drawerCurvature=v;case 24->drawerPerspective=v;case 25->drawerEdgeRotation=v;case 26->drawerEdgeScale=v;case 27->drawerDepth=v;case 28->drawerShadow=v;case 29->drawerInertia=v;}}
    public void save(Context c,String key){var e=c.getSharedPreferences("physics",0).edit();float[] v=values();for(int i=0;i<v.length;i++)e.putFloat(key+i,v[i]);e.putBoolean(key+"fps",fps).putBoolean(key+"slow",slowMotion).apply();}
    public static TransitionConfig load(Context c,String key){TransitionConfig x=new TransitionConfig();var p=c.getSharedPreferences("physics",0);float[] d=x.values();for(int i=0;i<d.length;i++)x.set(i,p.getFloat(key+i,d[i]));x.fps=p.getBoolean(key+"fps",true);x.slowMotion=p.getBoolean(key+"slow",false);return x;}
}
