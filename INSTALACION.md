## Guía Completa de Instalación y Despliegue

## Sigue este instructivo paso a paso para clonar, configurar y ejecutar el proyecto Farmacia Backend en tu entorno local.

## 📋 1. Requisitos Previos

### Asegúrate de tener instaladas las siguientes herramientas en tu sistema operativo:

### Java Development Kit (JDK) 17 o 21

### Verificar en terminal: java -version

### Apache Maven 3.8+

### Verificar en terminal: mvn -version

### MySQL Server 8.0+ & MySQL Workbench

### Servicio activo en el puerto 3306.

### Git

### Verificar en terminal: git --version

### IDE sugerido: IntelliJ IDEA o VS Code con Extension Pack for Java.

## 🗄️ 2. Configuración de la Base de Datos (MySQL)

### Abre MySQL Workbench o tu cliente SQL preferido y conéctate a tu servidor local.

### Crea la base de datos ejecutando:

CREATE DATABASE IF NOT EXISTS farmacia_db;
USE farmacia_db;


### Ejecuta el script de creación de tablas y Procedimientos Almacenados (sp_...):

-- TABLA CLIENTES
CREATE TABLE IF NOT EXISTS clientes (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
telefono VARCHAR(20)
);

-- TABLA MEDICAMENTOS
CREATE TABLE IF NOT EXISTS medicamentos (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
principio_activo VARCHAR(100),
precio DECIMAL(10,2) NOT NULL,
stock INT NOT NULL,
requiere_receta BOOLEAN DEFAULT FALSE,
categoria_id BIGINT
);

-- TABLA VENTAS
CREATE TABLE IF NOT EXISTS ventas (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
cliente_id BIGINT NOT NULL,
medicamento_id BIGINT NOT NULL,
cantidad INT NOT NULL,
total DECIMAL(10,2) NOT NULL,
fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (cliente_id) REFERENCES clientes(id),
FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id)
);

-- PROCEDIMIENTO ALMACENADO CREAR CLIENTE
DELIMITER //
CREATE PROCEDURE sp_crear_cliente(IN p_nombre VARCHAR(100), IN p_telefono VARCHAR(20))
BEGIN
INSERT INTO clientes(nombre, telefono) VALUES (p_nombre, p_telefono);
END //
DELIMITER ;

-- PROCEDIMIENTO ALMACENADO CREAR VENTA (CON VALIDACIÓN DE STOCK)
DELIMITER //
CREATE PROCEDURE sp_crear_venta(
IN p_cliente_id BIGINT,
IN p_medicamento_id BIGINT,
IN p_cantidad INT,
OUT p_id BIGINT,
OUT p_total DECIMAL(10,2)
)
BEGIN
DECLARE v_precio DECIMAL(10,2);
DECLARE v_stock INT;

    SELECT precio, stock INTO v_precio, v_stock FROM medicamentos WHERE id = p_medicamento_id;
    
    IF v_stock IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El medicamento no existe.';
    ELSEIF v_stock < p_cantidad THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente para realizar la venta.';
    ELSE
        SET p_total = v_precio * p_cantidad;
        INSERT INTO ventas(cliente_id, medicamento_id, cantidad, total, fecha_venta)
        VALUES (p_cliente_id, p_medicamento_id, p_cantidad, p_total, NOW());
        
        SET p_id = LAST_INSERT_ID();
        
        UPDATE medicamentos SET stock = stock - p_cantidad WHERE id = p_medicamento_id;
    END IF;
END //
DELIMITER ;


## ⚙️ 3. Configuración de application.properties

### Navega a la carpeta src/main/resources/application.properties y verifica la conexión a tu base de datos local:

spring.application.name=farmacia
spring.datasource.url=jdbc:mysql://localhost:3306/farmacia_db?useSSL=false&serverTimezone=America/Mexico_City&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA_MYSQL
spring.jpa.hibernate.ddl-auto=none


## 🚀 4. Clonado y Ejecución del Proyecto

## 4.1 Clonar el repositorio

### Abre CMD o Terminal y ejecuta:

cd C:\
mkdir proyectos
cd proyectos
git clone https://github.com/IsaacMichel19/proyecto_farmacia.git
cd proyecto_farmacia


## 4.2 Compilar y Ejecutar con Maven

### En la raíz del proyecto (donde está el archivo pom.xml):

mvn clean install
mvn spring-boot:run


### O abre la carpeta en IntelliJ IDEA, espera a que carguen las dependencias de Maven y ejecuta la clase FarmaciaApplication.java.

## ✅ 5. Verificación de Funcionamiento

### Cuando la consola muestre el mensaje:

Started FarmaciaApplication in X.XXX seconds (process running)


### El servidor estará listo atendiendo peticiones HTTP en http://localhost:8080.