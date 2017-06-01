# xpro_min
An (almost) minimal project to test OpenEJB with Jetty. Ignore its Hibernate and JAX-RS stuff - they're irrelevant.

Get OpenEJB:
- Download OpenEJB Standalone 7.0.3 from TomEE downloads, expand it, then put it somewhere suitable (eg. _/devel/apache-openejb-7.0.3)_.

Get dependent files:
- Run Ant file _build.xml_ target _get-dependent-files_.  
- As a sanity check, check these directories now contain JAR files:  

	business/src/main/lib-*  
	business/src/test/lib-*  
	web/src/main/lib-*  
	web/src/test/lib-*  

Clean:
- Run Ant file _build2.xml_ (NOTE THE "2" IN THE FILENAME) target _clean_.  
- As a sanity check, confirm that _collapsed/_ is now empty.  

Compile:
- Run Ant file _build2.xml_ target _compile_.  
- As a sanity check, confirm that _collapsed/_ now contains a WAR file in collapsed EAR format.  

Run:
- Open _runjetty.sh_ for edit. Examine _PROJECT_ and _OPENEJB_: they are directory paths, without trailing slashes. Modify them as necessary.
- On a command line, go to the project root. Check that _runjetty.sh_ is executable. Then run it:

	./runjetty.sh

- Wait until a message like this appears in the console log:  

	INFO [main] (Server.java:379) - Started @7249ms

- In a web browser, go to <a href="http://localhost:8080/xpro">_http://localhost:8080/xpro_</a>.
- Click on the first link. Expect it to return the same page, and to have logged some entries starting with _>>>>_, eg.  

	>>>> In PretendFinderService.findSomething()

- Click on the second link. Expect it to fail, with NPE:  

	Caused by: java.lang.NullPointerException
		at com.goxpro.xpro.business.domain.base.UsernameCacheRefresher.refreshUsernameCache(UsernameCacheRefresher.java:40)

 
