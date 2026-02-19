CREATE TABLE request_log (
                             id BIGSERIAL PRIMARY KEY,
                             path VARCHAR(255),
                             method VARCHAR(10),
                             status INT,
                             duration BIGINT,
                             user_id VARCHAR(255),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_id ON request_log(user_id);
CREATE INDEX idx_created_at ON request_log(created_at);
