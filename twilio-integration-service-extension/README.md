twilio-integration-service extension.

[Fill out this file with some information about your service extension.]

To use your service extension, you include the JAR build from this artifact to the CLASSPATH used when the service is started.

When you run a service as a bootable war, you use the loader.path command line argument to add JARs to the CLASSPATH. loader.path takes a comma-separated list of locations, which can reference JARs or directories containing one or more JARs. For example: 

java -Dloader.path=/lib,/path/to/my.jar -jar myservice-boot.war
If you are not running the Service as a bootable war, use the mechanism available in your application server.
