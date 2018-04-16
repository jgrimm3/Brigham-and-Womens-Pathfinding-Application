CREATE TABLE Map_Nodes (
  nodeID             VARCHAR(10) PRIMARY KEY,
  xCoord              INTEGER,
  yCoord              INTEGER,
  floor               VARCHAR(2),
  building            VARCHAR(255),
  nodeType           VARCHAR(4),
  longName           VARCHAR(255),
  shortName          VARCHAR(255),
  status             INTEGER,
  xCoord3D            INTEGER,
  yCoord3D           INTEGER);

CREATE TABLE Map_Edges (
  edgeID              VARCHAR(255) PRIMARY KEY,
  startNodeID           VARCHAR(10),
  endNodeID             VARCHAR(10),
  status              INTEGER,
  CONSTRAINT fk_startNode FOREIGN KEY (startNodeID) REFERENCES Map_Nodes(nodeID) ON DELETE CASCADE,
  CONSTRAINT fk_endNode FOREIGN KEY (endNodeID) REFERENCES Map_Nodes(nodeID) ON DELETE CASCADE,
  CONSTRAINT unique_edge UNIQUE (startNodeID,endNodeID));

CREATE TABLE Kiosk (
  kioskID        VARCHAR(10) PRIMARY KEY,
  nodeID         VARCHAR (10) UNIQUE,
  description    varchar(255),
  CONSTRAINT fk_kiosk_node FOREIGN KEY (nodeID) REFERENCES Map_Nodes(nodeID) ON DELETE CASCADE);

Create Table Room (
  specialization VARCHAR(255),
  detail         VARCHAR(255),
  popularity     INT,
  isOpen         BOOLEAN,
  nodeID         VARCHAR(10) UNIQUE,
  CONSTRAINT fk_nodeID1 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID) ON DELETE CASCADE);

Create Table UserAccount (
  userID        VARCHAR(10) PRIMARY KEY,
  firstName     VARCHAR(255),
  middleName    VARCHAR(255),
  lastName      VARCHAR(255),
  language      VARCHAR(255),
  userType      VARCHAR(255));

CREATE TABLE UserPassword (
  userName    VARCHAR(15) UNIQUE,
  password    VARCHAR(15),
  userID      VARCHAR(10) UNIQUE,
  CONSTRAINT fk_password_userID FOREIGN KEY (userID) REFERENCES UserAccount(userID) ON DELETE CASCADE);

CREATE TABLE Staff (
  isWorking           BOOLEAN,
  isAvailable         BOOLEAN,
  languageSpoken      VARCHAR(255),
  userID              VARCHAR(10) UNIQUE,
  CONSTRAINT fk_staff_userID FOREIGN KEY (userID) REFERENCES UserAccount(userID) ON DELETE CASCADE);


CREATE TABLE Pathfinder(
  pathfinderID    VARCHAR(10) PRIMARY KEY,
  startNodeID     VARCHAR(10),
  endNodeID       VARCHAR(10),
  CONSTRAINT fk_pathfinder_startNode FOREIGN KEY (startNodeID) REFERENCES Map_Nodes(nodeID) ON DELETE CASCADE,
  CONSTRAINT fk_pathfinder_endNode FOREIGN KEY (endNodeID) REFERENCES Map_Nodes(nodeID) ON DELETE CASCADE);

CREATE TABLE Log(
  logID           VARCHAR(10),
  description     VARCHAR(255),
  logTime         TIMESTAMP,
  userID          VARCHAR(10),
  associatedID    VARCHAR(10),
  associatedType  VARCHAR(15));

Create Table Message (
  messageID     VARCHAR(10) PRIMARY KEY,
  message       VARCHAR(255),
  isRead        BOOLEAN,
  sentDate      DATE,
  senderID      VARCHAR(10),
  receiverID    VARCHAR(10),
  CONSTRAINT fk_message_senderID FOREIGN KEY (senderID) REFERENCES UserAccount(userID) ON DELETE CASCADE,
  CONSTRAINT fk_message_receiverID FOREIGN KEY (receiverID) REFERENCES UserAccount(userID) ON DELETE CASCADE);

Create Table Request (
  requestID     VARCHAR(10) PRIMARY KEY,
  requestType   VARCHAR(255),
  priority      INTEGER,
  isComplete    BOOLEAN,
  adminConfirm  BOOLEAN,
  startTime     TIMESTAMP,
  endTime       TIMESTAMP,
  nodeID        VARCHAR(10),
  messageID     VARCHAR(10) UNIQUE,
  password      VARCHAR(255),
  CONSTRAINT fk_message_messageID FOREIGN KEY (messageID) REFERENCES Message(messageID) ON DELETE CASCADE,
  CONSTRAINT fk_request_nodeID FOREIGN KEY (nodeID) REFERENCES Map_Nodes(nodeID) ON DELETE CASCADE);
