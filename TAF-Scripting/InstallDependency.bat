call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Web-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Web -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-WebServices-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-WebServices -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-GenericUtility-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-GenericUtility -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Database-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Database -Dversion=0.0.3 -Dpackaging=jar
