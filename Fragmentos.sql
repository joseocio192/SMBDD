CREATE DATABASE Fragmentos;
go
use Fragmentos;
GO
create table Fragmentos
(
    Fragmento VARCHAR(50) NOT NULL primary key,
    BaseDeDatos VARCHAR(50) NOT NULL,
    CriterioFrag VARCHAR(25) NOT NULL,
    Atributos VARCHAR(100) NOT NULL,
    Gestor VARCHAR(50) NOT NULL,
    [Servidor(IP)] VARCHAR(50) NOT NULL,
    Usuario VARCHAR(50) NOT NULL,
    Contraseña VARCHAR(50) NOT NULL
)