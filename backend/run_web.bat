@echo off
setlocal
cd /d "%~dp0"

set "MAVEN_OPTS=--enable-native-access=ALL-UNNAMED -Djansi.mode=off"

call mvn -q -DskipTests compile
if errorlevel 1 exit /b %errorlevel%

echo Starting Wayfinder web UI at http://localhost:8080
java -cp "target\classes;.m2\repository\org\mongodb\mongodb-driver-sync\5.1.0\mongodb-driver-sync-5.1.0.jar;.m2\repository\org\mongodb\mongodb-driver-core\5.1.0\mongodb-driver-core-5.1.0.jar;.m2\repository\org\mongodb\bson\5.1.0\bson-5.1.0.jar;.m2\repository\org\mongodb\bson-record-codec\5.1.0\bson-record-codec-5.1.0.jar;.m2\repository\com\google\code\gson\gson\2.11.0\gson-2.11.0.jar;.m2\repository\org\slf4j\slf4j-api\2.0.13\slf4j-api-2.0.13.jar;.m2\repository\org\slf4j\slf4j-simple\2.0.13\slf4j-simple-2.0.13.jar" wayfinder.WayfinderApiServer
