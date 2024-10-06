CREATE TABLE IF NOT EXISTS session_state_update_event_audit
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            VARCHAR(255) NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_state VARCHAR(50)  NOT NULL
);
