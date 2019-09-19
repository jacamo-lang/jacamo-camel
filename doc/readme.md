# JaCaMo Component
```
DOCUMENTATION UNDER DEVELOPMENT!
```
## Overview
This project contains an implementation of two custom Apache Camel components that gives Jason agents and CArtAgO artifact from a MAS JaCaMo project a way to communicate with other services.

```
If the reader is only interested in how to apply the components in a JaCaMo MAS, he/she may feel free to skip to the Usage section.
```

### Table of contents
1. [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [How to use](#how-to-use)
  * [Defining a context route](#defining-a-context-route)
  * [Using custom context configuration](#using-custom-context-configuration)
2. [Notes](#notes)
3. [Examples](#examples)
  * [Agent-only 'main'](#agent-only-'main')
  * [PostgreSQL](#postgresql)
  * [MQTT](#mqtt)

<<<<<<< HEAD
Apache Camel is a framework based on Enterprise Integration Patterns that aims to resolve  integration problems between components.

Camel's approach to integration issues gives systems an abstraction as a component to be accessed through *endpoints* that transmits messages encapsulated in *exchange objects*.
When a component should send data, the developer establishes a *consumer* endpoint, that consumes the data and encapsulates it in an exchange object to be send. In the opposite flow, when a component must receive data, the former retrieves the latter sent as an exchange object with a *producer* endpoint, and then produces the data in a proper format to be used by the said component. The origin (consumer) and destinies (producers) endpoints are set in a camel *route*. Within a route, optional data manipulation can be defined as well. A graphical overview of this approach can be seen in the figure below.

![Communication Flow](/images/CamelApproach.pdf?raw=true)

The components provided by this project follows an important and clear fundament: **the integration shall not interfere in the MAS comprehension and development**. In essence, this means the developers of the MAS must not worry or change how they understand the system and uses it. In simple terms, if a JaCaMo agent interacts with the environment through action and perception, it **must** sustain this method when interacting with external systems integrated as environmental elements.

In the following sections this document will present each component individually, its essence, caveats, and when they should be applied. Then, a usage section provides the user simple instructions on how to include and make use of the camel components in one's MAS. The next sections contains notes and explanations of application examples.

## jacamo-agent


## camel-artifact



![Communication Flow](/images/CommunicationFlow.pdf?raw=true)

=======
## jacamo-agent
This component enables the modeling of external systems as internal agents to interact with other native agents. This means that native Jason agents will understand such external systems simply as another agents, and will be able to interact with them by sending and receiving ACL messages.
Since the agents already communicated via `.send`, this behavior needed to be sustained. Such effect can be obtained by overriding the agents default architecture and routing the action `send` to camel routes if there is an endpoint to receive a message (i.e. a consumer).
It is important to note that if the developer wishes to make a custom agent architecture, he/she must be careful when overriding the internal action.

## jacamo-artifact
This component enables the modeling of external systems as environmental CArtAgO artifacts to interact with other native agents. This means that native Jason agents will understand such external systems as native artifacts, and will be able to interact with them by observing properties, listening to signals, and acting over their operations.
Since the agents already communicated via `.send`, this behavior needed to be sustained. Such effect can be obtained by overriding the agents default architecture and routing the action `send` to camel routes if there is an endpoint to receive a message (i.e. a consumer).
It is important to note that if the developer wishes to make a custom agent architecture, he/she must be careful when overriding the internal action.
>>>>>>> b6f2f5f460a36d441bf01daa9dd8293acbb97ec6

## Getting Started
### Prerequisites
1. Gradle: you can find all info on how to install gradle [Here](https://gradle.org/install/).


### How to use
1. Create a basic gradle build file and add the code below to it, or use the `buildSample.gradle` file and rename it to `build.gradle`.

```
repositories {
   mavenCentral()

   maven { url "https://raw.github.com/jacamo-lang/mvn-repo/master" }
   maven { url "http://jacamo.sourceforge.net/maven2" }
}

dependencies {
  compile 'org.jacamo:jacamo:0.8'

  compile group: 'org.jacamo-lang',     name: 'camel-jason' ,   version: '1.0'

  compile group: 'org.apache.camel', name: 'camel-core', version: '2.22.1'

  compile group: 'org.springframework', name: 'spring-context', version: '5.0.10.RELEASE'

  compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.25'

  compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25'
}
```


2. Add the dependencies of the components you wish to integrate. Google "maven *mycomponent*", the first link should be from [this website](https://mvnrepository.com/), usually you would want the latest version, and go in the "gradle" tab, simply copy an paste in your build file. For instance, if you want to use mySQL you would add:

```
// https://mvnrepository.com/artifact/mysql/mysql-connector-java
compile group: 'mysql', name: 'mysql-connector-java', version: '8.0.13'
```

**NOTE** For JDK9+ users, see [Notes Section](##notes).

3. Create each of your contexts in separated `.xml` files and define only the routes within them. The files should maintain the following format:

```
<!-- This first header is mandatory -->
<routes xmlns="http://camel.apache.org/schema/spring">
    <route id="yourRouteIdOne">
        <from uri="myComponent:address"/>
        <to uri="jacamo-agent:agentBob"/>
    </route>

    <route id="yourRouteIdTwo">
        <from uri="jacamo-agent:myComponentName"/>
        <to uri="myComponent:address"/>
    </route>
</routes>
```

Each file creates a context, so you can have parellel routes running.
Check out the *Defining a context route* and *Examples* section for more.

4. Add the following to your `.jcm` project:

```
platform: jasonComponent.JasonCamel("routes-file-name.xml")
```

5. Run `gradle run` to start you `.jcm` with camel.

### Defining a context route
The context is defined mainly using Camel's simple language for XML.
Within the context, you can have many routes, defined by `<route> ... </route>`.

Usually, in each route you have a consumer endpoint, denoted by "from", and one or more producer endpoints, denoted by "to".

#### Producer endpoint
The purpose of the producer is to generate messages to Jason agents.
When defining a producer endpoint you must use the following pattern:

`<to uri=jacamo-agent:Agent?propertyOne=foo&propertyTwo=bar/>`

Where "Agent" is the agent's name (e.g. bob, alice), and "propertyOne" and "propertyTwo" are optional properties you can define to the message.

#### Consumer endpoint
The purpose of the producer is to receive messages that Jason agents tried to send to another component, believing it was an agent as well.
Defining a consumer endpoint is analog, the difference being that the producer URI is the name of the other components' representative agent. I.e. to whom the Jason agents will send the messages.

```
<from uri="jacamo-agent:Address?propertyOne=foo&propertyTwo=bar"/>
```

In this situation, Address is receivers name. Usually you just repeat the integrated component's name (e.g. `uri=jacamo-agent:MQTT`, `uri=jacamo-agent:REST`, ...)

Also, the producer will **apply** the properties to the message, while the consumer will **filter** messages with certain properties.

#### Examples
Let's say you want to deliver a message from component "phone" to agent "jomi". First of all you must be sure that "phone" is a valid camel component with its dependency imported in gradle. You can check Camel's official components [here](http://camel.apache.org/component-list-grouped.html).

Then, you would define a route as follow:

```
<route id="fromPhoneToJomi">
  <from uri="phone:myPhone?propertyOne=ok"/>
  <to uri="jacamo-agent:jomi"/>
</route>
```

This will create a consumer that consumes messages from "phone" component with some property defined, and generate a jason message with the performative "tell" to the agent "jomi".

The content of the message, since is not defined in the URI, is the body of the message in the exchange object generated by the "phone" consumer, and it varies according to the component's nature. You should always know how each component creates the message body in order to correctly parse to the agents.

Now if you want to order another agent "mateus" to achieve the plan !report("jason"), when "jomi" sends a message to "myPhone" containing the string "jason" in it, you could make another route as:

```
<route id="fromJomiToMyPhone">
  <from uri="jacamo-agent:myPhone?source=jomi"/>
  <choice>
    <when>
      <simple>${in.body} contains "jason"</simple>
      <to uri="jacamo-agent:mateus?performative=achieve&content=report("jason")"
    </when>
  </choice>
</route>
```

And finally put that route in your context:

```
<routes xmlns="http://camel.apache.org/schema/spring">
    <route id="fromPhoneToJomi">
      <from uri="phone:myPhone?propertyOne=ok"/>
      <to uri="jacamo-agent:jomi"/>
    </route>

    <route id="fromJomiToMyPhone">
      <from uri="jacamo-agent:myPhone?source=jomi"/>
      <choice>
        <when>
          <simple>${in.body} contains "jason"</simple>
          <to uri="jacamo-agent:mateus?performative=achieve&content=report("jason")"
        </when>
      </choice>
    </route>
</routes>
```

Important, if you want to define two routes with the same producer, and diferent conditionals, you **MUST** put in a single route and use <when\>, which is analog to the usual "if" statement.
So in the example above, if the source for "myPhone" can be agents "Jomi" or "Maicon", you must define your consumer route as:

```
<route id="fromAgentsToMyPhone">
  <from uri="jacamo-agent:myPhone"/>
  <choice>
    <when>
      <simple>${exchangeProperty[source]} == "jomi" and ${in.body} contains "jason"</simple>
      <to uri="jacamo-agent:mateus?performative=achieve&content=report("jason")"
    </when>
    <when>
      <simple>${exchangeProperty[source]} == "maicon"</simple>
      <.... do a different thing ....>
    </when>
  </choice>
</route>
```

If you try the following, it **WON'T** work:

```
<route id="fromJomiToMyPhone">
  <from uri="jacamo-agent:myPhone?source=jomi"/>
  <to uri="jacamo-agent:mateus?performative=achieve&content=report("jason")"
</route>

<route id="fromMaiconToMyPhone">
  <from uri="jacamo-agent:myPhone?source=maicon"/>
  <.... do a different thing ....>
</route>
```

Since both consumers have the address "myPhone", one route would overwrite another.
This also goes to different route files, you **should not** define the same consumer address in different routes, as only one will consume the message.

#### Routes properties and configurations

The producer might receive an exchange object to process with properties alrealdy defined, but it will always overwrite them if it was also declared in the URI, giving priority to the user.

Here's a list of possible producer properties to be defined as a URI argument:

<table class="tg">
  <tr>
    <th class="tg-baqh" colspan="3">**Producer Endpoint**</th>
  </tr>
  <tr>
    <td class="tg-0pky">Attribute's name</td>
    <td class="tg-0pky">Default value</td>
    <td class="tg-0pky">Description</td>
  </tr>
  <tr>
    <td class="tg-0pky">performative</td>
    <td class="tg-0pky">tell</td>
    <td class="tg-0pky">the performative or illocutionary force, is the "purpose" of the message.<br>It can be: tell, untell, achieve, unachieve, askOne, askAll, tellHow, untellHow, askHow;</td>
  </tr>
  <tr>
    <td class="tg-0pky">source</td>
    <td class="tg-0pky">camel</td>
    <td class="tg-0pky">the origin of the message, the receiver agent will believe the "source" is another agent and might reply to it;</td>
  </tr>
  <tr>
    <td class="tg-0pky">msgId</td>
    <td class="tg-0pky">*auto generated*</td>
    <td class="tg-0pky">an id to locate the message, mainly serves to send replies to messages with "askOne" and "askAll" performatives;</td>
  </tr>
  <tr>
    <td class="tg-0pky">irt</td>
    <td class="tg-0pky">*null*</td>
    <td class="tg-0pky">a history of the replies to messages;</td>
  </tr>
  <tr>
    <td class="tg-0pky">content</td>
    <td class="tg-0pky">*null*</td>
    <td class="tg-0pky">the message's body, it's really important to know the format of the content since agents may not receive it correctly.<br>The user should take care of handling well this element.</td>
  </tr>
  <tr>
    <td class="tg-0lax" colspan="3">Note: all the elements, despite having a default value, will give priority to the exchange's properties over the default value.<br>The priority order is as follow: user input &gt; exchange property &gt; default value.</td>
  </tr>
</table>

Here's also a list of possible consumer properties to be defined as a URI argument:

<table class="tg">
  <tr>
    <th class="tg-baqh" colspan="3">**Consumer Endpoint**</th>
  </tr>
  <tr>
    <td class="tg-0pky">Attribute's name</td>
    <td class="tg-0pky">Default value</td>
    <td class="tg-0pky">Description</td>
  </tr>
  <tr>
    <td class="tg-0pky">performative</td>
    <td class="tg-0pky">\*</td>
    <td class="tg-0pky">-</td>
  </tr>
  <tr>
    <td class="tg-0pky">source</td>
    <td class="tg-0pky">\*</td>
    <td class="tg-0pky">-</td>
  </tr>
  <tr>
    <td class="tg-0pky">irt</td>
    <td class="tg-0pky">\*</td>
    <td class="tg-0pky">-</td>
  </tr>
  <tr>
    <td class="tg-0lax" colspan="3">Note: all the consumer's elements serve as a filter, so if the user writes `performative=tell&amp;source=jomi`, only the messages with "tell" as performative and "jomi" as source will be consumed.
    Also, if you wish to filter the contents, you can easily use Camel's Simple language operator `contains` (e.g. `${body} contains "oi"` )</td>
  </tr>
</table>

For more examples check the *Examples* section down below.

### Using custom context configuration
In Camel, you can also use custom configurations in your context. This asset is mainly used when defining Beans.
If this resource is needed, you can create a context in another `.xml` file without routes, and adding ` --my-context-beans.xml` in the path you used for the routes.
Here's an example:

This would be `beans.xml`:
```
<beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="myDearBean" class="beanClass">
    <... properties and such ...>
  </bean>
</beans>
```

And `project.jcm` would have:

```
platform: jasonComponent.JasonCamel("routes-file-name.xml --beans.xml")
```

### Debugging and logging
Jason Component uses two types of logger:
#### **Log4j**

It logs the project initialization more thoroughly, as well as the history of messages within the exchange object.

It is an optional feature and is imported within your gradle build file. To turn it off you can simply comment out the following snippet from the dependencies section:

```
// ------------- log dependencies -------------
// https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.25'

// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25'
```

This dependency is important for testing and debugging your routes. You can define a producer that won't affect the exchange object using `<log message=""/>`.

Here's an example:

```
<routes xmlns="http://camel.apache.org/schema/spring">
    <route id="loggingRoute">
      <from uri="jacamo-agent:myPhone?source=alice"/>
      <log message="Incoming call from alice!"/>
      <to uri="jacamo-agent:bob?content=received(myPhone, alice)"/>
      <to uri="phone:myPhone"/>
    </route>
</routes>
```

If your run it, in your terminal you would expect the following:

* Lots of creation and definition messages from [JasonCamel] and [    main]


* `[JasonConsumer] Message received: <mid1,alice,tell,myPhone,Message>` *<- The message was consumed.*
* `[JasonEndpoint] Generating exchange object from: <mid1,alice,tell,myPhone,Message>` *<- The consumer sent the message for processing to become an Exchange object for the upcoming producer(s)*
* `[JasonEndpoint] Message's properties: {performative=tell, msgId=mid1, source=alice}` *<- Exchange's properties.*
* `[JasonEndpoint] Message's body      : Message` *<- Exchange's message.*
* `[JasonConsumer] Exchange created for processing: Exchange[]` *<- The Exchange was successfully created and processed.*
* `[     alice] loggingRoute         INFO  Incoming call from alice!`  *<- Logging. The 'alice' in the begining is because the Exchange's source property is 'alice'.*
* `[JasonProducer] Jason message: <<auto>1,alice,tell,bob,received(myPhone, alice)>` *<- The producer generated a Jason Message with the shown poperties.*
* `[JasonProducer] Agent received`

* Logging from phone component

#### **Internal Logger**

The Java logger class is also used as an internal logger, that will log the creation of endpoint, the consumption and production of exchange objects, and errors.

In the previous snippet, the log entries that begins with [JasonCamel], [JasonEndpoint], [JasonConsumer] and [JasonProducer] are generate from the internal logger, while the entries that have a lot of white spaces in the source, as in [&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;main] for example, are generated by Log4j.


## Notes:
* If you are running your project project with JDK 9 and over, you also need the JAX-B dependency, for binding your `.xml` files. Just add the following to your gradle dependencies section:

```
// https://mvnrepository.com/artifact/javax.xml/jaxb-api
compile group: 'javax.xml', name: 'jaxb-api', version: '2.1'
```

* For a sample of gradle build file, use `build.gradle` from this folder.
* See testeRota.xml for an example. Run it with `gradle run` from this folder.
* You can visit [this page](http://camel.apache.org/simple.html) for documentation on Camel's Simple language, to better define your routes.

## Examples
### Agent-only 'main'
Although this example does not use other camel components, it is good to ilustrate the behaviour of JasonComponent in different situations.
To use you simply run `gradle run`, the routes are defined in `routeMain.xml` and `routeMain2.xml`.
In the `.xml` file, you can identify only one Jason Consumer endpoint, and several Jason Producer endpoints. That's to show that you can filter your messages through different properties, like its source, or if it is a reply to another message.
It's important to note: if you want different agents to send to the same component, you **must use a single producer**. This is better explained in *Defining a context* section above.
Note in `routeMain2.xml` that is possible to filter a message with the consumer's URI or with the <when> selector.

// REVIEW: rodou certinho, mas fiquei um pouco perdido na depuracao do codigo, talvez daria pra descrever o que e esperado que os agentes facam, o que tu acha? ai fica mais claro o que esperar no debug. acho que o mesmo vale pros outros exemplos

### PostgreSQL
This is an example that represents the use and need of custom context files.
This example uses a local PostgreSQL server, so you won't be able to use without having a running server, changing the configurations in `applicationContext.xml` and the table queried in the `routeSQL.xml` file.
To run use `gradle sql`, the routes are defined in `routeSQL.xml`.
Notice that the project, defined in `sql.jcm`, uses a custom context configuration.
To connect with an SQL database, each database managing technology (e.g. PostgreSQL, mySQL) provides its driver class that is used by Apache to establish the connection. Also, the data delivered from the DB manager must be parsed for the agents, in a Java class.
Such classes are programmed in Java must be called through beans, that need to be defined in the context configuration file.
You can look the `applicationContext.xml` file and notice the beans being defined, and one is from a custom class. When defining your own beans classes, you also need to put them inside a source folder, usually in `src/java`.
A more thorough example can be seen [here](https://dzone.com/articles/data-transformation-csv-to-xml-using-apache-camel).

### MQTT

The MQTT example is also important since it demonstrate an use of message transformation.
This example uses a local mqtt broker. You can easily create one with mosquitto [here](http://mosquitto.org/).
To run use `gradle mqtt`, the routes are defined in `routeMQTT.xml`.
The broker sends the published data in a byte array format. So that the agents could received the information, `convertBodyTo` method was used.
Different components may send data in specific formats. Usually you will need to use custom bean methods, and parse the data with Java, you can see an example of this in the *PostgreSQL* example.
