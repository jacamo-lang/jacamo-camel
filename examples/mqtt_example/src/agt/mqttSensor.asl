!start.

+!start
   <- .print("Started.");
      !!publishRandom1;
      !!publishRandom2;
   .

+!publishRandom1
  :   true
  <-  .random(Reading);
      .send(mqttAgent, tell, one(Reading));
      .wait(Reading*10000);
      !!publishRandom1;
  .

+!publishRandom2
  :   true
  <-  .random(Reading);
      .send(mqttAgent, tell, two(Reading));
      .wait(Reading*10000);
      !!publishRandom2;
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
