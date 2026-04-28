@echo off
setlocal
cd /d "%~dp0"

set "MAVEN_OPTS=--enable-native-access=ALL-UNNAMED -Djansi.mode=off"

call mvn -q -DskipTests compile
if errorlevel 1 exit /b %errorlevel%

echo Starting Wayfinder backend...
java -cp "target\classes;.m2\repository\org\mongodb\mongodb-driver-sync\5.1.0\mongodb-driver-sync-5.1.0.jar;.m2\repository\org\mongodb\mongodb-driver-core\5.1.0\mongodb-driver-core-5.1.0.jar;.m2\repository\org\mongodb\bson\5.1.0\bson-5.1.0.jar;.m2\repository\org\mongodb\bson-record-codec\5.1.0\bson-record-codec-5.1.0.jar" wayfinder.WayfinderApplication
