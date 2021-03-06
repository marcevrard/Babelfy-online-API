Info related to this forked project
===================================

(README from the original project below)

Create the project structure
----------------------------

1. Move all `.jar` in `lib/`.
2. Copy the var.property file in config as shown below and add your key

    cp babelfy.var.properties my.babelfy.var.properties

3. Configure the project using `gradle` or `maven` (or `Eclipse` following the original README below).

### Using `gradle`

    gradle init --type java-library

And add:

    dependencies {
        // Babelfy related (Add External JARs)
        compile fileTree(dir: 'lib', include: ['*.jar'])
    }

    // Add application plugin to run from command line (https://stackoverflow.com/a/21358586)
    apply plugin:'application'
    mainClassName = "ExampleToken"

    // Allow for arg parsing to the class (https://stackoverflow.com/a/29382636)
    run {
        if (project.hasProperty("appArgs")) {
            args Eval.me(appArgs)
        }
    }

### Or using `maven` *(incomplete!)*


    mvn archetype:generate \
        -DgroupId=org.babelfy \
        -DartifactId=Babelfy-online-API \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DinteractiveMode=false
    ...

*************************************

README - Babelfy API 1.0 (April 2015)
=====================================

This package consists of a Java API to work with Babelfy, a unified multilingual, graph-based approach to Entity Linking and Word Sense Disambiguation. For more information please refer to the documentation below on how to install and run the software, as well as our website (http://babelfy.org) for news, updates and papers.

CONTENTS
--------

This package contains the following main components:

    babelfy-online-1.0.jar        # Jar of the Babelfy API
    CHANGELOG			# changelog for the Babelfy API
    config/			# configuration files
    docs/				# Javadocs
    lib/				# 3rd party libraries
    LICENSE			# Babelfy API's license
    README			# this file
    run-babelfydemo.sh		# shell script to test Babelfy in Linux
    run-babelfydemo.bat		# shell script to test Babelfy in Windows

REQUIREMENTS
------------

We assume that you have a standard installation of the Sun Java 1.7 JDK and all the associated programs (i.e., java, javac, etc.) in your path.

INSTALLATION
------------

### 1 Babelfy API

In order to access Babelfy RESTFul service, it is necessary to specify a valid key using the "babelfy.key" property in the config/babelfy.var.properties file. To obtain a key, please register on http://babelnet.org/register.

You are now ready to use the API. For testing purposes we provide a shell script:

		Linux:   run-babelnetdemo.sh, make sure that the file is
			executable by running: chmod +x run-babelnetdemo.sh.
		Windows: run-babelnetdemo.bat

#### 1.1 Configuring Babelfy API within an Eclipse project

1. Create your Eclipse project (File -> New -> Java (or Scala) project, give the project a name and press Finish). This creates a new folder with the project name under your Eclipse workspace folder
2. Copy the `config/` folder from the `Babelfy-online-API-1.0` folder into your `workspace/projectFolder/`
3. Now we need to include all the `lib/*.jar` and `babelfy-online-1.0.jar` files in the project build classpath:
   1. Select the project from Package Explorer tree view
   2. From the top bar click on Project and then Properties
   3. Once inside the Properties section click on Java build path and select the Libraries tab
   4. From the right menu click on the Add External JARs button
   5. Browse to the downloaded `Babelfy-online-API-1.0` folder, and select all the `lib/*.jar` and `babelfy-online-1.0.jar` files
4. Next we need to Include the config/ folder in the project build classpath:
   1. Select the project from Package Explorer tree view
   2. From the top bar click on File and then Refresh
   3. From the Java build path (see point 3 above) select the Source tab
   4. Once in the Source tab, click on Add Folder from the right sidebar and select the downloaded `config/` folder
5. Happy coding!! ;-)

For more information consult the guide online, http://babelfy.org/guide.

REFERENCES
----------

If you want to refer to Babelfy in your scientific work, please cite
this papers:

A. Moro, A. Raganato, R. Navigli. Entity Linking meets Word Sense Disambiguation: a Unified Approach. Transactions of the Association for Computational Linguistics (TACL), 2, pp. 231-244, 2014.

A. Moro, F. Cecconi, R. Navigli. Multilingual Word Sense Disambiguation and Entity Linking for Everybody. Proc. of the 13th International Semantic Web Conference, Posters and Demonstrations (ISWC 2014), pp. 25-28, Riva del Garda, Italy, 19-23 October 2014.

AUTHORS
-------

Roberto Navigli, Sapienza University of Rome
(navigli@di.uniroma1.it)

Andrea Moro, Sapienza University of Rome
(moro@di.uniroma1.it)

Federico Scozzafava
(federico.scozzafava@gmail.com)

Francesco Cecconi, Sapienza University of Rome
(cecconi@di.uniroma1.it)

COPYRIGHT
---------

Babelfy and the Babelfy API are licensed under a Creative Commons
Attribution-Noncommercial-Share Alike 3.0 License.
See the file LICENSE for details.

CONTACT
-------

Please feel free to get in touch with us for any question or problem you
may have using the following Google group:

  http://groups.google.com/group/babelnet-group

ACKNOWLEDGMENTS
---------------

Babelfy and the Babelfy API are an output of the ERC Starting Grant
MultiJEDI No. 259234.
