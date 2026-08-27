# Srot (স্রোত) — Android

Material 3 **yt-dlp** studio for Android. Kotlin · Jetpack Compose · English + বাংলা.

## What it does

- Paste a video / playlist URL (or share into the app)
- Analyze public metadata (title, thumbnail via oEmbed)
- Quality presets: Best · 4K · 1080p · 720p · Audio
- Queue + foreground download service with notifications
- Structured **yt-dlp argv** (no shell)
- Settings: language, theme, Wi‑Fi only, binary paths

## Build APK (local)

1. Install [Android Studio](https://developer.android.com/studio) (Ladybug or newer) + JDK 17
2. Open this folder as a Gradle project
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**  
   or:

```bash
./gradlew assembleDebug
```

Debug APK path:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Build APK on GitHub Actions

Push to `main` runs `.github/workflows/build-apk.yml` and uploads the debug APK as a workflow artifact.

1. Open the repo **Actions** tab
2. Select **Build APK**
3. Download the artifact after the run succeeds

## First run on device

1. Install the APK (enable “Install unknown apps” if needed)
2. Open **Settings → Download yt-dlp binary** (or set a path to your own binary)
3. Optional: install FFmpeg for merging/audio extract and set path in Settings
4. Grant notification permission for progress

> On many devices you need a **native** yt-dlp / python build that runs on Android ABI (`arm64-v8a`).  
> The “fetch binary” button downloads the official yt-dlp release as a starting point; replace it with a device-compatible build if execution fails.

## Legal

Only download media you have the right to save. Do **not** bypass DRM, paywalls, CAPTCHAs, or access controls. Respect site terms and copyright.

- yt-dlp — Unlicense  
- FFmpeg — LGPL/GPL depending on build  

## Package

`com.srot.downloader` · minSdk 26 · targetSdk 35
