--liquibase formatted sql

--changeset scholanova:1
CREATE TABLE IF NOT EXISTS STOCKS (
  ID                  SERIAL          NOT NULL,
  NAME                VARCHAR(255)    NOT NULL,
  TYPE                VARCHAR(255)    NOT NULL,
  VALUE               INTEGER    	  NOT NULL,
  STORE_ID            INTEGER    	  NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (store_id) REFERENCES STORES (id)
);
