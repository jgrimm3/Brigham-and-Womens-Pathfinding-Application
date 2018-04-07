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
  CONSTRAINT fk_startNode FOREIGN KEY (startNode) REFERENCES Map_Nodes(nodeID) CASCADE ON DELETE,
  CONSTRAINT fk_endNode FOREIGN KEY (endNode) REFERENCES Map_Nodes(nodeID) CASCADE ON DELETE,
  CONSTRAINT unique_edge UNIQUE (startNode,endNode));

Create Table Room (
  specialization VARCHAR(255),
  detail         VARCHAR(255),
  popularity     INT,
  isOpen         BOOLEAN,
  nodeID         VARCHAR(10) UNIQUE,
  CONSTRAINT fk_nodeID1 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID) CASCADE ON DELETE);

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
  nodeID        VARCHAR(10),
  messageID     VARCHAR(10) UNIQUE,
  password      VARCHAR(255),
  CONSTRAINT fk_message_messageID FOREIGN KEY (messageID) REFERENCES Request(messageID) CASCADE ON DELETE,
  CONSTRAINT fk_request_nodeID FOREIGN KEY (nodeID) REFERENCES Map_Nodes(nodeID) CASCADE ON DELETE);

Create Table Message (
  messageID     VARCHAR(255) PRIMARY KEY,
  message       VARCHAR(255),
  isRead        BOOLEAN,
  senderID      VARCHAR(10),
  receiverID    VARCHAR(10),
  CONSTRAINT fk_message_senderID FOREIGN KEY (senderID) REFERENCES UserAccount(userID) CASCADE ON DELETE,
  CONSTRAINT fk_message_receiverID FOREIGN KEY (receiverID) REFERENCES UserAccount(userID) CASCADE ON DELETE);
