# F30 - Proyecto integrador

## Descripcion del proyecto

Aplicacion de escritorio basada en Java para calcular el precio final de productos aplicando reglas de descuento. El proyecto modela productos de tipo bebida y comida mediante herencia, y permite definir descuentos mediante una interfaz funcional, lo que prepara la logica para trabajar con expresiones lambda.

## Lo implementado hasta el momento

- Clase abstracta `Producto` con nombre y precio.
- Validacion de nombres vacios y precios negativos.
- Clase `Comida`, con la propiedad de si es vegetariana.
- Clase `Bebida`, con la propiedad de si es fria.
- Metodo para obtener la categoria de cada producto.
- Interfaz funcional `ReglaDescuento` para calcular descuentos.
- Calculo del precio final con descuento y proteccion para evitar precios negativos.
- Clase `Main` como punto de entrada inicial del programa.

## Estructura del proyecto

### Estructura actual

```text
F30---Proyecto-integrador/
├── .git/
├── .gitignore
├── README.md
├── src/
│   ├── Main.java
│   └── model/
│       ├── Bebida.java
│       ├── Comida.java
│       ├── Producto.java
│       └── ReglaDescuento.java
```

### Estructura prevista para las siguientes fases

```text
src/
├── Main.java
├── model/
│   ├── Bebida.java
│   ├── Comida.java
│   ├── Producto.java
│   └── ReglaDescuento.java
├── service/
│   └── Logica de calculo y reglas de descuento
├── db/
│   └── Acceso a datos y persistencia JDBC
└── ui/
    └── Interfaz grafica Swing
```

Las carpetas `service`, `db` y `ui` representan la estructura que se implementara en las siguientes fases. Todavia no forman parte del codigo actual.

## Requisitos

- Java Development Kit (JDK) 11 o superior.
- Terminal compatible con el sistema operativo.

## Compilar y ejecutar

Ejecuta los siguientes comandos desde la carpeta raiz del proyecto.

### Linux

```bash
mkdir -p out
find src -name "*.java" -print0 | xargs -0 javac -d out
java -cp out Main
```

### macOS

```bash
mkdir -p out
find src -name "*.java" -print0 | xargs -0 javac -d out
java -cp out Main
```

### Windows

**PowerShell o CMD:**

```powershell
$javaFiles = Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
mkdir out -ErrorAction SilentlyContinue
javac -d out $javaFiles
java -cp out Main
```