CREATE TABLE Map_Nodes (
  nodeID             VARCHAR(10) PRIMARY KEY,
  xCoord              INTEGER,
  yCoord              INTEGER,
  floor               VARCHAR(2),
  building            VARCHAR(255),
  nodeType           VARCHAR(4),
  longName           VARCHAR(255),
  shortName          VARCHAR(255),
  status             INTEGER);

CREATE TABLE Map_Edges (
  edgeID              VARCHAR(255) PRIMARY KEY,
  startNode           VARCHAR(10),
  endNode             VARCHAR(10),
  status              INTEGER,
  CONSTRAINT fk_startNode FOREIGN KEY (startNode) REFERENCES Map_Nodes(nodeID),
  CONSTRAINT fk_endNode FOREIGN KEY (endNode) REFERENCES Map_Nodes(nodeID));

Create Table Room (
  specialization VARCHAR(255),
  detail         VARCHAR(255),
  popularity     INT,
  isOpen         BOOLEAN,
  nodeID         VARCHAR(10) UNIQUE,
  CONSTRAINT fk_nodeID1 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));

Create Table Transport (
  directionality VARCHAR(255),
  floors         VARCHAR(255),
  nodeID         VARCHAR(10) UNIQUE,
  CONSTRAINT fk_nodeID2 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));

Create Table Exit (
  isFireExit      BOOLEAN,
  isArmed         BOOLEAN,
  nodeID          VARCHAR(10) UNIQUE,
  CONSTRAINT fk_nodeID3 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));

Create Table Hallway (
  popularity     INT,
  nodeID         VARCHAR(10) UNIQUE,
  CONSTRAINT fk_nodeID4 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));


Create Table UserAccount (
  userID        VARCHAR(10) PRIMARY KEY,
  firstName     VARCHAR(255),
  middleInitial VARCHAR(255),
  lastName      VARCHAR(255),
  language      VARCHAR(255));

Create Table Request (
  requestID     VARCHAR(10) PRIMARY KEY,
  requestType   VARCHAR(255),
  priority      INTEGER,
  isComplete    BOOLEAN,
  adminConfirm  BOOLEAN,
  nodeID        VARCHAR(10) UNIQUE,
  employeeID      VARCHAR(10) UNIQUE,
  messageID     VARCHAR(10) UNIQUE,
  CONSTRAINT fk_message_messageID FOREIGN KEY (messageID) REFERENCES Request(messageID),
  CONSTRAINT fk_request_nodeID FOREIGN KEY (nodeID) REFERENCES Map_Nodes(nodeID),
  CONSTRAINT fk_request_employeeID FOREIGN KEY (employeeID) REFERENCES UserAccount(userID));

Create Table Message (
  messageID     VARCHAR(255) PRIMARY KEY,
  message       VARCHAR(255),
  isRead        BOOLEAN,
  senderID      VARCHAR(10) UNIQUE,
  receiverID    VARCHAR(10) UNIQUE,
  
  CONSTRAINT fk_message_senderID FOREIGN KEY (senderID) REFERENCES UserAccount(userID),
  CONSTRAINT fk_message_receiverID FOREIGN KEY (receiverID) REFERENCES UserAccount(userID));
