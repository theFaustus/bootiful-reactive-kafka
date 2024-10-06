TRUNCATE TABLE session_state_update_event_audit;
INSERT INTO session_state_update_event_audit (id, user_id, session_state)
VALUES (-1, 'codeMasterX', 'ACTIVE'),
       (-2, 'codeMasterX', 'INACTIVE'),
       (-3, 'user_789', 'IDLE'),
       (-4, 'user_321', 'ACTIVE'),
       (-5, 'user_654', 'IDLE'),
       (-6, 'user_987', 'INACTIVE');

