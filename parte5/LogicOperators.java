package parte5;

import parte3.Employee;
import parte3.Employee.TypeEmployee;
import java.util.List;
import java.util.Arrays;

public class LogicOperators {
    public static void main(String[] args) {
        // Crear empleados y procesar nómina
        Employee e1 = new Employee("Ana", TypeEmployee.INTERN, "1001", 0.0);
        Employee e2 = new Employee("Luis", TypeEmployee.STAFF, "1002", 0.0);
        Employee e3 = new Employee("Sofia", TypeEmployee.MANAGER, "1003", 0.0);
        Employee e4 = new Employee("Carlos", TypeEmployee.STAFF, "1004", 0.0);
        Employee e5 = new Employee("Maria", TypeEmployee.INTERN, "1005", 0.0);

        List<Employee> empleados = Arrays.asList(e1, e2, e3, e4, e5);
        empleados.add(new Employee("Nico", TypeEmployee.INTERN, "1001", 0.0));

        Payroll payroll = new Payroll(empleados);
        payroll.processPayroll();

        // Uso de operadores lógicos para identificar miembros VIP
        System.out.println("\n--- Verificando miembros VIP ---");
        for (Employee emp : empleados) {
            // Usando && (AND lógico)
            if (emp.getType() == TypeEmployee.STAFF && emp.getAccount().getBalance() > 5000) {
                System.out.println(emp.getName() + " es STAFF y tiene más de 5000 en su cuenta: ¡Miembro VIP!");
            }
            // Usando & (AND bit a bit, pero funciona como AND lógico en booleanos)
            if (emp.getType() == TypeEmployee.STAFF & emp.getAccount().getBalance() > 5000) {
                // Solo para mostrar el uso de &
                // No se recomienda usar & en vez de && para condiciones lógicas, pero es válido en Java
            }
            // Usando || (OR lógico)
            if (emp.getType() == TypeEmployee.MANAGER || emp.getAccount().getBalance() > 10000) {
                System.out.println(emp.getName() + " es MANAGER o tiene más de 10000 en su cuenta.");
            }
        }
    }
}