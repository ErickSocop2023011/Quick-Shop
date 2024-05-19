drop database if exists DB_QuickShop;


create database DB_QuickShop;

use DB_QuickShop;

create table Clientes(
	clienteID int,
    nombreClientes varchar (45),
    apellidosClientes varchar (45),
    direccionClientes varchar (128),
    NIT varchar (13),
    telefonoClientes varchar (13),
    correoClientes varchar (128),
    primary key PK_ClienteID(clienteID)
);

create table TipoProducto(
	codigoTipoProducto int ,
    descripcionProducto varchar (45),
    primary key PK_TipoProducto(codigoTipoProducto)
);

create table Compras(
	numeroDocumento int,
    fechaDocumento date,
    descripcionCompra varchar(60),
    totalDocumento decimal(20,2),
    primary key PK_NumeroDocumento(numeroDocumento)
);

create table proveedores(
	codigoProveedor int,
    NITProveedor varchar(13),
    nombresProveedor varchar(60),
    apellidosProveedor varchar (60),
    direccionProveedor varchar (150),
    razonSocial varchar (60),
    contactoPrincipal varchar (100),
    paginaWeb varchar(50),
    telefonoProveedor varchar (13),
    emailProveedor varchar(50),
    primary key PK_codigoProveedor(codigoProveedor)
);

create table CargoEmpleado (
	codigoCargoEmpleado int,
    nombreCargo varchar (45),
    descripcionCargo varchar (82),
    primary key PK_codigoCargoEmpleado (codigoCargoEmpleado)
);

create table Productos(
	codigoProducto varchar(15),
	descripcionProducto varchar(15),
	precioUnitario decimal(10,2),
	precioDocena decimal(10,2),
	precioMayor decimal(10,2),
	imagenProducto varchar(45),
	existencia int,
	codigoTipoProducto int,
	codigoProveedor int,
	primary key  PK_codigoProducto (codigoProducto),
	foreign key (codigoTipoProducto) references TipoProducto(codigoTipoProducto),
	foreign key (codigoProveedor) references Proveedores(codigoProveedor)
);

CREATE TABLE DetalleCompra(

codigoDetalleCompra int,
costoUnitario decimal(10,2),
cantidad int,
codigoProducto varchar(15),
numeroDocumento int,
PRIMARY KEY PK_codigoDetalleCompra (codigoDetalleCompra),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto),
FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento)
    
);

CREATE TABLE Empleados(

codigoEmpleado int,
nombresEmpleado varchar(50),
apellidosEmpleado varchar(50),
sueldo decimal(10,2),
direccion varchar(150),
turno varchar(15),
codigoCargoEmpleado int,
PRIMARY KEY PK_codigoEmpleado (codigoEmpleado),
FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoEmpleado(codigoCargoEmpleado)

);


CREATE TABLE Factura(

numeroDeFactura int,
estado varchar(50),
totalFactura decimal(10,2),
fechaFactura date,
clienteID int,
codigoEmpleado int,
PRIMARY KEY PK_numeroDeFactura (numeroDeFactura),
FOREIGN KEY (clienteID) REFERENCES Clientes(clienteID),
FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado)

);


CREATE TABLE DetalleFactura(

codigoDetalleFactura int,
precioUnitario decimal(10,2),
cantidad int,
numeroDeFactura int,
codigoProducto varchar(15),
PRIMARY KEY PK_codigoDetalleFactura (codigoDetalleFactura),
FOREIGN KEY (numeroDeFactura) REFERENCES Factura(numeroDeFactura),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto)

);


-- PROCEDIMIENTOS ALMACENADOS 
use DB_QuickShop;

delimiter $$
create procedure sp_AgregarCliente (in _idCliente int,in _nombre varchar (45), in _apellidos varchar(45), in _direccionClientes varchar(128), in _nit varchar(13), in _telefono varchar (13), in _correo varchar(128))
begin 
	insert into Clientes (Clientes.ClienteID, Clientes.nombreClientes, Clientes.apellidosClientes, Clientes.direccionClientes, Clientes.NIT, Clientes.telefonoClientes, Clientes.correoClientes)
		values (_idCliente,_nombre, _apellidos, _direccionClientes, _nit, _telefono,_correo);
end $$        
delimiter ;

call sp_AgregarCliente('1','Luis Rafa','Cordova','zona 21','1234567890123','12345678','luis@gmail.com');
call sp_AgregarCliente('2','Luis Rafa','Cordova','zona 21','1234567890123','12345678','luis@gmail.com');
call sp_AgregarCliente('3','Ramiro','Morales','zona 15','5869324785123','36578924','ramiro@gmail.com');



delimiter $$
create procedure sp_MostrarClientes ()
begin 
	select
    c.clienteID,
    c.nombreClientes,
    c.apellidosClientes,
    c.direccionClientes,
    c.NIT,
    c.telefonoClientes,
    c.correoClientes
    from clientes c;
end $$        
delimiter ;

call sp_MostrarClientes;

delimiter $$
create procedure sp_buscarClientes (in _clienteID int)
begin 
	select
    c.clienteID,
    c.nombreClientes,
    c.apellidosClientes,
    c.direccionClientes,
    c.NIT,
    c.telefonoClientes,
    c.correoClientes
    from clientes c
    where clienteID = _clienteID;
end $$        
delimiter ;

call sp_buscarClientes(1);

delimiter $$
create procedure sp_eliminarClientes (in _clienteID int)
begin 
	delete from clientes 
    where clienteID = _clienteID;
end $$        
delimiter ;

call sp_eliminarClientes(1);

call sp_MostrarClientes;

delimiter $$
create procedure sp_editarClientes (in _ID int(11), in _nom varchar (45), 
	in _ape varchar (45), in _dire varchar(128), in _nit varchar(13), in _num varchar(13), in _mail varchar (128))
begin 
	update Clientes
    set
    Clientes.clienteID = _ID,
    Clientes.nombreClientes = _nom,
    Clientes.apellidosClientes = _ape,
    Clientes.direccionClientes = _dire,
    Clientes.NIT = _nit,
    Clientes.telefonoClientes = _num,
    Clientes.correoClientes = _mail
    where
    Clientes.clienteID = _ID;
end $$        
delimiter ;

call sp_editarClientes('2','Jose','Figueroa','Amatitlan','1265289635741','98562471','jose@outlook.com');
call sp_MostrarClientes;

-- CRUD TIPO PRODUCTO
delimiter $$

create procedure sp_agregarTipoProducto (
    in _codigoTipoProducto int,
    in _descripcionProducto varchar(45)
)
begin
    insert into TipoProducto (codigoTipoProducto, descripcionProducto)
    values (_codigoTipoProducto, _descripcionProducto);
end $$

delimiter ;

call sp_agregarTipoProducto(1, 'Combustibles');
call sp_agregarTipoProducto(2, 'Alimentos');
call sp_agregarTipoProducto(3, 'Bebidas');
call sp_agregarTipoProducto(4, 'Snacks');
call sp_agregarTipoProducto(5, 'Cuidado Personal');

delimiter $$

create procedure sp_mostrarTipoProducto ()
begin
    select
        codigoTipoProducto,
        descripcionProducto
    from
        TipoProducto;
end $$

delimiter ;

call sp_mostrarTipoProducto;

delimiter $$

create procedure sp_buscarTipoProducto (in _codigoTipoProducto int)
begin
    select
        codigoTipoProducto,
        descripcionProducto
    from
        TipoProducto
    where
        codigoTipoProducto = _codigoTipoProducto;
end $$

delimiter ;

call sp_buscarTipoProducto(1);

delimiter $$

create procedure sp_eliminarTipoProducto (in _codigoTipoProducto int)
begin
    delete from TipoProducto
    where codigoTipoProducto = _codigoTipoProducto;
end $$

delimiter ;

call sp_eliminarTipoProducto(1);

delimiter $$

create procedure sp_editarTipoProducto (
    in _codigoTipoProducto int,
    in _descripcionProducto varchar(45)
)
begin
    update TipoProducto
    set descripcionProducto = _descripcionProducto
    where codigoTipoProducto = _codigoTipoProducto;
end $$

delimiter ;

 call sp_editarTipoProducto(1, 'Combustibles Premium');

-- CRUD CARGO EMPLEADO

delimiter $$

create procedure sp_agregarCargoEmpleado (
    in _codigoCargoEmpleado int,
    in _nombreCargo varchar(45),
    in _descripcionCargo varchar(82)
)
begin
    insert into cargoEmpleado (codigoCargoEmpleado, nombreCargo, descripcionCargo)
    values (_codigoCargoEmpleado, _nombreCargo, _descripcionCargo);
end $$

delimiter ;

call sp_agregarCargoEmpleado('1', 'Gerente', 'Responsable de la dirección y gestión del personal');
call sp_agregarCargoEmpleado('2', 'Asistente Administrativo', 'Apoyo en labores administrativas y de oficina');
call sp_agregarCargoEmpleado('3', 'Técnico de Soporte', 'Brinda asistencia técnica y soluciona problemas de hardware y software');
call sp_agregarCargoEmpleado('4', 'Analista de Marketing', 'Encargado de analizar datos y desarrollar estrategias de marketing');
call sp_agregarCargoEmpleado('5', 'Contador', 'Realiza tareas contables y financieras');

delimiter $$

create procedure sp_mostrarCargoEmpleado()
begin
    select
        ce.codigoCargoEmpleado,
        ce.nombreCargo,
        ce.descripcionCargo
    from
        cargoEmpleado ce;
end $$

delimiter ;

call sp_mostrarCargoEmpleado();

delimiter $$

create procedure sp_buscarCargoEmpleado (in _codigoCargoEmpleado int)
begin
    select
        ce.codigoCargoEmpleado,
        ce.nombreCargo,
        ce.descripcionCargo
    from
        cargoEmpleado ce
    where
        ce.codigoCargoEmpleado = _codigoCargoEmpleado;
end $$

delimiter ;

call sp_buscarCargoEmpleado(1);

delimiter $$

create procedure sp_eliminarCargoEmpleado (in _codigoCargoEmpleado int)
begin
    delete from cargoEmpleado
    where codigoCargoEmpleado = _codigoCargoEmpleado;
end $$

delimiter ;

call sp_eliminarCargoEmpleado(1);

delimiter $$

create procedure sp_editarCargoEmpleado (
    in _codigoCargoEmpleado int,
    in _nombreCargo varchar(45),
    in _descripcionCargo varchar(82)
)
begin
    update cargoEmpleado ce
    set
        ce.nombreCargo = _nombreCargo,
        ce.descripcionCargo = _descripcionCargo
    where
        ce.codigoCargoEmpleado = _codigoCargoEmpleado;
end $$

delimiter ;

call sp_editarCargoEmpleado('3','Limpieza y Mantenimiento','Encargado de mantener en orden su área específicada');

call sp_mostrarCargoEmpleado();

-- CRUD PROVEEDORES
delimiter $$

create procedure sp_agregarproveedor (
    in _codigoproveedor int,
    in _nitproveedor varchar(13),
    in _nombresproveedor varchar(60),
    in _apellidosproveedor varchar(60),
    in _direccionproveedor varchar(150),
    in _razonsocial varchar(60),
    in _contactoprincipal varchar(100),
    in _paginaweb varchar(50),
    in _telefonoproveedor varchar(13),
    in _emailproveedor varchar(50)
)
begin
    insert into Proveedores (codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb, telefonoProveedor, emailProveedor)
    values (_codigoproveedor, _nitproveedor, _nombresproveedor, _apellidosproveedor, _direccionproveedor, _razonsocial, _contactoprincipal, _paginaweb, _telefonoproveedor, _emailproveedor);
end $$

delimiter ;

call sp_agregarproveedor(1, '0614000000011', 'Gasolinera Express', 'S.A.', 'Av. Principal 123, Zona 1', 'Gasolinera Express S.A.', 'Juan Pérez', 'www.gasolineraexpress.com', '1234567890', 'info@gasolineraexpress.com');
call sp_agregarproveedor(2, '0614000000024', 'Distribuidora de Alimentos', 'Dialiment S.A.', 'Av. Comercial 456, Zona 2', 'Dialiment S.A.', 'María Gómez', 'www.dialiment.com', '2345678901', 'info@dialiment.com');
call sp_agregarproveedor(3, '0614000000037', 'Bebidas Refrescantes', 'Refrescos del Sur S.A.', 'Calle Refrescante 789, Zona 3', 'Refrescos del Sur S.A.', 'Pedro Martínez', 'www.refrescosdelsur.com', '3456789012', 'info@refrescosdelsur.com');
call sp_agregarproveedor(4, '0614000000040', 'Lubricantes y Aceites', 'Lubriaceites Ltda.', 'Carrera Lubricante 101, Zona 4', 'Lubriaceites Ltda.', 'Luis Rodríguez', 'www.lubriaceites.com', '4567890123', 'info@lubriaceites.com');
call sp_agregarproveedor(5, '0614000000053', 'Productos de Limpieza', 'Limpiafacil S.A.', 'Pasaje Limpio 202, Zona 5', 'Limpiafacil S.A.', 'Ana López', 'www.limpiafacil.com', '5678901234', 'info@limpiafacil.com');

delimiter $$

create procedure sp_mostrarproveedor ()
begin
    select
        codigoproveedor,
        nitproveedor,
        nombresproveedor,
        apellidosproveedor,
        direccionproveedor,
        razonsocial,
        contactoprincipal,
        paginaweb,
        telefonoproveedor,
        emailproveedor
    from
        Proveedores;
end $$

delimiter ;

call sp_mostrarproveedor;


delimiter $$

create procedure sp_buscarproveedor (in _codigoproveedor int)
begin
    select
        codigoproveedor,
        nitproveedor,
        nombresproveedor,
        apellidosproveedor,
        direccionproveedor,
        razonsocial,
        contactoprincipal,
        paginaweb,
        telefonoproveedor,
        emailproveedor
    from
        Proveedores
    where
        codigoproveedor = _codigoproveedor;
end $$

delimiter ;

call sp_buscarproveedor(2);

delimiter $$

create procedure sp_eliminarproveedor (in _codigoproveedor int)
begin
    delete from Proveedores
    where codigoproveedor = _codigoproveedor;
end $$

delimiter ;

call sp_eliminarproveedor(1);

delimiter $$

create procedure sp_editarproveedor (
    in _codigoproveedor int,
    in _nitproveedor varchar(13),
    in _nombresproveedor varchar(60),
    in _apellidosproveedor varchar(60),
    in _direccionproveedor varchar(150),
    in _razonsocial varchar(60),
    in _contactoprincipal varchar(100),
    in _paginaweb varchar(50),
    in _telefonoproveedor varchar(13),
    in _emailproveedor varchar(50)
)
begin
    update Proveedores
    set
        NITProveedor = _nitproveedor,
        nombresProveedor = _nombresproveedor,
        apellidosProveedor = _apellidosproveedor,
        direccionProveedor = _direccionproveedor,
        razonSocial = _razonsocial,
        contactoPrincipal = _contactoprincipal,
        paginaWeb = _paginaweb,
        telefonoProveedor = _telefonoproveedor,
        emailProveedor = _emailproveedor
    where
        codigoProveedor = _codigoproveedor;
end $$

delimiter ;

 call sp_editarproveedor(1, '1234567890123', 'Juan', 'Perez', 'Calle 123', 'Razón Social', 'Contacto', 'www.proveedor.com', '1234567890', 'proveedor@correo.com');


-- CRUD COMPRA
delimiter $$

create procedure sp_agregarcompra (
    in _numeroDocumento int,
    in _fechaDocumento date,
    in _descripcionCompra varchar(60),
    in _totalDocumento decimal(20,2)
)
begin
    insert into Compras (numeroDocumento, fechaDocumento, descripcionCompra, totalDocumento)
    values (_numeroDocumento, _fechaDocumento, _descripcionCompra, _totalDocumento);
end $$

delimiter ;

call sp_agregarcompra(1, '2024-05-01', 'Producto 1', 100.00);
call sp_agregarcompra(2, '2024-05-02', 'Producto 2', 150.00);
call sp_agregarcompra(3, '2024-05-03', 'Producto 3', 200.00);
call sp_agregarcompra(4, '2024-05-04', 'Producto 4', 250.00);
call sp_agregarcompra(5, '2024-05-05', 'Producto 5', 300.00);

delimiter $$

create procedure sp_mostrarcompras()
begin
    select
        cd.numeroDocumento,
        cd.fechaDocumento,
        cd.descripcionCompra,
        cd.totalDocumento
    from
        Compras cd;
end $$

delimiter ;

call sp_mostrarcompras();

delimiter $$

create procedure sp_buscarcompra (in _numeroDocumento int)
begin
    select
        cd.numeroDocumento as NumeroDocumento,
        cd.fechaDocumento as FechaDocumento,
        cd.descripcionCompra as DescripcionCompra,
        cd.totalDocumento as TotalDocumento
    from
        Compras cd
    where
        cd.numeroDocumento = _numeroDocumento;
end $$

delimiter ;

call sp_buscarcompra(1);


delimiter $$

create procedure sp_eliminarcompra (in _numeroDocumento int)
begin
    delete from Compras 
    where numeroDocumento = _numeroDocumento;
end $$

delimiter ;

call sp_eliminarcompra(1);

delimiter $$

create procedure sp_editarcompra (
    in _numeroDocumento int,
    in _fechaDocumento date,
    in _descripcionCompra varchar(60),
    in _totalDocumento decimal(20,2)
)
begin
    update Compras
    set
        fechaDocumento = _fechaDocumento,
        descripcionCompra = _descripcionCompra,
        totalDocumento = _totalDocumento
    where
        numeroDocumento = _numeroDocumento;
end $$

delimiter ;


call sp_editarcompra(1, '2024-05-01', 'Producto modificado', 150.00);

-- CRUD PRODUCTOS
DELIMITER $$

CREATE PROCEDURE sp_agregarProducto(
    IN p_codigoProducto VARCHAR(15),
    IN p_descripcionProducto VARCHAR(15),
    IN p_precioUnitario DECIMAL(10,2),
    IN p_precioDocena DECIMAL(10,2),
    IN p_precioMayor DECIMAL(10,2),
    IN p_imagenProducto VARCHAR(45),
    IN p_existencia INT,
    IN p_codigoTipoProducto INT,
    IN p_codigoProveedor INT
)
BEGIN
    INSERT INTO Productos(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, imagenProducto, existencia, codigoTipoProducto, codigoProveedor)
    VALUES(p_codigoProducto, p_descripcionProducto, p_precioUnitario, p_precioDocena, p_precioMayor, p_imagenProducto, p_existencia, p_codigoTipoProducto, p_codigoProveedor);
END$$
DELIMITER ;

CALL sp_agregarProducto('P001', 'Arroz', 5.99, 68.99, 129.99, 'arroz.jpg', 100, 2, 2);
CALL sp_agregarProducto('P002', 'Frijoles', 3.49, 39.99, 74.99, 'frijoles.jpg', 150, 2, 2);
CALL sp_agregarProducto('P003', 'Aceite', 8.99, 102.99, 194.99, 'aceite.jpg', 80, 3, 2);
CALL sp_agregarProducto('P004', 'Leche Entera', 2.99, 32.99, 62.99, 'leche.jpg', 120, 3, 4);
CALL sp_agregarProducto('P005', 'Azúcar', 4.49, 51.99, 98.99, 'azucar.jpg', 90, 4, 5);

Delimiter $$
create procedure sp_mostrarProductos()
	begin
    select
		p.codigoProducto,
        p.descripcionProducto,
        p.precioUnitario,
        p.precioDocena,
        p.precioMayor,
        p.imagenProducto,
        p.existencia,
        p.codigoTipoProducto,
        p.codigoProveedor
        from
        productos p;
	end$$
Delimiter ;

call sp_mostrarProductos();

delimiter $$
create procedure sp_buscarProducto(in codP varchar(15))
begin
	select
    p.codigoProducto,
        p.descripcionProducto,
        p.precioUnitario,
        p.precioDocena,
        p.precioMayor,
        p.imagenProducto,
        p.existencia,
        p.codigoTipoProducto,
        p.codigoProveedor
        from
        productos p
        where 
        codigoProducto = codP;
end$$
delimiter ;

call sp_buscarProducto(1);

call sp_mostrarProductos();

DELIMITER $$
CREATE PROCEDURE sp_editarProducto(
    IN p_codigoProducto VARCHAR(15),
    IN p_nuevaDescripcionProducto VARCHAR(15),
    IN p_nuevoPrecioUnitario DECIMAL(10,2),
    IN p_nuevoPrecioDocena DECIMAL(10,2),
    IN p_nuevoPrecioMayor DECIMAL(10,2),
    IN p_nuevaImagenProducto VARCHAR(45),
    IN p_nuevaExistencia INT,
    IN p_nuevoCodigoTipoProducto INT,
    IN p_nuevoCodigoProveedor INT
)
BEGIN
    UPDATE Productos
    SET descripcionProducto = p_nuevaDescripcionProducto,
        precioUnitario = p_nuevoPrecioUnitario,
        precioDocena = p_nuevoPrecioDocena,
        precioMayor = p_nuevoPrecioMayor,
        imagenProducto = p_nuevaImagenProducto,
        existencia = p_nuevaExistencia,
        codigoTipoProducto = p_nuevoCodigoTipoProducto, 
        codigoProveedor = p_nuevoCodigoProveedor
    WHERE codigoProducto = p_codigoProducto;
END$$
DELIMITER ;

call sp_editarProducto('P001', 'Pollo', 8.99, 69.99, 130.99, 'pollo.jpg', 100, 2, 2);

Delimiter $$
CREATE PROCEDURE sp_eliminarProducto(IN _codigoProducto VARCHAR(15))
BEGIN
    DELETE FROM Productos
    WHERE codigoProducto = _codigoProducto;
END$$

DELIMITER ;

call sp_eliminarProducto('P002');
-- CRUD DETALLE COMPRA
DELIMITER $$

CREATE PROCEDURE sp_crearDetalleCompra(
    IN p_codigoDetalleCompra INT,
    IN p_costoUnitario DECIMAL(10,2),
    IN p_cantidad INT,
    IN p_codigoProducto VARCHAR(15),
    IN p_numeroDocumento INT
)
BEGIN
    INSERT INTO DetalleCompra(codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    VALUES(p_codigoDetalleCompra, p_costoUnitario, p_cantidad, p_codigoProducto, p_numeroDocumento);
END$$
DELIMITER ;

CALL sp_crearDetalleCompra(2, 50.00, 10, 'P005', 2);
CALL sp_crearDetalleCompra(3, 50.00, 10, 'P005', 3);

DELIMITER $$
CREATE PROCEDURE sp_mostrarDetallesCompra()
BEGIN
    SELECT * FROM DetalleCompra;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleCompra(
    IN codDetComp VARCHAR(50)  -- Cambiado a VARCHAR
)
BEGIN
    SELECT * FROM DetalleCompra
    WHERE codigoProducto = codDetComp;
END$$
DELIMITER ;

call sp_buscarDetalleCompra('P001');

CALL sp_mostrarDetallesCompra();

DELIMITER $$
CREATE PROCEDURE sp_editarDetalleCompra(
    IN p_codigoDetalleCompra INT,
    IN p_nuevoCostoUnitario DECIMAL(10,2),
    IN p_nuevaCantidad INT,
    IN p_nuevoCodigoProducto VARCHAR(15),
    IN p_nuevoNumeroDocumento INT
)
BEGIN
    UPDATE DetalleCompra
    SET costoUnitario = p_nuevoCostoUnitario,
        cantidad = p_nuevaCantidad,
        codigoProducto = p_nuevoCodigoProducto,
        numeroDocumento = p_nuevoNumeroDocumento
    WHERE codigoDetalleCompra = p_codigoDetalleCompra;
END$$
DELIMITER ;

CALL sp_editarDetalleCompra(2, 55.00, 12, 'P001', 2);

call sp_mostrarproductos();
DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleCompra(
    IN p_codigoDetalleCompra INT
)
BEGIN
    DELETE FROM DetalleCompra
    WHERE codigoDetalleCompra = p_codigoDetalleCompra;
END$$

DELIMITER ;

CALL sp_eliminarDetalleCompra(2);

DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleCompraPorProducto(
    IN p_codigoProducto VARCHAR(10)
)
BEGIN
    DELETE FROM DetalleCompra
    WHERE codigoProducto = p_codigoProducto;
END$$
DELIMITER ;



-- CRUD de Empleados

DELIMITER $$

CREATE PROCEDURE sp_crearEmpleado(
    IN p_codigoEmpleado INT,
    IN p_nombresEmpleado VARCHAR(50),
    IN p_apellidosEmpleado VARCHAR(50),
    IN p_sueldo DECIMAL(10,2),
    IN p_direccion VARCHAR(150),
    IN p_turno VARCHAR(15),
    IN p_codigoCargoEmpleado INT
)
BEGIN
    INSERT INTO Empleados(codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado)
    VALUES(p_codigoEmpleado, p_nombresEmpleado, p_apellidosEmpleado, p_sueldo, p_direccion, p_turno, p_codigoCargoEmpleado);
END$$
DELIMITER ;
CALL sp_crearEmpleado(2, 'Juan', 'Perez', 1500.00, 'Calle Falsa 123', 'Diurno', 2);
CALL sp_crearEmpleado(3, 'Kevin', 'Mendez', 1500.00, 'Calle Real 321', 'Nocturno', 3);

DELIMITER $$

CREATE PROCEDURE sp_mostrarEmpleados()
BEGIN
    SELECT * FROM Empleados;
END$$
DELIMITER ;

CALL sp_mostrarEmpleados();

delimiter $$
create procedure sp_buscarEmpleado(in idEmpleado int)
begin
	select 
    e.codigoEmpleado,
    e.nombresEmpleado,
    e.apellidosEmpleado,
    e.sueldo,
    e.direccion,
    e.turno,
    e.codigoCargoEmpleado
    from empleados e
    where codigoEmpleado = idEmpleado;
end $$
delimiter ;

call sp_buscarEmpleado(2);

DELIMITER $$

CREATE PROCEDURE sp_editarEmpleado(
    IN p_codigoEmpleado INT,
    IN p_nuevosNombresEmpleado VARCHAR(50),
    IN p_nuevosApellidosEmpleado VARCHAR(50),
    IN p_nuevoSueldo DECIMAL(10,2),
    IN p_nuevaDireccion VARCHAR(150),
    IN p_nuevoTurno VARCHAR(15),
    IN p_nuevoCodigoCargoEmpleado INT
)
BEGIN
    UPDATE Empleados
    SET nombresEmpleado = p_nuevosNombresEmpleado,
        apellidosEmpleado = p_nuevosApellidosEmpleado,
        sueldo = p_nuevoSueldo,
        direccion = p_nuevaDireccion,
        turno = p_nuevoTurno,
        codigoCargoEmpleado = p_nuevoCodigoCargoEmpleado
    WHERE codigoEmpleado = p_codigoEmpleado;
END$$
DELIMITER ;

CALL sp_editarEmpleado(2, 'Juan', 'Perez Gomez', 1600.00, 'Avenida Siempreviva 742', 'Nocturno', 2);

DELIMITER $$

CREATE PROCEDURE sp_eliminarEmpleado(
    IN p_codigoEmpleado INT
)
BEGIN
    DELETE FROM Empleados
    WHERE codigoEmpleado = p_codigoEmpleado;
END$$

DELIMITER ;

CALL sp_eliminarEmpleado(1);


-- CRUD de Factura
DELIMITER $$

CREATE PROCEDURE sp_crearFactura(
    IN p_numeroDeFactura INT,
    IN p_estado VARCHAR(50),
    IN p_totalFactura DECIMAL(10,2),
    IN p_fechaFactura VARCHAR(45),
    IN p_codigoCliente INT,
    IN p_codigoEmpleado INT
)
BEGIN
    INSERT INTO Factura(numeroDeFactura, estado, totalFactura, fechaFactura, clienteID, codigoEmpleado)
    VALUES(p_numeroDeFactura, p_estado, p_totalFactura, p_fechaFactura, p_codigoCliente, p_codigoEmpleado);
END$$
DELIMITER ;

CALL sp_crearFactura(2, 'Pagada', 0.00, '2024-05-12', 2, 2);
CALL sp_crearFactura(3, 'Pendiente', 0.00, '2024-05-12', 3, 3);


DELIMITER $$

CREATE PROCEDURE sp_mostrarFacturas()
BEGIN
    SELECT * FROM Factura;
END$$
DELIMITER ;

CALL sp_mostrarFacturas();

delimiter $$
create procedure sp_buscarFactura(in facturaID int)
begin
	select * from factura
    where numeroDeFactura = facturaID;
end$$
delimiter ;

call sp_buscarFactura(2);

DELIMITER $$

CREATE PROCEDURE sp_editarFactura(
    IN p_numeroDeFactura INT,
    IN p_nuevoEstado VARCHAR(50),
    IN p_nuevoTotalFactura DECIMAL(10,2),
    IN p_nuevaFechaFactura VARCHAR(45),
    IN p_nuevoCodigoCliente INT,
    IN p_nuevoCodigoEmpleado INT
)
BEGIN
    UPDATE Factura
    SET estado = p_nuevoEstado,
        totalFactura = p_nuevoTotalFactura,
        fechaFactura = p_nuevaFechaFactura,
        clienteID = p_nuevoCodigoCliente,
        codigoEmpleado = p_nuevoCodigoEmpleado
    WHERE numeroDeFactura = p_numeroDeFactura;
END$$
DELIMITER ;



CALL sp_editarFactura(2, 'Pendiente', 0.00, '2024-05-13', 2, 2);


DELIMITER $$

CREATE PROCEDURE sp_eliminarFactura(
    IN p_numeroDeFactura INT
)
BEGIN
    DELETE FROM Factura
    WHERE numeroDeFactura = p_numeroDeFactura;
END$$

DELIMITER ;
CALL sp_eliminarFactura(1);

-- CRUD de DetalleFactura 
DELIMITER $$

CREATE PROCEDURE sp_crearDetalleFactura(
    IN p_codigoDetalleFactura INT,
    IN p_precioUnitario DECIMAL(10,2),
    IN p_cantidad INT,
    IN p_numeroDeFactura INT,
    IN p_codigoProducto VARCHAR(15)
)
BEGIN
    INSERT INTO DetalleFactura(codigoDetalleFactura, precioUnitario, cantidad, numeroDeFactura, codigoProducto)
    VALUES(p_codigoDetalleFactura, p_precioUnitario, p_cantidad, p_numeroDeFactura, p_codigoProducto);
END$$
DELIMITER ;

CALL sp_crearDetalleFactura(1, 50.00, 10, 2, 'P001');
CALL sp_crearDetalleFactura(3, 50.00, 10, 3, 'P003');

DELIMITER $$


CREATE PROCEDURE sp_mostrarDetallesFactura()
BEGIN
    SELECT * FROM DetalleFactura;
END$$
DELIMITER ;


CALL sp_mostrarDetallesFactura();


delimiter $$
create procedure sp_buscarDetalleFactura(in codDetFac int)
begin
	select * from DetalleFactura
    where 
		codigoDetalleFactura = codDetFac;
end $$
delimiter ;

call sp_buscarDetalleFactura(3);

DELIMITER $$

CREATE PROCEDURE sp_editarDetalleFactura(
    IN p_codigoDetalleFactura INT,
    IN p_nuevoPrecioUnitario DECIMAL(10,2),
    IN p_nuevaCantidad INT,
    IN p_nuevoNumeroDeFactura INT,
    IN p_nuevoCodigoProducto VARCHAR(15)
)
BEGIN
    UPDATE DetalleFactura
    SET precioUnitario = p_nuevoPrecioUnitario,
        cantidad = p_nuevaCantidad,
        numeroDeFactura = p_nuevoNumeroDeFactura,
        codigoProducto = p_nuevoCodigoProducto
    WHERE codigoDetalleFactura = p_codigoDetalleFactura;
END$$

DELIMITER ;
CALL sp_editarDetalleFactura(1, 55.00, 3, 3, 'P001');

DELIMITER $$

CREATE PROCEDURE sp_eliminarDetalleFactura(
    IN p_codigoDetalleFactura INT
)
BEGIN
    DELETE FROM DetalleFactura
    WHERE codigoDetalleFactura = p_codigoDetalleFactura;
END$$

DELIMITER ;
CALL sp_eliminarDetalleFactura(1);

-- Eliminar registros en DetalleFactura relacionados con una Factura específica
DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleFacturaPorFactura(p_numeroDeFactura INT)
BEGIN
    DELETE FROM DetalleFactura WHERE numeroDeFactura = p_numeroDeFactura;
END$$
DELIMITER ;

-- Eliminar registros en Factura relacionados con un Empleado específico
DELIMITER $$
CREATE PROCEDURE sp_eliminarFacturaPorEmpleado(p_codigoEmpleado INT)
BEGIN
    DELETE FROM Factura WHERE codigoEmpleado = p_codigoEmpleado;
END$$
DELIMITER ;



-- -----------------------
call sp_mostrarFacturas();
call sp_mostrarDetallesFactura();
call sp_mostrarDetallesCompra();
call sp_mostrarEmpleados();
call sp_mostrarProductos();

-- Trigger para actualización del total en la tabla Facturas al insertar un DetalleFactura
DELIMITER $$
CREATE TRIGGER ActualizarTotalFacturaInsert AFTER INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
    UPDATE Factura
    SET totalFactura = totalFactura + NEW.precioUnitario * NEW.cantidad
    WHERE numeroDeFactura = NEW.numeroDeFactura;
END
$$
DELIMITER ;

-- Trigger para actualización del total en la tabla Facturas al actualizar un DetalleFactura
DELIMITER $$
CREATE TRIGGER ActualizarTotalFacturaUpdate AFTER UPDATE ON DetalleFactura
FOR EACH ROW
BEGIN
    UPDATE Factura
    SET totalFactura = totalFactura + (NEW.precioUnitario * NEW.cantidad) - (OLD.precioUnitario * OLD.cantidad)
    WHERE numeroDeFactura = NEW.numeroDeFactura;
END $$
DELIMITER ;

-- Trigger para actualización del total en la tabla Facturas al eliminar un DetalleFactura
DELIMITER $$
CREATE TRIGGER ActualizarTotalFacturaDelete AFTER DELETE ON DetalleFactura
FOR EACH ROW
BEGIN
    UPDATE Factura
    SET totalFactura = totalFactura - OLD.precioUnitario * OLD.cantidad
    WHERE numeroDeFactura = OLD.numeroDeFactura;
END $$
DELIMITER ;

-- Procedimiento para actualizar el stock de un producto al insertar un DetalleFactura
DELIMITER $$
CREATE PROCEDURE ActualizarStockInsertarDetalle(
    IN p_codigoProducto varchar(15),
    IN p_cantidad INT
)
BEGIN
    UPDATE Productos
    SET existencia = existencia - p_cantidad
    WHERE codigoProducto = p_codigoProducto;
END$$
DELIMITER ;

-- Procedimiento para actualizar el stock de un producto al eliminar un DetalleFactura
DELIMITER $$
CREATE PROCEDURE ActualizarStockEliminarDetalle(
    IN p_productoID varchar(15),
    IN p_cantidad INT
)
BEGIN
    UPDATE Productos
    SET existencia = existencia + p_cantidad
    WHERE codigoProducto = p_productoID;
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER ActualizarStockInsert AFTER INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
    CALL ActualizarStockInsertarDetalle(NEW.codigoProducto, NEW.cantidad);
END;
$$
DELIMITER ;

-- Trigger para actualizar el stock al eliminar un DetalleFactura
DELIMITER $$
CREATE TRIGGER ActualizarStockDelete AFTER DELETE ON DetalleFactura
FOR EACH ROW
BEGIN
    CALL ActualizarStockEliminarDetalle(OLD.codigoProducto, OLD.cantidad);
END;
$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER AfterInsertDetalleCompra
AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
    DECLARE precioProveedor DECIMAL(10,2);
    DECLARE precioDocena DECIMAL(10,2);
    DECLARE precioMayor DECIMAL(10,2);

    -- Calcular precios
    SET precioProveedor = NEW.costoUnitario * 1.40;
    SET precioDocena = precioProveedor * 1.35;
    SET precioMayor = precioProveedor * 1.25;

    -- Actualizar productos con los precios calculados
    UPDATE Productos
    SET precioUnitario = precioProveedor,
        precioDocena = precioDocena,
        precioMayor = precioMayor
    WHERE codigoProducto = NEW.codigoProducto;
END $$

DELIMITER ;



set global time_zone= '-6:00';

