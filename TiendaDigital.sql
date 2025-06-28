--Borrar la Base de datos
use master;
drop database tienda_juegos;
--Crear la base de datos
create database tienda_juegos;
use tienda_juegos;
-- Procedimiento para crear tablas
CREATE OR ALTER PROCEDURE CrearTablas
AS
BEGIN
    -- Crear tabla usuarios si no existe
    IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='usuarios' AND xtype='U')
    BEGIN
        CREATE TABLE usuarios (
            id INT IDENTITY(1,1) PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            usuario VARCHAR(50) UNIQUE NOT NULL,
            contrasena VARCHAR(255) NOT NULL,
            rol VARCHAR(20) DEFAULT 'usuario' NOT NULL,
            tarjeta VARCHAR(20)
        );
        PRINT 'Tabla "usuarios" creada.';
    END
    ELSE
        PRINT 'Tabla "usuarios" ya existe.';

    -- Crear tabla juegos si no existe
    IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='juegos' AND xtype='U')
    BEGIN
        CREATE TABLE juegos (
            id INT IDENTITY(1,1) PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            imagen VARCHAR(255),
            precio INT NOT NULL,
            descripcion TEXT
        );
        PRINT 'Tabla "juegos" creada.';
    END
    ELSE
        PRINT 'Tabla "juegos" ya existe.';

    -- Crear tabla compras si no existe
    IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='compras' AND xtype='U')
    BEGIN
        CREATE TABLE compras (
            id_compra INT IDENTITY(1,1) PRIMARY KEY,
			id_usuario INT NOT NULL,
			id_juego INT NOT NULL,
			fecha_compra DATETIME DEFAULT GETDATE(),
			FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
			FOREIGN KEY (id_juego) REFERENCES juegos(id)
        );
        PRINT 'Tabla "compras" creada.';
    END
    ELSE
        PRINT 'Tabla "compras" ya existe.';
    -- Crear tabla soporte_mensajes si no existe
    IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='soporte_mensajes' AND xtype='U')
    BEGIN
        CREATE TABLE soporte_mensajes (
            id INT IDENTITY(1,1) PRIMARY KEY,
            id_usuario INT NOT NULL,
            descripcion_problema TEXT NOT NULL,
            fecha_envio DATETIME DEFAULT GETDATE(),
            FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
        );
        PRINT 'Tabla "soporte_mensajes" creada.';
    END
    ELSE
        PRINT 'Tabla "soporte_mensajes" ya existe.';
END;
GO


-- Procedimiento para agregar juegos con nombres de imagen consistentes
CREATE OR ALTER PROCEDURE AgregarJuegos
AS
BEGIN
    -- Tabla temporal para evitar duplicados
    DECLARE @juegos TABLE (
        nombre VARCHAR(100),
        imagen VARCHAR(255),
        precio INT,
        descripcion TEXT
    );
    
    -- Insertar con nombres de imagen consistentes (minúsculas, sin espacios)
    INSERT INTO @juegos (nombre, imagen, precio, descripcion)
    VALUES
        ('Dragon Ball Sparking! Zero', 'dragon_ball_sparking_zero.jpg', 54999, 'Dragon Ball Sparking! Zero es un juego de peleas.'),
        ('Expedición 33', 'expedicion_33.jpg', 33900, 'Un juego de rol por turnos con Gustav.'),
        ('Dark Souls III', 'dark_souls_3.jpg', 38499, 'Juego de acción con dificultad extrema.'),
        ('Elden Ring', 'elden_ring.jpg', 59990, 'Elden Ring es un juego de rol de acción en un mundo abierto creado por FromSoftware y George R. R. Martin.'),
        ('Palworld', 'palworld.jpg', 39990, 'Palworld es un juego multijugador de supervivencia y captura de criaturas.'),
        ('Devil May Cry V', 'devil_may_cry_5.jpg', 34990, 'Devil May Cry es un juego de acción con combates estilizados y enemigos demoníacos.'),
        ('The Last of Us parte I', 'the_last_of_us.jpg', 49990, 'The Last of Us es una aventura narrativa post-apocalíptica.'),
        ('Undertale', 'undertale.jpg', 9990, 'Undertale es un RPG único donde puedes derrotar a los enemigos sin matar.'),
        ('Sea of Stars', 'sea_of_stars.jpg', 19990, 'Sea of Stars es un RPG por turnos inspirado en los clásicos.'),
        ('GTA V', 'gta_5.jpg', 29990, 'Grand Theft Auto V ofrece un mundo abierto masivo con acción y crimen.'),
        ('Max Payne 3', 'max_payne_3.jpg', 24990, 'Max Payne 3 es un shooter en tercera persona lleno de acción.'),
        ('Sekiro', 'sekiro.jpg', 45990, 'Sekiro: Shadows Die Twice es un desafiante juego de acción.'),
        ('Need for Speed: Most Wanted', 'need_for_speed_most_wanted.jpg', 19990, 'Need for Speed: Most Wanted combina carreras callejeras.'),
        ('REPO', 'repo.jpg', 15990, 'Repo es un juego de acción futurista con estética cyberpunk.'),
        ('Stardew Valley', 'stardew_valley.jpg', 8990, 'Stardew Valley es un simulador de granja.'),
        ('Resident Evil 4 Remake', 'resident_evil_4_remake.jpg', 45990, 'Resident Evil 4 Remake moderniza el clásico juego.'),
        ('Dead Space Remake', 'dead_space_remake.jpg', 49990, 'Dead Space Remake es una aterradora reimaginación.'),
        ('Shadow of the Colossus', 'shadow_of_the_colossus.jpg', 45990, 'Shadow of the Colossus renace con gráficos mejorados.');

    -- Insertar solo los que no existen
    INSERT INTO juegos (nombre, imagen, precio, descripcion)
    SELECT j.nombre, j.imagen, j.precio, j.descripcion
    FROM @juegos j
    WHERE NOT EXISTS (
        SELECT 1 FROM juegos g WHERE g.nombre = j.nombre
    );
    
    PRINT 'Juegos insertados correctamente con nombres de imagen consistentes.';
END;
GO
--Procedimiento para agregar imagen a un juego nuevo
CREATE OR ALTER PROCEDURE sp_ActualizarImagenJuego
    @id_juego INT,
    @ruta_imagen VARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;
    
    BEGIN TRY
        -- Verificar si el juego existe
        IF NOT EXISTS (SELECT 1 FROM juegos WHERE id = @id_juego)
        BEGIN
            RAISERROR('El juego con ID %d no existe', 16, 1, @id_juego);
            RETURN;
        END
        
        -- Validar extensión de imagen
        DECLARE @extension VARCHAR(10) = LOWER(RIGHT(@ruta_imagen, CHARINDEX('.', REVERSE(@ruta_imagen)) - 1));
        
        IF @extension NOT IN ('jpg', 'jpeg', 'png', 'gif')
        BEGIN
            RAISERROR('Extensión de archivo no válida. Use .jpg, .jpeg, .png o .gif', 16, 1);
            RETURN;
        END
        
        -- Actualizar la imagen del juego
        UPDATE juegos
        SET imagen = @ruta_imagen
        WHERE id = @id_juego;
        
        PRINT 'Imagen actualizada correctamente para el juego ID: ' + CAST(@id_juego AS VARCHAR);
        PRINT 'Ruta de imagen: ' + @ruta_imagen;
    END TRY
    BEGIN CATCH
        PRINT 'Error al actualizar imagen: ' + ERROR_MESSAGE();
    END CATCH
END;
GO
-- Ejecutar los procedimientos
EXEC CrearTablas;
EXEC AgregarJuegos;
GO

-- Insertar juego de prueba
INSERT INTO juegos (nombre, precio, descripcion)
VALUES ('test',  1, 'Juego de prueba');
-- Actualizar imagen de algun juego nuevo
EXEC sp_ActualizarImagenJuego 
    @id_juego = 19, 
    @ruta_imagen = 'test.PNG';

-- Verificar el cambio
SELECT id, nombre, imagen FROM juegos WHERE id = 22;
-- Mostrar juegos
Select * FROM juegos;
-- Mostrar usuarios (vacío inicialmente)
SELECT * FROM usuarios;
-- Mostrar Quejas o cosas del soporte
SELECT * FROM soporte_mensajes;
-- Eliminar algun usuario
DELETE FROM usuarios WHERE id = '3';
-- Seleccionar juegos comprados y su usuario
SELECT * FROM compras;

SELECT * FROM usuarios;
SELECT * FROM soporte_mensajes;