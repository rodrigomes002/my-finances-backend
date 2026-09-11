create table lancamentos(
	id serial primary key,
	data date not null,
	valor decimal not null,
	descricao varchar not null
);
