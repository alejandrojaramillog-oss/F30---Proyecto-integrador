# F30 - Proyecto integrador

Aplicación Java de escritorio para gestionar descuentos de pedidos de una cafetería. Usa POO, una interfaz funcional con lambda, Swing y persistencia JDBC con SQLite, sin frameworks externos.

## Código implementado

- `model`: `Producto`, `Bebida`, `Comida` y la interfaz funcional `ReglaDescuento`.
- `Main`: conserva la regla del 15 % para montos mayores que 10 y abre Swing; intenta inicializar SQLite antes de abrir la ventana.
- `ui/VentanaCafeteria`: entrada de monto, cálculo y resultado; informa el estado de conexión y si el pedido se guardó o falló.
- `db/ConexionSQLite`: conexiones al archivo local y diagnóstico de controlador ausente.
- `db/RepositorioPedidos`: creación de la tabla, guardado con consultas parametrizadas y recuperación del historial mediante `listar()`.
- `db/PedidoGuardado`: datos recuperados de cada pedido, con identificador y fecha UTC.

La persistencia recibe los importes calculados por el modelo; no implementa otra regla de descuento. Todas las conexiones, consultas y resultados JDBC se cierran con `try-with-resources`. Los errores `SQLException` se muestran en la ventana; un fallo de guardado no se presenta como un guardado exitoso.

## Requisitos

JDK 11 o superior. SQLite requiere su controlador JDBC: se usa [Xerial SQLite JDBC 3.54.1.0](https://github.com/xerial/sqlite-jdbc/releases/tag/3.54.1.0), un controlador de base de datos. No se utiliza un framework de aplicación, Maven ni Gradle.

## macOS y Linux

Desde la raíz del proyecto:

```bash
sh ejecutar.sh
```

La primera ejecución descarga el controlador desde Maven Central y verifica su SHA-256. Las ejecuciones siguientes reutilizan la copia verificada en `lib`. La base se crea en `datos/cafeteria.db`; el archivo se conserva al cerrar y reabrir la aplicación.

Para compilar y ejecutar manualmente:

```bash
sh configurar-sqlite.sh
mkdir -p out
find src -name '*.java' -print0 | xargs -0 javac --release 11 -encoding UTF-8 -d out
java -cp 'out:lib/*' Main
```

Para elegir otra ubicación o comprobar un error de conexión, cambiar la propiedad `cafeteria.db`:

```bash
java -Dcafeteria.db=/ruta/a/cafeteria.db -cp 'out:lib/*' Main
```

Un directorio usado como archivo o una carpeta inaccesible debe producir un mensaje de error real en la ventana.

## Windows (PowerShell)

```powershell
New-Item -ItemType Directory -Force lib,out | Out-Null
$driver = 'lib/sqlite-jdbc-3.54.1.0.jar'
Invoke-WebRequest 'https://repo.maven.apache.org/maven2/org/xerial/sqlite-jdbc/3.54.1.0/sqlite-jdbc-3.54.1.0.jar' -OutFile $driver
if ((Get-FileHash $driver -Algorithm SHA256).Hash -ne 'f8dce420a6016278497f4f0560f65602b9db858de141b541cf91a4644ebe4639') { throw 'Checksum incorrecto' }
$fuentes = Get-ChildItem src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac --release 11 -encoding UTF-8 -d out $fuentes
java -cp 'out;lib/*' Main
```

El separador de classpath en Windows es `;`. En macOS y Linux es `:`.

## Verificar la persistencia

```bash
sh verificar.sh
```

Las pruebas usan archivos temporales, sin modificar los pedidos de la aplicación. Comprueban creación e inicialización repetida, guardado, lectura con otra conexión, identificadores, fecha, rechazo de importes inválidos, errores de ruta, archivo corrupto y recuperación después de un fallo. Otro proceso sin el JAR verifica el mensaje de controlador ausente.

La prueba de Swing ejecuta el botón sin mostrar ventanas y verifica el guardado y los mensajes de error. En servidores sin pantalla se omite esa prueba con un aviso explícito. No hay frameworks de pruebas externos.

`datos/`, `out/` y los JAR descargados en `lib/` se excluyen de Git.
