README For Developers
=====================

This document is a "README" file for developers interested in working
on this application.


Compiling and Running
---------------------

The primary way to compile the application, package it as a Mac application,
and then run it is with this shell script:

    _assemble-run.sh

If you'll look at that script, you'll see that it runs these commands:

* `sbt assembly`
* `ant`
* A shell script to add a "hi-res" key to the app's _Info.plist_ file

That's a bit of a kludge, but at least I only need to run one script
to build the project as a Mac application. And since you have to run
the project as an app so you can give it permission to run (in the
Mac System Preferences), this works for me.


Just Running
------------

To just run an application that has already been compiled and
packaged as a Mac OS X app, use this command:

    _run-app.sh


More Information
----------------

The main URL for this project is: 

*http://alvinalexander.com/lightsaber

I'll add more information for developers here as I think of it. In the meantime
you can reach me here:

* http://alvinalexander.com





