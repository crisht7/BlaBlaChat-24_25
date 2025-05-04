# Proyecto BlaBlaChat

Este es un proyecto de mensajería desarrollado utilizando Java con Swing para la interfaz gráfica de usuario, H2 para la base de datos y varios patrones de diseño como DAO, Singleton y Fachada. El sistema permite a los usuarios interactuar a través de mensajes de texto, emojis, y gestión de contactos y grupos, además de funcionalidades adicionales como la búsqueda avanzada y la exportación de conversaciones.

## Tabla de Contenidos

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Arquitectura](#arquitectura)
   - [Patrones de Diseño Utilizados](#patrones-de-diseño-utilizados)
   - [Modelo Vista Controlador (MVC)](#modelo-vista-controlador-mvc)
3. [Clases Principales](#clases-principales)
   - [appChat](#appchat)
   - [controlador](#controlador)
   - [persistencia](#persistencia)
4. [Historias de Usuario](#historias-de-usuario)
5. [Instalación y Ejecución](#instalación-y-ejecución)
6. [Licencia](#licencia)

---

## Descripción del Proyecto

BlaBlaChat es una aplicación de mensajería que permite a los usuarios gestionar sus contactos, grupos y enviar mensajes (tanto de texto como emoticonos). El sistema implementa una base de datos H2 con persistencia mediante el patrón DAO (Data Access Object) y emplea otros patrones de diseño como Singleton para la clase `Controlador` y Fachada para la interacción con la lógica de negocio.

### Funcionalidades principales:
- Autenticación de usuarios con login y registro.
- Creación, edición y eliminación de contactos y grupos.
- Envío y recepción de mensajes (texto y emoticonos).
- Búsqueda avanzada de mensajes.
- Funcionalidad premium para exportar conversaciones a PDF.
- Manejo de errores y validación de entrada.

---

## Arquitectura

La arquitectura del sistema sigue el patrón **Modelo-Vista-Controlador (MVC)**, donde:

- **Modelo**: Representa las entidades como `Usuario`, `Contacto`, `Mensaje` y `Grupo`.
- **Vista**: Interfaz gráfica de usuario implementada en Java Swing.
- **Controlador**: Lógica de negocio centralizada que orquesta las interacciones entre la vista y el modelo.

### Patrones de Diseño Utilizados

1. **DAO (Data Access Object)**: Para gestionar la persistencia de datos en la base de datos H2.
2. **Singleton**: Para garantizar que solo haya una instancia de las clases principales como el `Controlador`.
3. **Fachada**: Para simplificar la interacción con el sistema a través de la clase `Controlador`.
4. **Observer**: Implicitamente usado en la actualización de la interfaz al recibir nuevos mensajes.
5. **Comando (Command)**: Usado en el sistema de descuentos para usuarios premium.

---

## Clases Principales

### appChat

Las clases en el paquete `appChat` representan los modelos del dominio. Algunas de las clases principales son:

- **Usuario**: Representa a un usuario en el sistema con atributos como nombre, teléfono, foto de perfil, etc.
- **Contacto**: Representa un contacto genérico, que puede ser un `ContactoIndividual` o un `Grupo`.
- **Mensaje**: Representa un mensaje enviado entre usuarios o a grupos.
- **Grupo**: Representa un grupo de contactos que pueden recibir mensajes grupales.

### controlador

La clase **Controlador** gestiona la lógica del sistema y actúa como punto de entrada para las operaciones del usuario, como:

- Autenticación (`hacerLogin`).
- Gestión de contactos y grupos (`crearContacto`, `abrirChatConTelefono`).
- Envío de mensajes.

### persistencia

Las clases de persistencia permiten almacenar y recuperar objetos del modelo en la base de datos mediante el uso de DAOs. Entre las principales clases de persistencia se encuentran:

- **AdaptadorUsuario**: Gestor de la persistencia de usuarios.
- **AdaptadorContactoIndividual**: Gestor de la persistencia de contactos individuales.
- **AdaptadorGrupo**: Gestor de la persistencia de grupos.
- **AdaptadorMensaje**: Gestor de la persistencia de mensajes.

---

## Historias de Usuario

Durante el desarrollo de BlaBlaChat, se han seguido historias de usuario para definir las funcionalidades del sistema. Algunas de las historias de usuario son:

- **HU1 - Login de Usuario**: Como usuario registrado, quiero iniciar sesión con mi número de teléfono y contraseña, para acceder a mis conversaciones y funcionalidades.
- **HU2 - Registro de Usuario**: Como usuario no registrado, quiero registrarme proporcionando mis datos personales para poder acceder a la aplicación.
- **HU5 - Crear Grupo**: Como usuario logueado, quiero crear grupos de difusión introduciendo un nombre único y una imagen opcional, para enviar mensajes simultáneamente a varios contactos.
- **HU7 - Enviar Mensaje a Usuario**: Como usuario logueado, quiero enviar mensajes de texto o emojis a otros usuarios, para comunicarme de forma individual y mantener un historial ordenado por fecha y hora.

---

## Instalación y Ejecución

1. Clona el repositorio:

    ```bash
    git clone https://github.com/tu_usuario/blaBlaChat.git
    ```

2. Asegúrate de tener Java 8 o superior instalado.

3. Compila el proyecto utilizando Maven:

    ```bash
    mvn clean install
    ```

4. Ejecuta el archivo principal:

    ```bash
    mvn exec:java
    ```

---

## Licencia

Este proyecto está licenciado bajo la MIT License - consulta el archivo [LICENSE](LICENSE) para más detalles.

---

