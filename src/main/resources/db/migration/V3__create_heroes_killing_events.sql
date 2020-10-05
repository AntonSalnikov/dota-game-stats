CREATE TABLE "heroes_killings" (
  "id" bigserial NOT NULL,
  "match_id" bigint NOT NULL,
  "started_at" bigint NOT NULL,

  "hero_name" VARCHAR(255) NOT NULL,
  "killed_by" VARCHAR(255) NOT NULL,

  "create_time" TIMESTAMP NOT NULL,
  PRIMARY KEY("id"),
  CONSTRAINT "ref_purchased_items_to_matches" FOREIGN KEY ("match_id")
  REFERENCES "matches" ("id")
  MATCH SIMPLE
  ON DELETE RESTRICT
  ON UPDATE NO ACTION
  NOT DEFERRABLE
);
