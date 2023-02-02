@echo off
@set SITE=http://localhost:8080
@set LYNX_HOME=c:\tools\lynx
cd %LYNX_HOME%
%LYNX_HOME%\lynx.exe "%SITE%"