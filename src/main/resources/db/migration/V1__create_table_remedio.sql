CREATE TABLE tb_remedios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    via VARCHAR(50) NOT NULL,
    lote VARCHAR(50) NOT NULL,
    quantidade int not null ,
    validade VARCHAR(50) NOT NULL ,
    laboratorio VARCHAR(50) NOT NULL ,

    PRIMARY KEY (id)
) engine=InnoDB default charset=utf8;

INSERT INTO tb_remedios ( nome, via, lote, quantidade, validade, laboratorio) VALUES
    ( 'Buscopan 40 mg', 'ORAL', '2', 10, '2023-03-10', 'MEDLEY'),
    ( 'Dipirona 500mg', 'ORAL', '3', 10, '2026-04-15', 'MEDLEY'),
    ( 'Aspirina 400mg', 'ORAL', 'A1', 25, '2026-12-15', 'BAYER');