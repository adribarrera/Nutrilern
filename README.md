# Nutrilern - Gestión Nutricional Inteligente

Nutrilern es una aplicación de escritorio diseñada para ayudar a los usuarios a tomar el control total de su alimentación y progreso físico a través de una interfaz moderna, intuitiva y basada en datos científicos.

## 🌟 Perspectiva del Usuario

Nutrilern actúa como un asistente nutricional personal que permite:

*   **Panel de Control Personalizado:** Visualiza de un vistazo tu progreso diario (calorías y macronutrientes) y tu estado físico actual (IMC).
*   **Registro Diario de Comidas:** Una tabla interactiva donde puedes añadir alimentos desde una base de datos global, ajustar cantidades y ver cómo impactan en tus objetivos en tiempo real.
*   **Seguimiento de Evolución:** Gráficas dinámicas que muestran la evolución de tu peso, distribución de calorías semanales y desglose de macronutrientes por día.
*   **Seguridad y Perfil:** Gestión completa de perfil con objetivos personalizados (perder peso, mantener, ganar músculo) y un sistema seguro de recuperación de cuenta vía email.
*   **Administración (Solo Admin):** Capacidad para gestionar la base de datos de usuarios e importar alimentos de forma masiva mediante archivos CSV.

## 🛠️ Perspectiva Técnica

Nutrilern ha sido desarrollado siguiendo estándares profesionales para garantizar robustez y escalabilidad.

### Arquitectura y Tecnologías
*   **Lenguaje:** Java 17+.
*   **Interfaz Gráfica:** Java Swing con un sistema de temas personalizado (`TemaNutrix`) para una estética moderna y fluida.
*   **Patrón de Diseño:** **MVC (Modelo-Vista-Controlador)**, separando estrictamente la lógica de negocio, el acceso a datos y la interfaz de usuario.
*   **Gestión de Dependencias:** Maven.
*   **Base de Datos:** MySQL (Relacional), gestionada a través de DAOs (Data Access Objects) y una conexión centralizada.

### Seguridad
*   **Cifrado de Contraseñas:** Uso de la librería **jBCrypt** para el hasheo seguro de contraseñas con salt. Nunca se almacenan contraseñas en texto plano.
*   **Verificación por Email:** Integración con el protocolo **SMTP** mediante `JavaMail API` para el envío de códigos de verificación únicos (8 dígitos) durante el registro y la recuperación de contraseña.

### Componentes Clave
*   **Calculadora Nutricional:** Algoritmos basados en el perfil físico del usuario para calcular el gasto calórico y objetivos de macros.
*   **Motor de Gráficos:** Implementación personalizada de gráficos de barras, circulares y lineales utilizando `Java2D` y `Graphics2D` para una visualización de datos sin dependencias externas pesadas.

## 🚀 Instalación y Ejecución

Para que el proyecto funcione correctamente en el entorno de evaluación, siga estos pasos:
1.  **Base de Datos:**
    *   Importe el archivo SQL adjunto (`nutrilern.sql`) en su servidor MySQL.
    *   Configure las credenciales de acceso en el archivo `src/main/resources/db.properties`.

2.  **Servicio de Correo (Opcional):**
    *   La aplicación utiliza el archivo `src/main/resources/correo.properties` para enviar códigos de verificación.
    *   Si desea probarlo, se recomienda usar una "Contraseña de Aplicación" de Google.

3.  **Compilación y Ejecución:**
    ```bash
    mvn clean install
    mvn exec:java -Dexec.mainClass="com.nutrilern.Main"
    ```

## 📖 Guía de Uso (Tutorial)

A continuación se describe cómo navegar y utilizar las principales funciones de Nutrilern:

### 1. Acceso y Seguridad
*   **Registro:** Haz clic en "¿No tienes cuenta? ¡Regístrate!". Deberás validar tu email mediante un código de 8 dígitos enviado a tu correo. Tras la validación, completa tu perfil físico para que Nutrilern calcule tus objetivos.
*   **Recuperación:** Si olvidas tu contraseña, usa el enlace "¿Has olvidado la contraseña?". Recibirás un código de seguridad para restablecerla.

### 2. Panel Principal (Dashboard)
Es tu centro de mando. Aquí verás:
*   **Barras de Progreso:** Indican qué porcentaje de tus calorías, carbohidratos, proteínas y grasas has consumido hoy.
*   **Estado del IMC:** Tu Índice de Masa Corporal actualizado según tu último pesaje.
*   **Acceso Rápido:** Botones grandes para saltar directamente a las secciones de registro, evolución o ajustes.

### 3. Registro de Comidas (Mis Comidas)
Para registrar lo que comes:
1.  Haz clic en **"Añadir Fila"**.
2.  Haz **doble clic** en la primera celda para seleccionar un alimento de la lista.
3.  Introduce la cantidad en **gramos**. Los macros se calcularán automáticamente.
4.  Pulsa **"Guardar Registro Diario"** para que los datos se sumen a tu progreso.
5.  *Tip:* Puedes crear nuevos alimentos o categorías con los botones superiores si no encuentras lo que buscas.

### 4. Seguimiento (Mi Evolución)
*   **Calendario:** Selecciona cualquier día del mes para ver qué comiste y cómo se distribuyeron tus macros en esa fecha específica.
*   **Gráficas:** Observa la tendencia de tu peso en los últimos pesajes y compara tu consumo calórico de la última semana.

### 5. Ajustes y Perfil
*   **Datos Físicos:** Actualiza tu peso y edad regularmente para que los objetivos se mantengan precisos.
*   **Objetivos:** Cambia entre "Perder peso", "Mantener" o "Ganar músculo" para recalcular tus metas nutricionales.
*   **Seguridad:** Desde aquí puedes cambiar tu email o contraseña actual de forma segura.

---
**Desarrollado para la asignatura de Desarrollo de Interfaces.**
