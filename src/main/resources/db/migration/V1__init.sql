CREATE TABLE IF NOT EXISTS log_event_audit
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address    VARCHAR(255) NOT NULL,
    device_type   VARCHAR(255) NOT NULL,
    browser       VARCHAR(255),
    event_type    VARCHAR(255) NOT NULL,
    login_method  VARCHAR(255),
    logout_reason VARCHAR(255),
    session_id    VARCHAR(255)
);
