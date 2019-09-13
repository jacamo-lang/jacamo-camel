!start.

+!start
   <- .print("Started.");
      .wait(5000);
      .print("Sending arity zero");
      !!start0;
      .wait(10000);
      .print("Sending arity one");
      !!start1;
      .wait(10000);
      .print("Sending arity two");
      !!start2;
      .wait(10000);
      .print("Sending arity three");
      !!start3;
      .wait(10000);
      .print("Sending arity X");
      !!startX;
      .wait(10000);
      .print("Sending list1");
      !!startList1;
      .wait(10000);
      .print("Sending list2");
      !!startList2;
   .

+!start0
   <-
      .send(camel0, tell, fixed);
      .send(camel0, tell, wrong);
   .
+!start1
   <-
      .send(camel1, tell, perf(10));
      .send(camel1, tell, perf(oi));
      .send(camel1, tell, perf("ola"));
   .
+!start2
   <-
   .send(camel2, tell, perf(10, 20));
   .send(camel2, tell, perf(10, fixed));
   .send(camel2, tell, perf(oi, fixed));
   .send(camel2, tell, perf("ola", fixed));
   .
+!start3
   <-
   .send(camel3, tell, perf(10, 20, 30));
   .send(camel3, tell, perf(10, 20, fixed));
   .send(camel3, tell, perf(oi, tchau, fixed));
   .send(camel3, tell, perf("ola", ateh, fixed));
   .
+!startX
   <-
   .send(camelX, tell, oi);
    .send(camelX, tell, perf(10));
    .send(camelX, tell, perf("ola", fixed));
    .send(camelX, tell, perf("oi", 10, fixed));
   .
+!startList1
   <-
   .send(camelL1, tell, [10, oi, "ola"]);
   .
+!startList2
   <-
   .send(camelL2, tell, [10, oi, "ola"]);
   .send(camelL2, tell, [20, oi, "ola"]);
   .

+!sayOne(X)
  :     true
  <-    .print("received: ", X);
  .
+!sayTwo(X, Y)
  :     true
  <-    .print("received: ", X, "; and :", Y);
  .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
