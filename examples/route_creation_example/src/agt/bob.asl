!start.

+!start
   <- .print("Started.");
   .

+!say(X)
  :     true
  <-    .print(X);
  .

+!create(X)
  :     true
  <-    .print("Creating a new context");
        jasonComponent.createContext(X);
  .

+!dispose(X)
  :     true
  <-    .print("Disposing a new context");
        jasonComponent.disposeContext(X);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
