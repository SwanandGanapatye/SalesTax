To start the application
----------------------
Do required configuration in 'configuration.properties' file (defaults are already provided)
Keep the 'configuration.properties' in the classpath (default available at 'SalesTaxSoln\target\classes')
Make sure input, archive and output path mentioned in 'configuration.properties' are accesible.
Run the maven project (through command prompt go to directory with pom.xml and enter 'mvn exec:java')


To use the application
----------------------
Put input file in the input directory mantioned in 'configuration.properties' (Extention of the file should be as mentioned in 'configuration.properties' )
Expect the output in output directory mentioned in 'configuration.properties'.
Pop up will be displayed for any error. The corresponding details will be available on console.

To shut down the application
----------------------------
Type 'Exit' (without quote) in the console and hit enter key
Wait for application to shut down


Features of the application
---------------------------
Multi threaded 			- To improve performance of the application.
Configurable modules 		- To enhance as per need in future
Configurable tuning parameters	- To tune performance as per environment.
Lightweight 			- To keep it simple
Robust				- To handle invalid inputs and errors with user friendly pop ups


Can be improved with
--------------------
Graceful shutdown		- To avoid data loss if aplication closed while processing
Logging mechanism 		- For monitoring the application (currently console is used for the same)
Documentation 			- For support and maintenance assistance
Constant file			- To get all messages through constants
Multilingual support		- To make application more user friendly
Interactive UI			- To make application more user friendly


Note:
For any clarification or support please call Swanand on mobile (+91 9833769763)