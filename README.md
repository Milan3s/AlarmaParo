```markdown
# 🚨 AlarmaParo

![AlarmaParo](docs/img/portada.png)

## 📋 Descripción

**AlarmaParo** es una aplicación de escritorio desarrollada en **JavaFX** cuyo objetivo es ayudar a gestionar la renovación de la demanda de empleo.

La aplicación permite registrar renovaciones, crear recordatorios, consultar el historial de renovaciones y visualizar un calendario con las próximas fechas importantes, evitando que el usuario olvide sellar el paro.

---

## ✨ Características

- 👤 Gestión de usuarios.
- 📅 Registro de renovaciones de la demanda de empleo.
- 🔔 Creación de recordatorios automáticos.
- 🗓️ Calendario con próximas renovaciones.
- 📚 Historial completo de renovaciones.
- ✏️ Edición y eliminación de registros (CRUD).
- 🔍 Búsqueda de información.

---

## 🛠️ Tecnologías utilizadas

- Java 21
- JavaFX
- MySQL
- JDBC
- Maven
- Scene Builder
- CSS

---

## 📂 Estructura del proyecto

```

AlarmaParo
│
├── src
│   ├── dao
│   ├── database
│   ├── main
│   ├── models
│   └── resources
│       ├── css
│       ├── images
│       └── fxml
│
├── docs
│   └── img
│       └── portada.png
│
└── pom.xml

````

---

## 🗄️ Base de datos

La aplicación utiliza **MySQL** para almacenar toda la información.

Principales tablas:

- Usuarios
- Renovaciones
- Recordatorios
- Calendario
- Historial

La comunicación con la base de datos se realiza mediante **JDBC**.

---

## 📌 Funcionalidades del CRUD

### Usuarios

- Crear usuarios
- Modificar usuarios
- Eliminar usuarios
- Consultar usuarios

### Renovaciones

- Registrar renovación
- Editar renovación
- Eliminar renovación
- Consultar renovaciones

### Recordatorios

- Crear recordatorios
- Modificar recordatorios
- Eliminar recordatorios
- Consultar recordatorios

---

## 🔔 Sistema de recordatorios

El sistema permite registrar avisos antes de la fecha de renovación de la demanda de empleo.

Cada recordatorio puede configurarse con una antelación determinada para facilitar al usuario la renovación dentro del plazo correspondiente.

---

## 📸 Capturas

Puedes añadir más imágenes aquí:

```markdown
![Pantalla principal](docs/img/inicio.png)

![Renovaciones](docs/img/renovaciones.png)

![Recordatorios](docs/img/recordatorios.png)
````

---

## 🚀 Cómo ejecutar

1. Clonar el repositorio.

```bash
git clone https://github.com/TU_USUARIO/AlarmaParo.git
```

2. Abrir el proyecto con NetBeans o cualquier IDE compatible con Maven.

3. Configurar la conexión a MySQL.

4. Crear la base de datos e importar el script SQL.

5. Ejecutar el proyecto.

---

## 👨‍💻 Autor

Desarrollado como proyecto independiente utilizando JavaFX y MySQL.

---

## 📄 Licencia

Proyecto con fines educativos.

```
```
