# CustomConfigParser
The following is the design of the custom config parser application:
Config class:  
	Config class represents the config file loaded while booting.
	This maintains states like NOT_STARTED, IN_PROGRESS, COMPLETE, FAILED.

ConfigBuilder:
	ConfigBuilder thread will be invoked by Config class whenever there is a 
	request to load a config file in the program. During its execution phase	
	ConfigBuilder thread will update the status of Config loading to the 
	following: FAILED: if the execution failed while processing the config file
	COMPLETE: if the execution succeeded without any errors. 
	IN_PROGRESS: when starting the processing of the config file.
	
ConfigStatus:
	Enum representing various states of config load process.
	
ConfigTest:
	Class containing various possible test cases for config boot.
	
	
To keep the get operation as quick, we are using LinkedHashMap.
The order of retrieval of key in this application of 0(1) in the best case(
when no collision occurs for keys in the hashmap).

The project have been built in Maven and the junit dependencies have been
fetched automatically by maven.