$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.7.6-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn "-Dhttps.protocols=TLSv1.2" spring-boot:run
