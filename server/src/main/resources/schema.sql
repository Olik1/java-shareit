DROP TABLE IF EXISTS bookings, items, users, requests, comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    512
) NOT NULL ,
    CONSTRAINT email UNIQUE
(
    email
),
    CONSTRAINT pk_user PRIMARY KEY
(
    id
),
    CONSTRAINT UQ_USER_EMAIL UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY
    NOT
    NULL,
    description
    VARCHAR
(
    255
) NOT NULL,
    requester_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY
(
    requester_id
) REFERENCES users
(
    id
)
                      ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    512
) NOT NULL,
    available BOOLEAN NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY
(
    id
),
    owner_id BIGINT REFERENCES users
(
    id
),
    request_id BIGINT REFERENCES requests
(
    id
)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY
    NOT
    NULL,
    starts
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    ends
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    item_id
    BIGINT
    NOT
    NULL,
    booker_id
    BIGINT
    NOT
    NULL,
    status
    VARCHAR,
    FOREIGN
    KEY
(
    item_id
) REFERENCES items
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    booker_id
) REFERENCES users
(
    id
)
  ON DELETE CASCADE

    );
CREATE TABLE IF NOT EXISTS comments
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY
    NOT
    NULL,
    text
    VARCHAR
(
    500
) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
)
                      ON DELETE CASCADE,
    FOREIGN KEY
(
    author_id
) REFERENCES users
(
    id
)
                      ON DELETE CASCADE
    );
CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY
    NOT
    NULL,
    description
    VARCHAR
(
    512
) NOT NULL,
    requester_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY
(
    requester_id
) REFERENCES users
(
    id
)
                      ON DELETE CASCADE
    );
