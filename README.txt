CÓMO EJECUTAR (Visual Studio Code)
1. Instalar JDK 8 o superior y la extensión "Extension Pack for Java" en VS Code.
2. Seleccionar la carpeta ISII_09_Cuentas.
3. Abrir src/Vista/frmTransacciones.java y pulsar "Run".

Por terminal (dentro de la carpeta ISII_09_Cuentas):
   javac -encoding UTF-8 -d bin src/Reglas/*.java src/Vista/*.java
   java -cp bin Vista.frmTransacciones
