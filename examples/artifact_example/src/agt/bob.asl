!start.

+!start
  <-  .print("-----Agent started");
      focus(dummyArtifact);
  .

+val(X)
  :   X == 10
  <-  .print("I, Bob, perceived 'val' reached 10.").

+tick
  <-  .print("I, Bob, perceived a tick").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
