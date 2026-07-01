# 🚨 AlarmaParo

![AlarmaParo](portada-alarma-paro.png)

> **AlarmaParo** es un software de escritorio desarrollado en **JavaFX** que facilita la gestión y el seguimiento de la renovación de la demanda de empleo. La aplicación ayuda a evitar olvidos mediante un sistema de recordatorios, un calendario integrado y un historial completo de renovaciones, ofreciendo una solución sencilla, rápida e intuitiva.

---

# 📖 Descripción

**AlarmaParo** nace con el objetivo de proporcionar una herramienta que permita controlar de forma cómoda el estado de la demanda de empleo y conocer en todo momento cuándo debe realizarse la siguiente renovación.

La aplicación permite registrar renovaciones, administrar usuarios, configurar recordatorios personalizados, consultar el historial de sellados y visualizar una cuenta atrás con el tiempo restante hasta la próxima renovación.

Toda la información se almacena de forma persistente en una base de datos **MySQL**, mientras que la interfaz gráfica ha sido desarrollada con **JavaFX**, proporcionando una experiencia visual moderna y fácil de utilizar.

---

# ✨ Funcionalidades

- 👤 Gestión completa de usuarios.
- 📅 Registro de renovaciones de la demanda de empleo.
- ✏️ Edición de renovaciones.
- ❌ Eliminación de renovaciones.
- 🔍 Consulta y búsqueda de renovaciones.
- 🔔 Creación de recordatorios personalizados.
- ⏳ Cuenta atrás automática hasta el próximo sellado.
- 📚 Historial completo de renovaciones.
- 🗓️ Calendario con próximas renovaciones.
- 💾 Persistencia de datos mediante MySQL.
- 🎨 Interfaz moderna desarrollada con JavaFX.

---

# 🖥️ Tecnologías utilizadas

| Tecnología | Descripción |
|------------|-------------|
| ☕ Java | Lenguaje principal de desarrollo |
| 🎨 JavaFX | Desarrollo de la interfaz gráfica |
| 🗄️ MySQL | Sistema gestor de base de datos |
| 🔌 JDBC | Conexión entre la aplicación y MySQL |
| 📦 Maven | Gestión de dependencias |
| 🎨 CSS | Personalización de la interfaz |
| 🛠️ Scene Builder | Diseño de las vistas FXML |

---

# 🗄️ Base de datos

La aplicación utiliza **MySQL** como sistema gestor de base de datos para almacenar toda la información del sistema.

El proyecto incluye el script SQL necesario para crear automáticamente la base de datos y todas sus tablas.

Las principales entidades gestionadas son:

- Usuarios
- Renovaciones
- Recordatorios
- Historial
- Calendario

El acceso a los datos se realiza mediante **JDBC**, implementando el patrón de diseño **DAO (Data Access Object)** para mantener una correcta separación entre la lógica de negocio y el acceso a la base de datos.

---

# 📌 Operaciones CRUD

La aplicación implementa operaciones completas **CRUD (Create, Read, Update y Delete)** sobre todas las entidades principales.

## Renovaciones

- Crear renovación.
- Consultar renovaciones.
- Modificar renovación.
- Eliminar renovación.

## Recordatorios

- Crear recordatorio.
- Consultar recordatorios.
- Modificar recordatorio.
- Eliminar recordatorio.

## Usuarios

- Crear usuario.
- Consultar usuarios.
- Modificar usuario.
- Eliminar usuario.

---

# 🔔 Sistema de recordatorios

AlarmaParo incorpora un sistema de avisos configurable que permite recordar con antelación la fecha de renovación de la demanda de empleo.

Desde la pantalla principal el usuario puede consultar en cualquier momento:

- ⏳ Tiempo restante hasta el próximo sellado.
- 📅 Fecha de la próxima renovación.
- ✅ Fecha del último sellado.
- 📚 Historial completo de renovaciones.
- 🗓️ Calendario con las próximas fechas.

Gracias a ello, el usuario puede realizar un seguimiento continuo de su demanda de empleo y reducir el riesgo de olvidar la renovación.

---

# 🚀 Instalación

## 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/AlarmaParo.git
```

## 2. Abrir el proyecto

Accede al directorio del proyecto:

```bash
cd AlarmaParo
```

Importa el proyecto directamente desde **Apache NetBeans** como un proyecto **Maven**.

---

## 3. Importar la base de datos

La base de datos **MySQL** ya se encuentra incluida dentro del proyecto mediante un archivo **.sql**.

Únicamente debes importar dicho archivo utilizando **MySQL Workbench**, **phpMyAdmin** o cualquier otro gestor compatible con MySQL.

Una vez importada la base de datos, verifica que el servidor MySQL se encuentre en ejecución.

---

## 4. Configurar la conexión

Si tu instalación utiliza una dirección IP, puerto, usuario, contraseña o nombre de base de datos diferente, deberás modificar la configuración de la conexión.

Dirígete al siguiente directorio del proyecto:

```text
src/main/java/config/
```

Dentro de esta carpeta encontrarás el archivo encargado de configurar la conexión con MySQL.

Modifica los siguientes parámetros según tu instalación:

- Dirección IP o Host.
- Puerto de conexión.
- Nombre de la base de datos.
- Usuario.
- Contraseña.

Guarda los cambios y vuelve a ejecutar la aplicación.

---

## 5. Ejecutar la aplicación

Una vez importada la base de datos y configurada la conexión, ejecuta el proyecto desde **Apache NetBeans**.

---

# 👨‍💻 Autor

**David Milanés Moreno**

**AlarmaParo** es un **software independiente** desarrollado utilizando **Java**, **JavaFX**, **MySQL** y **JDBC**.

El proyecto implementa una arquitectura basada en el patrón **DAO (Data Access Object)** y operaciones **CRUD** completas para la gestión de usuarios, renovaciones y recordatorios de la demanda de empleo, ofreciendo además un calendario integrado y un historial de renovaciones para facilitar el seguimiento de todas las fechas importantes.

---

# 📄 Licencia

Este proyecto se distribuye como **software independiente** y ha sido desarrollado con fines educativos, de aprendizaje y como demostración de conocimientos en el desarrollo de aplicaciones de escritorio utilizando **JavaFX**, **JDBC** y **MySQL**.
