@echo off

:loop
tasklist|find /i "soffice.exe"
if %errorlevel%==1 (
goto start
)
echo "service was started"
goto loop

:start
echo "start service"
set pwd = %cd%
C:
cd "C:\\Program Files (x86)\\OpenOffice 4\\program\\"
soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
cd %pwd%
goto loop