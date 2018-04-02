CREATE TABLE map_nodes (
  nodeID             VARCHAR(10) PRIMARY KEY,
  xCoord              INTEGER,
  yCoord              INTEGER,
  floor               VARCHAR(2),
  building            VARCHAR(255),
  nodeType           VARCHAR(4),
  longName           VARCHAR(255),
  shortName          VARCHAR(255),
  status             BOOLEAN);

CREATE TABLE map_edges (
  edgeID              VARCHAR(255) PRIMARY KEY,
  startNode           VARCHAR(255),
  endNode             VARCHAR(255),
  status              BOOLEAN);

Create Table Rooms (
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
