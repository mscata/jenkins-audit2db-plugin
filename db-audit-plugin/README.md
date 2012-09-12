Jenkins Audit to Database Plugin
================================

This Jenkins plugin allows the recording of build information to a database.

Installation
------------
Database connections are established via JDBC, so you have to ensure a valid
JDBC driver for your database can be found by this plugin. This can be
accomplished in two ways:

### Use the Jenkins classpath
If Jenkins is running as a standalone application, you can put the JDBC 
driver package in the `war/WEB-INF/lib` directory. If Jenkins is running
inside a J2EE container (e.g. Tomcat) you can use the container's classpath 
instead (consult the container's documentation for details).

### Use the plugin's classpath
Regardless of whether jenkins is running as a standalone application or
as a web application inside a J2EE container, you can put the JDBC driver
package in `$JENKINS_HOME/plugins/audit2db/WEB-INF/lib`. This directory
will be created the first time you run the plugin inside Jenkins, so if
you can't see it (and assuming you have actually already installed the
audit to databasep plugin), then try restarting Jenkins.

Contributing
------------
Contributions are always welcome. Here's how:

1. Fork the plugin repo.
2. Create a branch (`git checkout -b my_audit2db`)
3. Commit your changes (`git commit -am "Added cool stuff."`)
4. Push to the branch (`git push origin my_audit2db`)
5. Open a [Pull Request][1]
6. Wait. Good things will come.

[1]: http://github.com/github/markup/pulls