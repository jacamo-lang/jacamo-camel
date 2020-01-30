counter(0).

!start.

+!start
  <-  .print("-----Agent started");
      focus(dummyArtifact);
      callMe(X);
      .print("Unifyed X with", X)
      callMe(myReturnValue);
      callMe(myOtherValue);
  .

+!updateCycle
  :   counter(X)
  <-  .wait(500)
      updateProp(X);
      NewX = X + 1;
      -+counter(NewX);
      !updateCycle;
  .

+tick
  <-  .print("I, Alice, perceived a tick").

+say(X)
  <-  .print("--------------------------", X);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
