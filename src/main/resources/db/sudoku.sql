--SQLite

CREATE TABLE sudoku (
    id integer primary key,
    type varchar(20),
    difficulty integer,
    status varchar(10),
    player varchar(30),
    initial_board varchar(300),
    solving_notes varchar(2000),
    solving_sequence varchar(300),
    board varchar(300),
    elapsed_time integer,
    created integer,
    updated integer
)