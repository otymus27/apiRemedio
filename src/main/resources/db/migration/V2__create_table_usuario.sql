CREATE TABLE tb_usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(50) NOT NULL,
    senha VARCHAR(50) NOT NULL,

    PRIMARY KEY (id)
) engine=InnoDB default charset=utf8;

INSERT INTO tb_usuarios ( login, senha) VALUES
    ( 'admin', 'admin'),
    ( 'otymus', '123')
