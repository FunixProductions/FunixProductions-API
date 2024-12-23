TRUNCATE TABLE twitch_client_tokens;

ALTER TABLE twitch_client_tokens ADD COLUMN token_type VARCHAR(255) NOT NULL default 'VIEWER';

DROP TABLE twitch_event_sub_streamers;