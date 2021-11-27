@echo off

set PROJECT=C:\prg\myapps\Pacman
set CLASSES=%PROJECT%\build\classes
set SRC=%PROJECT%\src
set JDKBIN=C:\prg\jdk8\bin
set LIBS=C:\prg\libs\xstream-1.4.7.jar;C:\prg\libs\xmlpull-1.1.3.1.jar;C:\prg\libs\xpp3_min-1.1.4c.jar;C:\prg\libs\mysql-connector-java-5.1.34-bin.jar;


rem ----------------------- avvia il server mysql se non è già in esecuzione -----------------------
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL 
if not "%ERRORLEVEL%"=="0" (start /min C:\prg\mysqlStart.lnk) 

rem ----------------------- ERRORLEVEL == "0" indica che il programma è in esecuzione senza errori -

rem ----------------------- crea cartella classes se non esiste ------------------------------------
if not exist %CLASSES% mkdir %CLASSES%

rem ----------------------- genera ServerActivityLog.class e lo esegue -----------------------------
tasklist /FI "WINDOWTITLE eq ServerActivityLog" 2>NUL | find /I /N "ServerActivityLog">NUL
if not "%ERRORLEVEL%"=="0" (start "ServerActivityLog" "cmd /c color 2F && cd %SRC% & %JDKBIN%\javac -cp .;%LIBS% ServerActivityLog.java -d %CLASSES% && cd %PROJECT% & echo Log Attivita': & %JDKBIN%\java -cp %CLASSES% ServerActivityLog & pause")

rem ----------------------- genera i file .class ed esegue Pacman se non è già in esecuzione -------
tasklist /FI "WINDOWTITLE eq Pacman" 2>NUL | find /I /N "java.exe">NUL
if not "%ERRORLEVEL%"=="0" (start /min cmd /c "cd %SRC% & %JDKBIN%\javac -cp .;%LIBS% *.java -d %CLASSES% && cd %PROJECT% & %JDKBIN%\java -classpath %LIBS%;%CLASSES% Game")

