!start.

+!start
   <- .print("Started.")
   .

+reading(X)[source(S)]
  :   true
  <-  .print("I'm client 2, and just received: ", X, " from ", S);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
