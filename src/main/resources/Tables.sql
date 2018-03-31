CREATE TABLE map_nodes (
  nodeID             CHAR(10) PRIMARY KEY,
  xCoord              INTEGER,
  yCoord              INTEGER,
  floor               VARCHAR(2),
  building            VARCHAR(255),
  nodeType           VARCHAR(4),
  longName           VARCHAR(255),
  shortName          VARCHAR(255),
  teamAssigned       VARCHAR(255));

CREATE TABLE map_edges (
  edgeID              VARCHAR(255),
  startNode           VARCHAR(255),
  endNode             VARCHAR(255));

Create Table Rooms (
  specialization VARCHAR(255),
  detail         VARCHAR(255),
  popularity     INT,
  isOpen         BOOLEAN,
  nodeID         CHAR(10),
  CONSTRAINT fk_nodeID1 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));

Create Table Transport (
  direcitonality VARCHAR(255),
  floors         VARCHAR(255),
  nodeID         CHAR(10),
  CONSTRAINT fk_nodeID2 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));

Create Table Exit (
  isFireExit      BOOLEAN,
  isArmed         BOOLEAN,
  nodeID          CHAR(10),
  CONSTRAINT fk_nodeID3 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));

Create Table Hallway (
  popularity     INT,
  nodeID         CHAR(10),
  CONSTRAINT fk_nodeID4 FOREIGN KEY (nodeID) REFERENCES map_nodes(nodeID));
