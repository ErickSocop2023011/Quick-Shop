DROP DATABASE IF EXISTS DB_QuickShop;

CREATE DATABASE DB_QuickShop;

USE DB_QuickShop;

CREATE TABLE Clientes(
    clienteID INT,
    nombreClientes VARCHAR(45),
    apellidosClientes VARCHAR(45),
    direccionClientes VARCHAR(128),
    NIT VARCHAR(13),
    telefonoClientes VARCHAR(13),
    correoClientes VARCHAR(128),
    PRIMARY KEY PK_ClienteID(clienteID)
);

CREATE TABLE TipoProducto(
    codigoTipoProducto INT,
    descripcionProducto VARCHAR(45),
    PRIMARY KEY PK_TipoProducto(codigoTipoProducto)
);

CREATE TABLE Compras(
    numeroDocumento INT,
    fechaDocumento DATE,
    descripcionCompra VARCHAR(60),
    totalDocumento DECIMAL(20,2),
    PRIMARY KEY PK_NumeroDocumento(numeroDocumento)
);

CREATE TABLE Proveedores(
    codigoProveedor INT,
    NITProveedor VARCHAR(13),
    nombresProveedor VARCHAR(60),
    apellidosProveedor VARCHAR(60),
    direccionProveedor VARCHAR(150),
    razonSocial VARCHAR(60),
    contactoPrincipal VARCHAR(100),
    paginaWeb VARCHAR(50),
    telefonoProveedor VARCHAR(13),
    emailProveedor VARCHAR(50),
    PRIMARY KEY PK_codigoProveedor(codigoProveedor)
);

CREATE TABLE CargoEmpleado (
    codigoCargoEmpleado INT,
    nombreCargo VARCHAR(45),
    descripcionCargo VARCHAR(82),
    PRIMARY KEY PK_codigoCargoEmpleado (codigoCargoEmpleado)
);

CREATE TABLE Productos(
    codigoProducto INT,
    descripcionProducto VARCHAR(15),
    precioUnitario DECIMAL(10,2),
    precioDocena DECIMAL(10,2),
    precioMayor DECIMAL(10,2),
    imagenProducto VARCHAR(45),
    existencia INT,
    codigoTipoProducto INT,
    codigoProveedor INT,
    PRIMARY KEY PK_codigoProducto (codigoProducto),
    FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto) on delete cascade,
    FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)on delete cascade

);

CREATE TABLE DetalleCompra(
    codigoDetalleCompra INT,
    costoUnitario DECIMAL(10,2),
    cantidad INT,
    codigoProducto INT,
    numeroDocumento INT,
    PRIMARY KEY PK_codigoDetalleCompra (codigoDetalleCompra),
    FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto)on delete cascade,
    FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento) on delete cascade

);

CREATE TABLE Empleados(
    codigoEmpleado INT,
    nombresEmpleado VARCHAR(50),
    apellidosEmpleado VARCHAR(50),
    sueldo DECIMAL(10,2),
    direccion VARCHAR(150),
    turno VARCHAR(15),
    codigoCargoEmpleado INT,
    PRIMARY KEY PK_codigoEmpleado (codigoEmpleado),
    FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoEmpleado(codigoCargoEmpleado) on delete cascade
);

CREATE TABLE Factura(
    numeroDeFactura INT,
    estado VARCHAR(50),
    totalFactura DECIMAL(10,2),
    fechaFactura DATE,
    clienteID INT,
    codigoEmpleado INT,
    PRIMARY KEY PK_numeroDeFactura (numeroDeFactura),
    FOREIGN KEY (clienteID) REFERENCES Clientes(clienteID) on delete cascade,
    FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado) on delete cascade
);

CREATE TABLE DetalleFactura(
    codigoDetalleFactura INT,
    precioUnitario DECIMAL(10,2),
    cantidad INT,
    numeroDeFactura INT,
    codigoProducto INT,
    PRIMARY KEY PK_codigoDetalleFactura (codigoDetalleFactura),
    FOREIGN KEY (numeroDeFactura) REFERENCES Factura(numeroDeFactura) on delete cascade,
    FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) on delete cascade
);

-- PROCEDIMIENTOS ALMACENADOS
USE DB_QuickShop;

DELIMITER $$
CREATE PROCEDURE sp_AgregarCliente (IN _idCliente INT, IN _nombre VARCHAR(45), IN _apellidos VARCHAR(45), IN _direccionClientes VARCHAR(128), IN _nit VARCHAR(13), IN _telefono VARCHAR(13), IN _correo VARCHAR(128))
BEGIN 
    INSERT INTO Clientes (Clientes.ClienteID, Clientes.nombreClientes, Clientes.apellidosClientes, Clientes.direccionClientes, Clientes.NIT, Clientes.telefonoClientes, Clientes.correoClientes)
        VALUES (_idCliente, _nombre, _apellidos, _direccionClientes, _nit, _telefono, _correo);
END $$        
DELIMITER ;

CALL sp_AgregarCliente(1, 'Luis Rafa', 'Cordova', 'zona 21', '1234567890123', '12345678', 'luis@gmail.com');
CALL sp_AgregarCliente(2, 'Luis Rafa', 'Cordova', 'zona 21', '1234567890123', '12345678', 'luis@gmail.com');
CALL sp_AgregarCliente(3, 'Ramiro', 'Morales', 'zona 15', '5869324785123', '36578924', 'ramiro@gmail.com');

DELIMITER $$
CREATE PROCEDURE sp_MostrarClientes ()
BEGIN 
    SELECT
        c.clienteID,
        c.nombreClientes,
        c.apellidosClientes,
        c.direccionClientes,
        c.NIT,
        c.telefonoClientes,
        c.correoClientes
    FROM Clientes c;
END $$        
DELIMITER ;

CALL sp_MostrarClientes;

DELIMITER $$
CREATE PROCEDURE sp_buscarClientes (IN _clienteID INT)
BEGIN 
    SELECT
        c.clienteID,
        c.nombreClientes,
        c.apellidosClientes,
        c.direccionClientes,
        c.NIT,
        c.telefonoClientes,
        c.correoClientes
    FROM Clientes c
    WHERE clienteID = _clienteID;
END $$        
DELIMITER ;

CALL sp_buscarClientes(1);

DELIMITER $$
CREATE PROCEDURE sp_eliminarClientes (IN _clienteID INT)
BEGIN 
    DELETE FROM Clientes 
    WHERE clienteID = _clienteID;
END $$        
DELIMITER ;

CALL sp_eliminarClientes(1);

CALL sp_MostrarClientes;

DELIMITER $$
CREATE PROCEDURE sp_editarClientes (IN _ID INT(11), IN _nom VARCHAR(45), IN _ape VARCHAR(45), IN _dire VARCHAR(128), IN _nit VARCHAR(13), IN _num VARCHAR(13), IN _mail VARCHAR(128))
BEGIN 
    UPDATE Clientes
    SET
        Clientes.clienteID = _ID,
        Clientes.nombreClientes = _nom,
        Clientes.apellidosClientes = _ape,
        Clientes.direccionClientes = _dire,
        Clientes.NIT = _nit,
        Clientes.telefonoClientes = _num,
        Clientes.correoClientes = _mail
    WHERE
        Clientes.clienteID = _ID;
END $$        
DELIMITER ;

CALL sp_editarClientes(2, 'Jose', 'Figueroa', 'Amatitlan', '1265289635741', '98562471', 'jose@outlook.com');
CALL sp_MostrarClientes;

-- CRUD TIPO PRODUCTO
DELIMITER $$

CREATE PROCEDURE sp_agregarTipoProducto (
    IN _codigoTipoProducto INT,
    IN _descripcionProducto VARCHAR(45)
)
BEGIN
    INSERT INTO TipoProducto (codigoTipoProducto, descripcionProducto)
    VALUES (_codigoTipoProducto, _descripcionProducto);
END $$

DELIMITER ;

CALL sp_agregarTipoProducto(1, 'Combustibles');
CALL sp_agregarTipoProducto(2, 'Alimentos');
CALL sp_agregarTipoProducto(3, 'Bebidas');
CALL sp_agregarTipoProducto(4, 'Snacks');
CALL sp_agregarTipoProducto(5, 'Cuidado Personal');

DELIMITER $$

CREATE PROCEDURE sp_mostrarTipoProducto ()
BEGIN
    SELECT
        codigoTipoProducto,
        descripcionProducto
    FROM
        TipoProducto;
END $$

DELIMITER ;

CALL sp_mostrarTipoProducto;

DELIMITER $$

CREATE PROCEDURE sp_buscarTipoProducto (IN _codigoTipoProducto INT)
BEGIN
    SELECT
        codigoTipoProducto,
        descripcionProducto
    FROM
        TipoProducto
    WHERE
        codigoTipoProducto = _codigoTipoProducto;
END $$

DELIMITER ;

CALL sp_buscarTipoProducto(1);

DELIMITER $$

CREATE PROCEDURE sp_eliminarTipoProducto (IN _codigoTipoProducto INT)
BEGIN
    DELETE FROM TipoProducto
    WHERE codigoTipoProducto = _codigoTipoProducto;
END $$

DELIMITER ;

CALL sp_eliminarTipoProducto(1);

DELIMITER $$

CREATE PROCEDURE sp_editarTipoProducto (
    IN _codigoTipoProducto INT,
    IN _descripcionProducto VARCHAR(45)
)
BEGIN
    UPDATE TipoProducto
    SET descripcionProducto = _descripcionProducto
    WHERE codigoTipoProducto = _codigoTipoProducto;
END $$

DELIMITER ;

CALL sp_editarTipoProducto(1, 'Combustibles Premium');

-- CRUD CARGO EMPLEADO

DELIMITER $$

CREATE PROCEDURE sp_agregarCargoEmpleado (
    IN _codigoCargoEmpleado INT,
    IN _nombreCargo VARCHAR(45),
    IN _descripcionCargo VARCHAR(82)
)
BEGIN
    INSERT INTO CargoEmpleado (codigoCargoEmpleado, nombreCargo, descripcionCargo)
    VALUES (_codigoCargoEmpleado, _nombreCargo, _descripcionCargo);
END $$

DELIMITER ;

CALL sp_agregarCargoEmpleado(1, 'Gerente', 'Responsable de la dirección y gestión del personal');
CALL sp_agregarCargoEmpleado(2, 'Asistente Administrativo', 'Apoyo en labores administrativas y de oficina');
CALL sp_agregarCargoEmpleado(3, 'Técnico de Soporte', 'Brinda asistencia técnica y soluciona problemas de hardware y software');
CALL sp_agregarCargoEmpleado(4, 'Analista de Marketing', 'Encargado de analizar datos y desarrollar estrategias de marketing');
CALL sp_agregarCargoEmpleado(5, 'Contador', 'Realiza tareas contables y financieras');

DELIMITER $$

CREATE PROCEDURE sp_mostrarCargoEmpleado ()
BEGIN
    SELECT
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    FROM
        CargoEmpleado;
END $$

DELIMITER ;

CALL sp_mostrarCargoEmpleado;

DELIMITER $$

CREATE PROCEDURE sp_buscarCargoEmpleado (IN _codigoCargoEmpleado INT)
BEGIN
    SELECT
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    FROM
        CargoEmpleado
    WHERE
        codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

DELIMITER ;

CALL sp_buscarCargoEmpleado(1);

DELIMITER $$

CREATE PROCEDURE sp_eliminarCargoEmpleado (IN _codigoCargoEmpleado INT)
BEGIN
    DELETE FROM CargoEmpleado
    WHERE codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

DELIMITER ;

CALL sp_eliminarCargoEmpleado(1);

DELIMITER $$

CREATE PROCEDURE sp_editarCargoEmpleado (
    IN _codigoCargoEmpleado INT,
    IN _nombreCargo VARCHAR(45),
    IN _descripcionCargo VARCHAR(82)
)
BEGIN
    UPDATE CargoEmpleado
    SET nombreCargo = _nombreCargo,
        descripcionCargo = _descripcionCargo
    WHERE codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

DELIMITER ;

CALL sp_editarCargoEmpleado(1, 'Gerente General', 'Responsable de la dirección y gestión general del personal');

-- CRUD PROVEEDOR

DELIMITER $$

CREATE PROCEDURE sp_agregarProveedor (
    IN _codigoProveedor INT,
    IN _NITProveedor VARCHAR(13),
    IN _nombresProveedor VARCHAR(60),
    IN _apellidosProveedor VARCHAR(60),
    IN _direccionProveedor VARCHAR(150),
    IN _razonSocial VARCHAR(60),
    IN _contactoPrincipal VARCHAR(100),
    IN _paginaWeb VARCHAR(50),
    IN _telefonoProveedor VARCHAR(13),
    IN _emailProveedor VARCHAR(50)
)
BEGIN
    INSERT INTO Proveedores (codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb, telefonoProveedor, emailProveedor)
    VALUES (_codigoProveedor, _NITProveedor, _nombresProveedor, _apellidosProveedor, _direccionProveedor, _razonSocial, _contactoPrincipal, _paginaWeb, _telefonoProveedor, _emailProveedor);
END $$

DELIMITER ;

CALL sp_agregarProveedor(1, '1234567890123', 'Carlos', 'Martinez', 'Calle 123', 'Razon Social 1', 'Contacto Principal 1', 'www.proveedor1.com', '12345678', 'proveedor1@gmail.com');
CALL sp_agregarProveedor(2, '9876543210987', 'Ana', 'Lopez', 'Avenida 456', 'Razon Social 2', 'Contacto Principal 2', 'www.proveedor2.com', '87654321', 'proveedor2@hotmail.com');

DELIMITER $$

CREATE PROCEDURE sp_mostrarProveedores ()
BEGIN
    SELECT
        codigoProveedor,
        NITProveedor,
        nombresProveedor,
        apellidosProveedor,
        direccionProveedor,
        razonSocial,
        contactoPrincipal,
        paginaWeb,
        telefonoProveedor,
        emailProveedor
    FROM
        Proveedores;
END $$

DELIMITER ;

CALL sp_mostrarProveedores;

DELIMITER $$

CREATE PROCEDURE sp_buscarProveedor (IN _codigoProveedor INT)
BEGIN
    SELECT
        codigoProveedor,
        NITProveedor,
        nombresProveedor,
        apellidosProveedor,
        direccionProveedor,
        razonSocial,
        contactoPrincipal,
        paginaWeb,
        telefonoProveedor,
        emailProveedor
    FROM
        Proveedores
    WHERE
        codigoProveedor = _codigoProveedor;
END $$

DELIMITER ;

CALL sp_buscarProveedor(1);

DELIMITER $$

CREATE PROCEDURE sp_eliminarProveedor (IN _codigoProveedor INT)
BEGIN
    DELETE FROM Proveedores
    WHERE codigoProveedor = _codigoProveedor;
END $$

DELIMITER ;

CALL sp_eliminarProveedor(1);

DELIMITER $$

CREATE PROCEDURE sp_editarProveedor (
    IN _codigoProveedor INT,
    IN _NITProveedor VARCHAR(13),
    IN _nombresProveedor VARCHAR(60),
    IN _apellidosProveedor VARCHAR(60),
    IN _direccionProveedor VARCHAR(150),
    IN _razonSocial VARCHAR(60),
    IN _contactoPrincipal VARCHAR(100),
    IN _paginaWeb VARCHAR(50),
    IN _telefonoProveedor VARCHAR(13),
    IN _emailProveedor VARCHAR(50)
)
BEGIN
    UPDATE Proveedores
    SET
        NITProveedor = _NITProveedor,
        nombresProveedor = _nombresProveedor,
        apellidosProveedor = _apellidosProveedor,
        direccionProveedor = _direccionProveedor,
        razonSocial = _razonSocial,
        contactoPrincipal = _contactoPrincipal,
        paginaWeb = _paginaWeb,
        telefonoProveedor = _telefonoProveedor,
        emailProveedor = _emailProveedor
    WHERE
        codigoProveedor = _codigoProveedor;
END $$

DELIMITER ;

CALL sp_editarProveedor(2, '1234567890123', 'Carlos', 'Gomez', 'Calle 789', 'Razon Social Editada', 'Contacto Principal Editado', 'www.proveedor-editado.com', '12345678', 'proveedor-editado@gmail.com');
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
    IN p_codigoProducto int,
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

CALL sp_agregarProducto(1, 'Arroz', 0.00, 0.00, 0.00, 'arroz.jpg', 100, 2, 2);
CALL sp_agregarProducto(2, 'Frijoles', 0.00, 0.00, 0.00, 'frijoles.jpg', 150, 2, 2);
CALL sp_agregarProducto(3, 'Aceite', 0.00, 0.00, 0.00, 'aceite.jpg', 80, 3, 2);
CALL sp_agregarProducto(4, 'Leche Entera', 0.00, 0.00, 0.00, 'leche.jpg', 120, 3, 2);
CALL sp_agregarProducto(5, 'Azúcar', 0.00, 0.00, 0.00, 'azucar.jpg', 90, 4, 2);

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

call sp_editarProducto(1, 'Pollo', 0.00, 0.00, 0.00, 'pollo.jpg', 100, 2, 2);

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

CALL sp_crearDetalleCompra(2, 50.00, 10, 5, 2);
CALL sp_crearDetalleCompra(3, 50.00, 10, 5, 3);

DELIMITER $$
CREATE PROCEDURE sp_mostrarDetallesCompra()
BEGIN
    SELECT * FROM DetalleCompra;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleCompra(
    IN codDetComp int  -- Cambiado a VARCHAR
)
BEGIN
    SELECT * FROM DetalleCompra
    WHERE codigoProducto = codDetComp;
END$$
DELIMITER ;

call sp_buscarDetalleCompra(1);

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

CALL sp_editarDetalleCompra(2, 55.00, 12, 1, 2);

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
    IN p_codigoProducto int
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
    IN p_codigoProducto int
)
BEGIN
    INSERT INTO DetalleFactura(codigoDetalleFactura, precioUnitario, cantidad, numeroDeFactura, codigoProducto)
    VALUES(p_codigoDetalleFactura, p_precioUnitario, p_cantidad, p_numeroDeFactura, p_codigoProducto);
END$$
DELIMITER ;

CALL sp_crearDetalleFactura(1, 50.00, 10, 2, 1);
CALL sp_crearDetalleFactura(3, 50.00, 10, 3, 3);

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
    IN p_nuevoCodigoProducto int
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
CALL sp_editarDetalleFactura(1, 55.00, 3, 3, 1);

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
    IN p_codigoProducto int,
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
    SET precioUnitario = NEW.costoUnitario,
        precioDocena = precioDocena,
        precioMayor = precioMayor
    WHERE codigoProducto = NEW.codigoProducto;
    
END $$

DELIMITER ;

-- Trigger para actualización del total en la tabla Facturas al insertar un DetalleCompra
DELIMITER $$
CREATE TRIGGER ActualizarTotalCompraInsert AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
    UPDATE Compras
    SET totalDocumento = totalDocumento + NEW.costoUnitario * NEW.cantidad
    WHERE numeroDocumento = NEW.numeroDocumento;
END
$$
DELIMITER ;

-- Trigger para actualización del total en la tabla Compras al actualizar un DetalleCompra
DELIMITER $$
CREATE TRIGGER ActualizarTotalCompraUpdate AFTER UPDATE ON DetalleCompra
FOR EACH ROW
BEGIN
    UPDATE Compras
    SET totalDocumento = totalDocumento + (NEW.costoUnitario * NEW.cantidad) - (OLD.costoUnitario * OLD.cantidad)
    WHERE numeroDocumento = NEW.numeroDocumento;
END $$
DELIMITER ;

-- Trigger para actualización del total en la tabla Facturas al eliminar un DetalleFactura
DELIMITER $$
CREATE TRIGGER ActualizarTotalCompraDelete AFTER DELETE ON DetalleCompra
FOR EACH ROW
BEGIN
    UPDATE Compras
    SET totalDocumento = totalDocumento - OLD.costoUnitario * OLD.cantidad
    WHERE numeroDocumento = OLD.numeroDocumento;
END $$
DELIMITER ;

delimiter $$
create procedure sp_eliminarProductoPorProveedor(in codProveedor int)
begin
	delete from productos 
    where codigoProveedor = codProveedor;
end$$
delimiter ;

delimiter $$
create procedure sp_eliminarDetalleFacturaPorProducto(in codProducto int)
begin
	delete from detalleFactura 
    where codigoProducto = codProducto;
end$$
delimiter ;



set global time_zone= '-6:00';
