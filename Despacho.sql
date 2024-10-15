create database Despacho
go
use Despacho
go
create table clientes
(
    idCliente int not null,
    Nombre varchar(50) not null,
    Estado varchar(50) not null,
    Credito DECIMAL(10,2),
    Deuda DECIMAL(10,2)
)
go
alter table clientes add constraint pk_idCliente primary key(idCliente);
go
--mysql
insert into clientes
values
    (4, 'Pablo', 'Baja California', 50, 10),
    (5, 'Lissandra', 'Baja California', 50, 100),
    (6, 'Leblanc', 'Baja California', 30, 93)
--sql server
insert into clientes
values
    (1, 'Pedro', 'Jalisco', 50, 10),
    (2, 'Luis', 'Jalisco', 50, 100),
    (3, 'Jose', 'Jalisco', 30, 93)
--postgresql
insert into clientes
values
    (7, 'Urgot', 'Chiapas', 50, 10),
    (8, 'Darius', 'Chiapas', 50, 100),
    (9, 'Zac', 'Chiapas', 30, 93)