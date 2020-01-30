!start.

+!start
   <- .print("Started.");
      .send(filterAgent, achieve, iShallPass);
   .

+message(X)[source(S)]
  :   	true
  <-  	.print("Received message ", X, ", from ", S, ". Replying.");
  		  .send(S, tell, message(X));
  .

+!say(X)
  :     true
  <-    .print(X);
  .

+!create(X)
  :     true
  <-    .print("Creating a new context");
        jacamoComponent.createContext(X);
  .

+!dispose(X)
  :     true
  <-    .print("Disposing a new context");
        jacamoComponent.disposeContext(X);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
