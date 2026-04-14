``` mermaid
classDiagram
    class CategoriaAlimento {
        - int idCategoria
        - String nombre
        + getIdCategoria() int
        + getNombre() String
    }

    class ObjetivoUsuario {
        - int idObjetivo
        - String nombre
        - String descripcion
        - getIdObjetivo() int
        - getNombre() String
    }

    class Usuario {
        - int idUsuario
        - String email
        - String password
        - String nombre
        - String apellidos
        - int edad
        - int altura
        - double pesoInicial
        - String rol
        - ObjetivoUsuario objetivo
        + getObjetivo() ObjetivoUsuario
        + toString() String
    }

    class Alimento {
        - int idAlimento
        - String nombre
        - String marca
        - double kcal
        - double proteinas
        - double hidratosCarbono
        - double grasas
        - CategoriaAlimento categoria
        + getCategoria() CategoriaAlimento
    }

    class RegistroDiario {
        - int idRegistro
        - double cantidadGramos
        - LocalDate fecha
        - String tipoComida
        - Usuario usuario
        - Alimento alimento
        + calcularKcalConsumidas() double
    }

    class EvolucionFisica {
        - int idEvolucion
        - double pesoRegistrado
        - LocalDate fecha
        - Usuario usuario
    }
    
    Usuario --> ObjetivoUsuario : tiene un
    Alimento --> CategoriaAlimento : pertenece a
    RegistroDiario --> Usuario : registrado por
    RegistroDiario --> Alimento : contiene
    EvolucionFisica --> Usuario : pertenece a
```
