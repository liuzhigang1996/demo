@echo off
C:\Program Files\Java\jdk1.5.0_06\bin\native2ascii -encoding GBK %1.ascii >%1_zh_CN.properties
C:\Program Files\Java\jdk1.5.0_06\bin\native2ascii -encoding GBK %1.ascii >%1_zh_HK.properties
