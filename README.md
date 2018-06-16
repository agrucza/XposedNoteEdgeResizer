# XposedNoteEdgeResizer

This Project is a Xposed Module aiming at the Samsung Galaxy Note Edge running non stock Roms. (Android Oreo 8.1)
This module will resize the System UI / Android Apps to only occupy the display space excluding the Edge Screen.

This module is the base for my other project https://github.com/agrucza/NoteEdgeSidebarService which aims to provide a Sidebar Service as found in the stock Samsung roms.

This project is set up on AIDE (Android IDE) this should be ok for Android Studio but i can't guarantee as i've never tried it in there.

Progress so far:
-
The module seems to work reasonably well and the space for the Edge Display is left unoccupied by the systen and the apps.

Issues that needs to be fixed:
-
1. In Landscape mode the keyboard is not rearranged and is in its standard position at the bottom of the screen.
2. Also in Landscape mode but at an rotation of 270 degrees (Edge Screen on top) the UI itself and the Apps are starting at 160px vertical but the status bar content is not displayed even if the status bar is also positioned at 160px from the top.
3. When in landscape mode (rotation 270 degree, with the Edge Screen on top) the touch is 160px off as the views are shifted downwards by 160px. (e.g. if you touch an element the element 160px below it will receive the event)
4. Not all apps realign well on the changes of the views width which could result in dialogs reaching into the Edge Screen or cut off content. I'm happy for feedback which apps showing this behaviour.
