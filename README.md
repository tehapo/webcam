# Webcam Add-on for Vaadin 7

Webcam is an HTML5 webcam component for Vaadin 7.

## Online demo

Try the add-on demo at http://teemu.app.fi/gifbooth

The demo application uses and includes animated GIF processing Java classes made available by Kevin Weiner. See webcam-demo/src/main/java/com/madgag/gif/fmsware/README.markdown for more details.

## Download release

Official releases of this add-on will later be available at Vaadin Directory. For Maven instructions, download and reviews, go to https://vaadin.com/addon/webcam

## Building and running demo

- git clone https://github.com/tehapo/webcam.git
- mvn clean install
- cd demo
- mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine.

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for webcam-root project.

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your webcam-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the webcam-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/webcam-demo/ to see the application.

### Debugging client-side

The most common way of debugging and making changes to the client-side code is dev-mode. To create debug configuration for it, open webcam-demo project properties and click "Create Development Mode Launch" button on the Vaadin tab. Right-click newly added "GWT development mode for webcam-demo.launch" and choose Debug As > Debug Configurations... Open up Classpath tab for the development mode configuration and choose User Entries. Click Advanced... and select Add Folders. Choose Java and Resources under webcam/src/main and click ok. Now you are ready to start debugging the client-side code by clicking debug. Click Launch Default Browser button in the GWT Development Mode in the launched application. Now you can modify and breakpoints to client-side classes and see changes by reloading the web page.

Another way of debugging client-side is superdev mode. To enable it, uncomment devModeRedirectEnabled line from the end of DemoWidgetSet.gwt.xml located under webcam-demo resources folder and compile the widgetset once by running vaadin:compile Maven target for webcam-demo. Refresh webcam-demo project resources by right clicking the project and choosing Refresh. Click "Create SuperDevMode Launch" button on the Vaadin tab of the webcam-demo project properties panel to create superder mode code server launch configuration and modify the class path as instructed above. After starting the code server by running SuperDevMode launch as Java application, you can navigate to http://localhost:8080/webcam-demo/?superdevmode. Now all code changes you do to your client side will get compiled as soon as you reload the web page. You can also access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings.


## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

Webcam is written by Teemu Pöntelin
