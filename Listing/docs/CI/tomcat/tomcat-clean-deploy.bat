cd D:/servers/apache-tomcat-8.0.20/bin 

netstat -na | find "LISTENING" | find /C /I ":8080" > NUL
if %errorlevel%==0 goto :running
goto :continue

:running
shutdown.sh
echo shutdown tomcat

:continue
echo removing files
ping 127.0.0.1 -n 10 -w 9999999 >NUL
rd /s /q D:\servers\apache-tomcat-8.0.20\webapps\Listing
del D:\servers\apache-tomcat-8.0.20\webapps\Listing.war
echo starting tomcat
startup.bat


