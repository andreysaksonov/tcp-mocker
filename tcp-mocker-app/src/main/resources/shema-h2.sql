create table Recording (
    id bigint(19) auto_increment not null primary key,
    timestamp timestamp not null,
    request varbinary(8192) not null,
    reply varbinary(8192) not null
);
