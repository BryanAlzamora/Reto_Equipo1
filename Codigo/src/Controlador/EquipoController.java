package Controlador;

import Excepcion.DatoNoValido;
import Modelo.Equipo;
import Modelo.EquipoDAO;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquipoController {
    private final EquipoDAO equipoDAO;

    public EquipoController(EquipoDAO equipoDAO) {
        this.equipoDAO = equipoDAO;
    }

    public void altaValidarDatosEquipo() {
        String cod = solicitarDatos("Código", "Introduce el código del equipo", "^[0-9]{4}$");
        String nombre = solicitarDatos("Nombre", "Introduce el nombre del equipo", "^[A-Z][a-z]*$");
        LocalDate fecha = formatearFecha(solicitarDatos("Fecha de fundación", "Introduce la fecha de fundación del equipo", ""));

        Equipo equipo = new Equipo(cod, nombre, fecha);

        equipoDAO.insertar(equipo);
    }

    private String solicitarDatos(String dato, String mensaje, String exprRegular) {
        String variable = "";
        boolean terminar = false;

        do {
            try {
                variable = JOptionPane.showInputDialog(mensaje);

                if (variable.isEmpty()) {
                    throw new DatoNoValido(dato + " es un campo obligatorio a rellenar");
                }

                Pattern pat = Pattern.compile(exprRegular);
                Matcher mat = pat.matcher(variable);
                if (!mat.matches()) {
                    throw new DatoNoValido(dato + " no se ha introducido de forma correcta");
                }

                terminar = true;

            } catch (DatoNoValido e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (terminar);

        return variable;
    }

    public void modificar() {

        ArrayList<Equipo> equipos = equipoDAO.getEquipos();

        Equipo[] opciones = equipos.toArray(new Equipo[0]);
        Equipo opcionElegida = (Equipo) JOptionPane.showInputDialog(null, "Elija a que equipo le quiere modificar los datos", "Modificación", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        opcionElegida.setNombreEquipo(solicitarDatos("Nombre", "Introduce el nuevo nombre del equipo", "^[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*$"));
        opcionElegida.setFechaFund(formatearFecha(solicitarDatos("Fecha de fundación", "Introduce la nueva fecha de fundación del equipo", "^$")));

        JOptionPane.showMessageDialog(null, "Se han modificado los datos del equipo correctamente");
    }

    public void borrar() {

        ArrayList<Equipo> equipos = equipoDAO.getEquipos();

        Equipo[] opciones = equipos.toArray(new Equipo[0]);
        Equipo opcionElegida = (Equipo) JOptionPane.showInputDialog(null, "Elige a que equipo le quieres dar de baja", "Dar de Baja", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        equipoDAO.borrar(opcionElegida);

        JOptionPane.showMessageDialog(null, "Se ha dado de baja al equipo con éxito", "Baja Completada", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrar() {
        ArrayList<Equipo> equipos = equipoDAO.getEquipos();

        JOptionPane.showMessageDialog(null, equipos);
    }

    private LocalDate formatearFecha(String fecha) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(fecha, formato);
    }
}
