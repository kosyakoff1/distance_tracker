## Distance tracker app

API key should be placed in release.properties and debug.properties (secrets-gradle-plugin
 searching files) and linked in manifest com.google.android.geo.API_KEY metadata property

TODO:

* refactor code to use viewModels
* remove databinding in permission fragment if needed
* add support for dark theme map style
* check to use android standard permission instead of EasyPermissions library
* refactor project structure