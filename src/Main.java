import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "RIBERA";
    private static final String PASS = "ribera";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n--- GESTIÓN DE EMPLEADOS (CRUD) ---");
            System.out.println("1. Insertar nuevo empleado");
            System.out.println("2. Ver todos los empleados");
            System.out.println("3. Actualizar salario por ID");
            System.out.println("4. Eliminar empleado por ID");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> crearEmpleado(sc);
                    case 2 -> leerEmpleados();
                    case 3 -> actualizarSalario(sc);
                    case 4 -> eliminarEmpleado(sc);
                    case 5 -> System.out.println("Cerrando programa...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 5);
        sc.close();
    }
//Crear - Create
    private static void crearEmpleado(Scanner sc) throws SQLException {
        System.out.print("ID del empleado: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Salario: ");
        double salario = Double.parseDouble(sc.nextLine());

        String sql = "INSERT INTO empleado (id, nombre, salaio) VALUES (?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setDouble(3, salario);
            ps.executeUpdate();
            System.out.println("Empleado guardado con éxito.");
        }
    }
//Leer - Read
    private static void leerEmpleados() throws SQLException {
        String sql = "SELECT * FROM empleado ORDER BY id ASC";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | NOMBRE | SALARIO");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.printf("%d | %s | %.2f€%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salaio"));
            }
        }
    }
//Actualizar - update
    private static void actualizarSalario(Scanner sc) throws SQLException {
        System.out.print("ID del empleado a modificar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo salario: ");
        double nuevoSalario = Double.parseDouble(sc.nextLine());

        String sql = "UPDATE empleado SET salaio = ? WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSalario);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Salario actualizado." : "No existe el ID.");
        }
    }
//eliminar - delete
    private static void eliminarEmpleado(Scanner sc) throws SQLException {
        System.out.print("ID del empleado a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM empleado WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Usuario eliminado." : "El ID no existe.");
        }
    }
}