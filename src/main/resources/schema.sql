-- ----------------------------
-- Table structure for t_bing_wallpaper
-- ----------------------------
DROP TABLE IF EXISTS "t_bing_wallpaper";
CREATE TABLE "t_bing_wallpaper" (
  "bid"            INTEGER      NOT NULL ON CONFLICT ROLLBACK PRIMARY KEY AUTOINCREMENT,
  "hash"           CHAR(32)     NOT NULL,
  "date"           CHAR(13)     NOT NULL,
  "name"           VARCHAR(128)  NOT NULL,
  "code"           VARCHAR(64)  NOT NULL,
  "title"          VARCHAR(255),
  "caption"        VARCHAR(255),
  "description"    VARCHAR(1024),
  "copyright"      VARCHAR(255)  NOT NULL,
  "country"        CHAR(5)       NOT NULL,
  "hits"           INTEGER,
  "likes"          INTEGER,
  "downloads"      INTEGER,
  CONSTRAINT "unique_hash" UNIQUE ("hash" ASC) ON CONFLICT ROLLBACK
);

-- ----------------------------
-- Table structure for t_filming_location
-- ----------------------------
DROP TABLE IF EXISTS "t_filming_location";
CREATE TABLE "t_filming_location" (
  "fid"       INTEGER      NOT NULL ON CONFLICT ROLLBACK PRIMARY KEY AUTOINCREMENT,
  "name"      VARCHAR(64)  NOT NULL,
  "attribute" VARCHAR(32)  NOT NULL,
  "longitude" DOUBLE,
  "latitude"  DOUBLE,
  "map_url"   VARCHAR(128)
);

-- ----------------------------
-- Auto increment value for t_bing_wallpaper
-- ----------------------------
UPDATE "sqlite_sequence" SET seq = 0 WHERE name = 't_bing_wallpaper';

-- ----------------------------
-- Indexes structure for table t_bing_wallpaper
-- ----------------------------
CREATE UNIQUE INDEX "unique_bid" ON "t_bing_wallpaper" ("bid" ASC);

-- ----------------------------
-- Auto increment value for t_filming_location
-- ----------------------------
UPDATE "sqlite_sequence" SET seq = 0 WHERE name = 't_filming_location';

-- ----------------------------
-- Indexes structure for table t_filming_location
-- ----------------------------
CREATE UNIQUE INDEX "unique_fid" ON "t_filming_location" ("fid" ASC);
