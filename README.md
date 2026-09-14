# Fluid Home for Galaxy S24 Ultra

Fluid Home is a genuine Android `HOME` application with a gesture-driven, procedural material desktop. It targets Android 14+ and the Galaxy S24 Ultra portrait display rather than diluting the renderer for older hardware. There are no animation bitmaps: icons, elevation, lighting, perspective, shadows, page replacement, and drawer curvature are calculated at runtime.

## Architecture and directory map

* `app/src/main/AndroidManifest.xml` registers `MainActivity` for `HOME`, `DEFAULT`, and `LAUNCHER` and requests package visibility so the app drawer can discover installed launchable activities.
* `LauncherView` is the hardware-accelerated scene, gesture controller, page compositor, app drawer, frame monitor, and idle-aware render scheduler.
* `TransitionConfig` centralizes all 30 home and drawer physical values and persists live/preset values.
* `AppRepository` queries `PackageManager`, retains the original drawable (including `AdaptiveIconDrawable`), sorts labels, and launches the selected component explicitly.
* `LayoutStore` stores page/cell/component placement as JSON in private `SharedPreferences`.
* `WidgetController` owns the supported `AppWidgetHost`/`AppWidgetManager` lifecycle and is the integration boundary for live host views.
* `SettingsActivity` is the live physics laboratory with sliders, defaults, named preset slot, FPS/frame-time overlay, and slow motion.

The project uses only Android platform APIs, keeping builds small and offline-friendly after Gradle/SDK dependencies are cached.

## Launcher operation

At first run, launchable activities populate two pages. Tap an icon to launch it, horizontally drag anywhere outside the dock to move pages, and tap **Apps** to open the vertically scrolling drawer. Long-press the desktop for page management and settings. Positions and page count survive process death. Package discovery occurs off the UI thread; source icons are never modified or written to disk.

Android recognizes the activity as a Home candidate through the manifest intent filter. On Samsung, install the APK, press Home, and select **Fluid Home**, or use **Settings → Apps → Choose default apps → Home app → Fluid Home**. Gesture navigation remains owned by One UI; transparent system bars and edge-to-edge layout preserve the S24 Ultra display area.

## Fluid field and rendering

Finger displacement directly defines normalized progress `p`; it is never converted to a canned animation while held. The front is `x_front = width × (1 − |p|)` (reversed for the opposite direction). For every icon the local disturbance is:

```
dx = (iconX - frontX) / (screenWidth * waveWidth)
dy = (iconY - fingerY) / (screenHeight * verticalSpread)
field = exp(-3.2 * (dx² + 0.36dy²))
        * (0.72 + 0.28cos(2πdx / wavelength))
```

The field drives horizontal travel, Z lift, local tilt, a restrained vertical shear, perspective scale, mesh-like bow scaling, and runtime blurred shadow separation. Outgoing and incoming pages have independent smooth-step timing gates, so the advancing pressure front physically clears one arrangement and reveals the other rather than crossfading whole pages. The wallpaper belongs to Android/system composition and is never drawn into, scrolled, refracted, or transformed.

On release, a semi-implicit, elapsed-time spring integrates `a = stiffness(target − p) − damping × velocity`. Gesture threshold and measured fling velocity choose the target; reversal simply changes the field sign. Delta time is clamped after stalls and never assumes 60 Hz. Rendering uses `postInvalidateOnAnimation()` only during a gesture, spring, or drawer inertia, so the stationary launcher does not continuously consume GPU power. Canvas is hardware accelerated, draw paints/rectangles are reused, icons are cached as package drawables, and discovery never blocks rendering.

### Tuning reference

Wave amplitude/intensity set overall response; width and wavelength set crest envelope/ripples; propagation velocity is available for autonomous refinements; damping restrains the wave; vertical spread ties geometry to finger height. Horizontal displacement and Z elevation control travel/lift. Curvature, mesh bow, perspective, and tilt shape attached icons. Shadow offset, softness, scale, and opacity define procedural depth. Incoming/outgoing timing control the replacement boundary. Spring stiffness, settling damping, fling sensitivity, and gesture threshold control release. Drawer curvature/perspective/edge rotation/edge scale/Z depth/shadow/inertia define its deliberately subtle large-radius surface.

The current high-quality Canvas renderer approximates icon mesh curvature by anisotropic deformation around the local crest. Android Canvas does not expose a stable textured vertex mesh for arbitrary adaptive drawables; a future GLES renderer can implement a 9×9 texture mesh behind the same config and interaction interfaces without changing icon sources.

## App drawer

Rows remain centered and readable, while normalized distance from the viewport center quadratically changes scale, Z position, tilt, and shadow. Scroll is direct while touched and becomes elapsed-time exponential inertia on release. Tap coordinates are resolved against the untransformed logical grid, maintaining reliable targets.

## Widgets and platform limitations

`WidgetController` uses the official `AppWidgetHost` architecture and starts/stops listening with the Home lifecycle. The host allocation/view API is ready for provider binding. The first release deliberately does not expose the privileged widget picker/binding UI or place host views on the grid: non-system launchers require a user-confirmed `ACTION_APPWIDGET_BIND` flow, and arbitrary live view trees cannot be GPU-mesh warped. The safe next implementation is a live `AppWidgetHostView` at rest, one transition-only bitmap snapshot, then immediate restoration of the interactive view after settling. Shortcuts similarly require `LauncherApps` pin-request handling. Folder data and cross-page drag UI are not yet exposed; desktop page management, persistence, discovery, launching, and drawer are operational. These are documented limitations, not broken placeholder paths.

Samsung may restrict package visibility or background activity starts under enterprise policy. One UI owns Recents, task animations, system gesture exclusion, wallpaper selection, notification dots, and proprietary edge panels. Fluid Home uses no undocumented Samsung interfaces.

## Build, APK, and installation

1. Install Android Studio Ladybug or newer, JDK 17, Android SDK Platform 35, and Build Tools 35.
2. Open this repository (the directory containing `settings.gradle.kts`) and allow Gradle sync.
3. This source-only repository intentionally does not commit the binary Gradle wrapper JAR. Use Android Studio's bundled Gradle integration, or install Gradle 8.10.2 and run `gradle clean lint testDebugUnitTest assembleDebug`.
4. Find the APK at `app/build/outputs/apk/debug/app-debug.apk`.
5. Transfer it to the phone and permit **Install unknown apps**, or enable USB debugging and run `adb install -r app/build/outputs/apk/debug/app-debug.apk`.
6. Select Fluid Home using the Samsung path above. Keep One UI Home installed so it remains an easy fallback.

For a release APK, configure a private signing key in a local (uncommitted) Gradle signing configuration and run `gradle assembleRelease`. Never publish a debug-signed launcher as a trusted release.

## Troubleshooting

* **No apps:** ensure package visibility was not removed and work-profile policy permits queries; relaunch after package installation.
* **Home chooser does not appear:** clear the current Home app default in Samsung Settings, then press Home.
* **Gradle cannot find SDK:** create `local.properties` containing `sdk.dir=/absolute/path/to/Android/Sdk`.
* **JDK/AGP error:** force Gradle JDK 17. AGP 8.7.3 is intentionally pinned.
* **Icons look stale after app updates:** force-stop/reopen Fluid Home; its in-memory repository reloads with the process.
* **Too dramatic:** restore defaults in Fluid settings; presets are local to this install.

## Recommended improvements

Implement user-confirmed widget binding and transition snapshots first, followed by drag/drop editing, folder composition, `LauncherApps` pinned shortcuts, package-change callbacks, notification badges, accessibility virtual nodes, and an OpenGL ES 3.2 instanced 9×9 icon mesh. Add Macrobenchmark/FrameMetrics instrumentation on physical S24 Ultra hardware and tune against Samsung's adaptive 120 Hz scheduling; emulator timing is not representative.
