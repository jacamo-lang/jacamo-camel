!start.

+!start
   <- .print("Started. Receiving entries (Tag, Value, Timestamp)");
      .wait(10000);
      .print("Sending a create request.");
      .send(sql, tell, foo);
   .

+value(tagQuatro, Value, TS)
   <- .print("Received created entry (", Tag, ", ", Value,", ", TS,").");
      .wait(5000);
      .print("Sending a delete request now.");
      .send(sql, achieve, foo);
      .

+value(Tag, Value, TS)
   <- .print("Received new entry (", Tag, ", ", Value,", ", TS,").");
      .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
