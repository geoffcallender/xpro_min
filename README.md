# xpro_min
Minimal (almost) project to test OpenEJB with Jetty.

Prepare:
- Download OpenEJB Standalone 7.0.3 from TomEE downloads, expand it, then put it somewhere suitable (eg. _/devel/apache-openejb-7.0.3)_.
- Download Jetty ... TODO.  

Get dependent files:
- Run Ant file _build.xml_ target _get-dependent-files_.  

Clean:
- Run Ant file _build2.xml_ (NOTE THE "2" IN THE FILENAME) target _clean_.   
- As a sanity check, confirm that _collapsed/_ is now empty.  

Compile:
- Run Ant file _build2.xml_ target _compile_.  
- As a sanity check, confirm that _collapsed/_ now contains a WAR file in collapsed EAR format.  

Run:
- Open _runjetty.sh_ for edit. Modify the values of _PROJECT_, _OPENEJB_, and _JETTY_ as necessary. They are directory paths, without trailing slashes.  
