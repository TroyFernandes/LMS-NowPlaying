# Logitech Media Server Now Playing
Android TV app to show the current playing song from your Logitech Media Server instance.

If you have a Jellyfin instance, you can provide the credentials to grab the backdrop image. Otherwise it defaults to showing the album art

| ![space-1.jpg](https://github.com/TroyFernandes/LMS-NowPlaying/blob/main/sample-images/nowplaying-wjellyfin.png?raw=true) | 
|:--:| 
| *w/ Jellyfin* |

| ![space-1.jpg](https://github.com/TroyFernandes/LMS-NowPlaying/blob/main/sample-images/nowplaying-nojellyfin.png?raw=true) | 
|:--:| 
| *w/o Jellyfin* |

# Building the Source
*Version used to develop app: Android Studio Giraffe | 2022.3.1 Patch 2*
1. Clone the repo and open it in Android Studio
2. Take a look at the ``sample.local.properties`` file in the root directory and fill out the ``local.properties`` file accordingly
3. Restart Android Studio
4. Change the build variant to release
5. In Android Studio: -> Build -> Build Bundle(s) / APK - > Build APK
6. Install the APK