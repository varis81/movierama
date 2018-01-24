The MovieRama application is a Spring Boot application which uses Maven for managing its build lifecycle.
It has been built with Java 8 and can be run like this:       java -jar movierama-1.0.war      , the war file can be found under target. Alternatively, one can run it through the IDE of choice.
For simplicity, no actual Database has been used for storing the data needed but rather an EhCache has been used with a time to live big enough for the purposes of this demo application.

The application caches its queries to an EhCache implementation for somewhat less than a week (timeToLiveSeconds="600000").
You can check ehCache configuration under ehcache.xml.

After running the application, one can visit the homepage as follows:
http://localhost:8080/

This will lead you to the default homepage for an anonymous user. You are free to test the functionality implemented, by accessing this page.

A test user has been created (Registration works normally as well in case you want a different User):
username: Gandalf
password: 123

Some movies are imported during startup for exhibition purposes. A logged in user can add movies through the button provided.

The class MovieRamaController listens to all the endpoints configured and acts accordingly.
MovieRamaServiceImpl and UserServiceImpl are the services that implement most of the actual functionality. No db layer has been built since there is no need for such a small application.




IN ORDER TO RUN THE APPLICATION:
JAVA 8 required
Go to the root directory (movierama-project) and type:

java -jar target/movierama-1.0.war