# Running The Application

To test the example application run the following commands.

* To package the example run.
        npm install <br/>
        mvn package -DskipTests

* To run the server run.<br/>
        java -jar target/dropwizard-chatroom-0.0.1-SNAPSHOT.jar server chatroom.yml

* To open client use below url in any browser.<br/>
	http://localhost:8080/app/angular/index.htm

* BasicAuth used. Not yet customised to work in background using form based Auth.
    Use username as anything you like >> will be used as chat-room username.<br/>
    Use password as "secret".<br/>

* Use different browser to test for multiple user.
* WebSocket/Server push not implemented. Chat will be refreshed on each text submit/ each refresh button hit.

* To add/update/delete chatRoom click Change first to see the options.

* TestCases(Java/JS) not yet implemented properly.