# Guía de Arquitectura Nutrix: MVC y Gráficas Personalizadas 

Este documento explica cómo funciona internamente la aplicación Nutrix tras la refactorización a un modelo de diseño **MVC (Modelo-Vista-Controlador)**.

---

## 1. El Corazón de la App: Patrón MVC y Diseño Earthy

Para que el código sea fácil de leer y mantener, hemos separado las responsabilidades en tres capas, todo envuelto en una estética cálida de **tonos tierra y café**.

### El Modelo (`com.nutrilern.modelo`)
Son las clases que gestionan los datos y la base de datos (DAOs).
- **DAOs (`AlimentoDAO`, `UsuarioDAO`, etc.)**: Contienen las consultas SQL. Su única misión es hablar con TiDB Cloud y devolver objetos Java.
- **Entidades (`Alimento`, `Usuario`)**: Clases simples que representan la información (nombre, calorías, peso, etc.).

### El Controlador (`com.nutrilern.controlador`)
Es el "cerebro". Aquí reside la lógica que conecta la interfaz con los datos.
- **`ControladorVistas`**: Prepara "paquetes" de datos para las pantallas. Por ejemplo, junta las calorías, el IMC y los macros en un solo mapa para que el Dashboard solo tenga que pintarlos.
- **`CalculadoraNutricional`**: Contiene las fórmulas matemáticas (IMC, cálculo de calorías diarias según el objetivo).

### La Vista (`com.nutrilern.vista`)
Es la parte visual (Swing). 
- **Paneles (`PanelMenuPrincipal`, `PanelEvolucion`)**: Solo se encargan del diseño. No saben qué es una base de datos. Piden los datos al controlador y los muestran.

---

## 2. El Sistema de Gráficas (Java 2D)

A diferencia de otras apps que usan librerías pesadas, Nutrix dibuja sus propias gráficas píxel a píxel en el `PanelEvolucion`. Esto se hace mediante el método `paintComponent`.

### Gráfica de Barras (Calorías)
- **Cálculo**: Se busca el valor máximo de la semana para ajustar la escala.
- **Dibujo**: Se calcula la altura proporcional: `(valor / max_semana) * altura_disponible`.

### Gráfica Circular (Macros)
- **Cálculo**: Los gramos se convierten a porcentajes sobre el total.
- **Dibujo**: Se usa `fillArc`. Cada porcentaje se multiplica por `3.6` para obtener los grados del círculo (100% = 360º).

### Gráfica Lineal (Peso)
- **Escala Elástica**: La gráfica se ajusta automáticamente para que los cambios de peso (aunque sean pocos kilos) se vean claros, calculando el mínimo y máximo real de la serie.

---

## 3. Flujo de Datos Típico

1. El usuario hace clic en el **Calendario**.
2. La vista captura la **Fecha** y se la pasa al `ControladorVistas`.
3. El controlador pide los macros al `AlimentoDAO` de esa fecha.
4. El DAO lanza una consulta `SELECT SUM(...)` a la base de datos.
5. El controlador recibe los gramos, los pasa a porcentajes y se los devuelve a la vista.
6. La vista llama a `repaint()` y la gráfica circular se actualiza mágicamente.

---

**Nota**: Si el día de mañana cambiamos la base de datos de TiDB a otra, solo tenemos que tocar el Modelo. El diseño (Vista) y la lógica (Controlador) seguirán funcionando igual.
