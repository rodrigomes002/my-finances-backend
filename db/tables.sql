create table lancamentos(
	id serial primary key,
	data date not null,
	valor decimal not null,
	descricao varchar not null
);

create table arquivos_processados(
	id bigserial primary key,
	hash_sha256 varchar(64) not null unique,
	nome_arquivo varchar,
	processado_em timestamp not null
);
