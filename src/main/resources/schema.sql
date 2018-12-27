PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for t_bing_wall_paper
-- ----------------------------
DROP TABLE IF EXISTS "t_bing_wall_paper";
CREATE TABLE "t_bing_wall_paper" (
  "bid" INTEGER NOT NULL,
  "hash" CHAR(32) NOT NULL,
  "name" VARCHAR(64) NOT NULL,
  "code" VARCHAR(64) NOT NULL,
  "title" VARCHAR(64) NOT NULL,
  "attribute" VARCHAR(64) NOT NULL,
  "description" VARCHAR(128) NOT NULL,
  "copyright" VARCHAR(64) NOT NULL,
  "copyright_link" VARCHAR(128) NOT NULL,
  "show_date" CHAR(8) NOT NULL,
  "longitude" DOUBLE,
  "latitude" DOUBLE,
  "map_url" VARCHAR(128),
  "continent" VARCHAR(16) NOT NULL,
  "country" VARCHAR(16),
  "city" VARCHAR(16),
  "hits" INTEGER,
  "likes" INTEGER,
  "downloads" INTEGER,
  PRIMARY KEY ("bid")
);

PRAGMA foreign_keys = true;
