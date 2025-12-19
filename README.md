# Verteilte Systeme Praktikumsaufgabe 3
## Coordination and registry service for the control of robot arms

## 1. Objective

The goal is to develop a distributed system for controlling up to four robot
arms. Part of the task includes both the implementation of a naming service and
the coordination between multiple clients.

You will be developing a central registry service where robot arms can register.
A client queries this service for available arms and then communicates directly
with the selected arms. It is essential to prevent two clients from using the
same robot arm simultaneously. The focus here is on naming (name, address,
service) and coordination between the clients, not on the robotics itself.

## 2. Task

### 2.1. Registry-Service (Registry-Server)

Develop a central service that manages all clients and available robot arms.

- Design a lean application protocol, based on JSON, between Registry and
  clients.
- For each client and robot arm, store at least one ID, name, IP address, port,
  and type (client or robot).
- Implement the registry server in the programming language of your choice. It must be able to handle multiple parallel connections.
- Provide at least the following operations:
  - register(name, ip, port)
  - unregister(name)
  - list(type)
- Persistence can be in-memory; permanent storage is not required.
- Handling of duplicate names: Registrations with an already existing name are rejected by the registry service; the registry service returns a meaningful error message. Clients and robot nodes must react to this error (e.g., choose a new name or abort).

Choose and justify the specific concurrency strategy (e.g., thread per connection, thread pool, NIO) in the report.

### 2.2. Robot-Control (Robot-Node)

Note: For communication between the Robot node and the robot, use the library provided by the "CaDS" research group (Prof. Martin Becke) (repository: https://github.com/Transport-Protocol/CaDSPracticalExamVS). This library includes, among other things, the classes CaDSRoboticArmReal and CaDSRoboticArmSimulation.

Develop a Java program (Robot-Node) that can register a robot arm in the registry service and control it.

The robot node forms the logical server that the terminal clients address; the actual robots are not directly visible to the client.

- Register the arm at startup with its name and connection parameters.
- Keep the entry up-to-date at runtime (e.g., through periodic reports or a custom mechanism).
- Remove or invalidate the entry upon exit.
- Depending on the configuration, the robot node should use either the real implementation
  ```java ICaDSRoboticArm roboticArm = new CaDSRoboticArmReal(roboticArmHostAddress, roboticArmHostPort);```
  or the simulation
  ```java ICaDSRoboticArm roboticArm = new CaDSRoboticArmSimulation();```

Consider for yourself how you will handle connection interruptions, duplicate names, and an inaccessible registry.

### 2.3. Terminal-Clients

Implement a client (e.g., a terminal application) that uses the registry service and then sends control commands to one or more user-selected robot nodes.

- Periodically or upon request, query the list of known robot arms and display name and address/status.
- Allow the selection of a robot arm (e.g., by entering a name or ID).
- Next, establish a connection to the selected robot nodes and transmit the control commands either
  - via Remote Procedure Calls (see practical exercise 1) or
  - via a request/response mechanism you have designed.
- The terminal clients can be implemented in the programming language of your choice.
- To prevent multiple clients from accessing the same robot simultaneously, a token ring algorithm will be implemented. Each client will receive a unique, sequential ID from the registry. Clients can query predecessors and successors in the ring topology based on these IDs from the registry. This query must be repeated regularly to react to changes in the topology (e.g., clients terminating). The client with the first ID generates the token once, upon system startup.

Communication between and from the clients to the robot arm must not use the registry service, but must be exclusively direct via IP address and port. For acceptance testing, it is sufficient to set simple positions of individual motors (no complex motion sequences are required). Reusing program code from the solutions in the previous task is expressly permitted for this purpose. Only the Java-CaDS robot control library and a message serialization library may be used.

Your system should demonstrate at least the following scenarios:

- Successful process of registration, listing and control of a robot with multiple clients,
- When a client terminates, the entry in the registry service reacts accordingly (e.g., disappears according to your chosen mechanism) and the ring topology is adjusted accordingly by the clients.
- The registry service is unreachable → the terminal client reports a reproducible error situation.

For acceptance testing, either the real robot arms in the laboratory or the simulations (CaDSRoboticArmSimulation) can be used.

## 3. Documentation and submission

Create documentation that describes the following points:

1. Architectural overview
   - Overview of the registry service, robot node, and terminal client, and their communication relationships.
   - At least one flowchart (e.g., sequence diagram) for the registration and use of a robot.
2. Protocol description
   - Description of the defined operations (register, unregister, list) with syntax and semantics.
   - Description of the token transfer
   - At least one complete example (request and response) per operation.
   - Description of the behavior in case of errors (e.g., duplicate name, invalid queries).
3. Key design decisions
   - Selection of your concurrency strategy (e.g., thread per connection, thread pool, NIO).
   - Mechanism for keeping entries up-to-date (e.g., periodic reports, timeouts).

Note on the simulation: The CaDSRoboticArmSimulation class requires JavaFX. To run it, you can download the JavaFX SDK locally and add it via VM arguments when starting your application.
Example (adjust path): \
--module-path /path/to/javafx-sdk-21.0.2/lib \
--add-modules=javafx.controls,javafx.fxml \
Ensure these options are set when starting the Robot node if you are using
the simulation.