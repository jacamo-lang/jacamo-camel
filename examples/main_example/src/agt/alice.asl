!start.

+!start
   <- .print("Started.");
      .send(filterAgent, tell, iShallPass);
      .send(filterAgent, achieve, iShallPass);
      .send(filterAgent, achieve, letMePassToo);
   .

+message(X)[source(S)]
  :   	true
  <-  	.print("Received message ", X, ", from ", S);
  		.send(S, tell, message(X));
  .

+value(X)
  :     true
  <-    .print("Value received ", X);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
