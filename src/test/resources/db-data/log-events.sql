TRUNCATE TABLE log_event_audit;
INSERT INTO log_event_audit (id, user_id, created_at, ip_address, device_type, browser, event_type, login_method, logout_reason, session_id)
VALUES
    (-1, 'tech_guru_01', '2023-09-28 12:34:56', '1.1.1.1', 'DESKTOP', 'Chrome', 'LOGIN', 'PASSWORD', NULL, '09c12574-72b3-4167-b0e1-75d76f0dfe5f'),
    (-2, 'codeMasterX', '2023-09-28 13:00:00', '1.1.1.2', 'MOBILE', 'Firefox', 'LOGOUT', 'SSO', 'USER_INITIATED', 'd1da7988-7340-4e31-83e8-8020ba21cfdd'),
    (-3, 'dev_ninja007', '2023-09-28 14:15:23', '1.1.1.3', 'TABLET', NULL, 'LOGIN', 'BIOMETRIC', NULL, 'be7f50ff-4f35-45c5-9de7-2ee92df02a10'),
    (-4, 'kotlinWiz', '2023-09-28 15:45:12', '1.1.1.4', 'DESKTOP', 'Safari', 'LOGOUT', 'TWO_FACTOR', 'SESSION_TIMEOUT', 'b4e869c1-e42f-4cf7-b372-e91ac1defd95'),
    (-5, 'backend_boss', '2023-09-28 16:20:45', '1.1.1.5', 'DESKTOP', 'Edge', 'LOGIN', 'TWO_FACTOR', NULL, 'c3459189-7f98-419f-bf22-42bb3da2ce36');
