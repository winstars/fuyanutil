@echo off
---set ANT_HOME=D:\devtools\ant-1.8.2
---set JAVA_HOME=D:\devtools\jrmc4v6
---set PATH=%PATH%;"%JAVA_HOME%"\bin;%ANT_HOME%\bin

cmd /c ant -f build.xml compile
pause