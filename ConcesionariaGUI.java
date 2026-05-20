import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import concesionario.*;

public class ConcesionariaGUI extends JFrame {
    private Concesionaria concesionaria;

    public ConcesionariaGUI() {
        concesionaria = new Concesionaria();
        setTitle("Sistema de Concesionaria");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        mostrarMenuPrincipal();
    }

    private void mostrarMenuPrincipal() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        menuPanel.add(titulo, BorderLayout.NORTH);

        JButton inventarioVentasBtn = new JButton("Gestión de Inventario/Ventas");
        JButton gestionUsuarioBtn = new JButton("Gestión de Usuario");

        inventarioVentasBtn.addActionListener(e -> mostrarInventarioVentasPanel());
        gestionUsuarioBtn.addActionListener(e -> mostrarUsuarioPanel());

        JPanel botones = new JPanel(new GridLayout(2, 1, 10, 10));
        botones.add(inventarioVentasBtn);
        botones.add(gestionUsuarioBtn);

        menuPanel.add(botones, BorderLayout.CENTER);

        setContentPane(menuPanel);
        revalidate();
        repaint();
        setVisible(true);
    }

    private void mostrarInventarioVentasPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Registrar Venta", crearPanelRegistroVenta());
        tabbedPane.addTab("Ventas por Vendedor", crearPanelVentasVendedor());
        tabbedPane.addTab("Registrar Auto", crearPanelRegistrarAuto());
        tabbedPane.addTab("Consulta General", crearPanelConsultaGeneral());
        tabbedPane.addTab("Buscar por Modelo", crearPanelBusquedaModelo());
        tabbedPane.addTab("Buscar por Estado", crearPanelBusquedaEstado());
        tabbedPane.addTab("Modificar/Eliminar Auto", crearPanelModificarEliminarAuto());

        JButton volver = new JButton("Volver al Menú Principal");
        volver.addActionListener(e -> mostrarMenuPrincipal());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(volver, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void mostrarUsuarioPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Registrar Vendedor", crearPanelRegistrarVendedor());
        tabbedPane.addTab("Modificar/Eliminar Vendedor", crearPanelModificarEliminarVendedor());
        tabbedPane.addTab("Consulta General Vendedores", crearPanelConsultaGeneralVendedores());
        tabbedPane.addTab("Ventas por Vendedor", crearPanelVentasVendedor());

        JButton volver = new JButton("Volver al Menú Principal");
        volver.addActionListener(e -> mostrarMenuPrincipal());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(volver, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();
        repaint();
    }

    private JPanel crearPanelRegistroVenta() {
        JPanel panel = new JPanel(new GridLayout(9, 2));
        JComboBox<String> idVendedorBox = new JComboBox<>();
        for (Vendedor v : concesionaria.getVendedores()) {
            idVendedorBox.addItem(v.getId() + " - " + v.getNombre());
        }
        JTextField clienteField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField vinField = new JTextField();
        JComboBox<String> tipoVentaBox = new JComboBox<>(new String[]{"Contado", "Financiado"});
        JTextField precioFinalField = new JTextField();
        JTextField mensualidadesField = new JTextField();

        JButton registrarBtn = new JButton("Registrar Venta");
        registrarBtn.addActionListener(e -> {
            try {
                String selectedVendedor = (String) idVendedorBox.getSelectedItem();
                if (selectedVendedor == null) {
                    JOptionPane.showMessageDialog(this, "Seleccione un vendedor.");
                    return;
                }
                String idVendedor = selectedVendedor.split(" - ")[0];
                boolean exito = concesionaria.registrarVenta(idVendedor, clienteField.getText(), direccionField.getText(),
                        telefonoField.getText(), vinField.getText(), (String) tipoVentaBox.getSelectedItem(),
                        Double.parseDouble(precioFinalField.getText()), Integer.parseInt(mensualidadesField.getText()));

                if (exito) {
                    concesionaria.guardarDatos();
                    JOptionPane.showMessageDialog(this, "Venta registrada con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Vendedor o automóvil no encontrado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("ID Vendedor:")); panel.add(idVendedorBox);
        panel.add(new JLabel("Nombre Cliente:")); panel.add(clienteField);
        panel.add(new JLabel("Dirección Cliente:")); panel.add(direccionField);
        panel.add(new JLabel("Teléfono Cliente:")); panel.add(telefonoField);
        panel.add(new JLabel("VIN Automóvil:")); panel.add(vinField);
        panel.add(new JLabel("Tipo Venta:")); panel.add(tipoVentaBox);
        panel.add(new JLabel("Precio Final:")); panel.add(precioFinalField);
        panel.add(new JLabel("Mensualidades:")); panel.add(mensualidadesField);
        panel.add(new JLabel()); panel.add(registrarBtn);

        return panel;
    }

    private JPanel crearPanelBusquedaEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        JComboBox<String> estadoBox = new JComboBox<>(new String[]{"En concesionaria", "Vendido", "Siendo financiado"});
        JButton buscarBtn = new JButton("Buscar");
        DefaultTableModel model = new DefaultTableModel(new String[]{"VIN", "Marca", "Modelo", "Precio", "Estado"}, 0);
        JTable table = new JTable(model);

        buscarBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Automovil a : concesionaria.getAutomoviles()) {
                if (a.getEstado().equalsIgnoreCase((String) estadoBox.getSelectedItem())) {
                    model.addRow(new Object[]{a.getVin(), a.getMarca(), a.getModelo(), a.getPrecio(), a.getEstado()});
                }
            }
        });

        JPanel top = new JPanel();
        top.add(new JLabel("Estado:"));
        top.add(estadoBox);
        top.add(buscarBtn);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBusquedaModelo() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextField modeloField = new JTextField(15);
        JButton buscarBtn = new JButton("Buscar");
        DefaultTableModel model = new DefaultTableModel(new String[]{"VIN", "Marca", "Modelo", "Precio", "Estado"}, 0);
        JTable table = new JTable(model);

        buscarBtn.addActionListener(e -> {
            model.setRowCount(0);
            int idx = concesionaria.buscarAutoBinarioPorModeloIndex(modeloField.getText());
            if (idx >= 0) {
                Automovil a = concesionaria.getAutomoviles().get(idx);
                model.addRow(new Object[]{a.getVin(), a.getMarca(), a.getModelo(), a.getPrecio(), a.getEstado()});
            } else {
                JOptionPane.showMessageDialog(this, "Modelo no encontrado.");
            }
        });

        JPanel top = new JPanel();
        top.add(new JLabel("Modelo:"));
        top.add(modeloField);
        top.add(buscarBtn);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelConsultaGeneral() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"VIN", "Marca", "Modelo", "Precio", "Estado"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JButton mostrarBtn = new JButton("Mostrar Inventario");
        mostrarBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            for (Automovil auto : concesionaria.getAutomoviles()) {
                tableModel.addRow(new Object[]{
                    auto.getVin(), auto.getMarca(), auto.getModelo(), auto.getPrecio(), auto.getEstado()
                });
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(mostrarBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelRegistrarVendedor() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField idField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField telefonoField = new JTextField();
        JButton registrarBtn = new JButton("Registrar");

        registrarBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String nombre = nombreField.getText().trim();
            String telefono = telefonoField.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            Vendedor nuevo = new Vendedor(id, nombre, telefono);
            concesionaria.getVendedores().add(nuevo);
            concesionaria.guardarDatos();
            JOptionPane.showMessageDialog(this, "Vendedor registrado con éxito.");

            idField.setText("");
            nombreField.setText("");
            telefonoField.setText("");
        });

        panel.add(new JLabel("ID Vendedor:")); panel.add(idField);
        panel.add(new JLabel("Nombre:")); panel.add(nombreField);
        panel.add(new JLabel("Teléfono:")); panel.add(telefonoField);
        panel.add(new JLabel()); panel.add(registrarBtn);

        return panel;
    }

    private JPanel crearPanelVentasVendedor() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextField idField = new JTextField(10);
        JButton buscarBtn = new JButton("Buscar");
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Venta", "Cliente", "Auto", "Tipo", "Precio", "Mensualidades"}, 0);
        JTable table = new JTable(model);

        buscarBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Venta v : concesionaria.getVentas()) {
                if (v.getVendedor().getId().equals(idField.getText())) {
                    model.addRow(new Object[]{v.getIdVenta(), v.getNombreCliente(), v.getAutomovil().getMarca() + " " + v.getAutomovil().getModelo(), v.getTipoVenta(), v.getPrecioFinal(), v.getMensualidades()});
                }
            }
        });

        JPanel top = new JPanel();
        top.add(new JLabel("ID Vendedor:"));
        top.add(idField);
        top.add(buscarBtn);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelRegistrarAuto() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        JTextField vinField = new JTextField();
        JTextField marcaField = new JTextField();
        JTextField modeloField = new JTextField();
        JTextField precioField = new JTextField();

        JButton registrarBtn = new JButton("Registrar Auto");
        registrarBtn.addActionListener(e -> {
            try {
                String vin = vinField.getText().trim();
                String marca = marcaField.getText().trim();
                String modelo = modeloField.getText().trim();
                double precio = Double.parseDouble(precioField.getText());

                if (vin.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                    return;
                }

                Automovil nuevo = new Automovil(vin, marca, modelo, precio, "En concesionaria");
                concesionaria.getAutomoviles().add(nuevo);
                concesionaria.guardarDatos();
                JOptionPane.showMessageDialog(this, "Automovil registrado con exito.");

                vinField.setText(""); marcaField.setText(""); modeloField.setText(""); precioField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("VIN:")); panel.add(vinField);
        panel.add(new JLabel("Marca:")); panel.add(marcaField);
        panel.add(new JLabel("Modelo:")); panel.add(modeloField);
        panel.add(new JLabel("Precio:")); panel.add(precioField);
        panel.add(new JLabel()); panel.add(registrarBtn);

        return panel;
    }

    private JPanel crearPanelModificarEliminarAuto() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"VIN", "Marca", "Modelo", "Precio", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(model);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable cargarAutos = () -> {
            model.setRowCount(0);
            for (Automovil a : concesionaria.getAutomoviles()) {
                model.addRow(new Object[]{a.getVin(), a.getMarca(), a.getModelo(), a.getPrecio(), a.getEstado()});
            }
        };

        JButton eliminarBtn = new JButton("Eliminar Seleccionado");
        eliminarBtn.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String vin = (String) model.getValueAt(fila, 0);
                concesionaria.getAutomoviles().removeIf(a -> a.getVin().equals(vin));
                concesionaria.guardarDatos();
                cargarAutos.run();
            }
        });

        JButton modificarBtn = new JButton("Modificar Seleccionado");
        modificarBtn.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String vin = (String) model.getValueAt(fila, 0);
                for (Automovil a : concesionaria.getAutomoviles()) {
                    if (a.getVin().equals(vin)) {
                        String nuevaMarca = JOptionPane.showInputDialog(this, "Nueva Marca:", a.getMarca());
                        String nuevoModelo = JOptionPane.showInputDialog(this, "Nuevo Modelo:", a.getModelo());
                        String nuevoPrecioStr = JOptionPane.showInputDialog(this, "Nuevo Precio:", a.getPrecio());

                        if (nuevaMarca != null && !nuevaMarca.trim().isEmpty()) a.setMarca(nuevaMarca);
                        if (nuevoModelo != null && !nuevoModelo.trim().isEmpty()) a.setModelo(nuevoModelo);
                        if (nuevoPrecioStr != null && !nuevoPrecioStr.trim().isEmpty()) {
                            try {
                                a.setPrecio(Double.parseDouble(nuevoPrecioStr));
                            } catch (NumberFormatException ignored) {}
                        }
                        concesionaria.guardarDatos();
                        cargarAutos.run();
                        break;
                    }
                }
            }
        });

        JPanel botones = new JPanel();
        botones.add(modificarBtn);
        botones.add(eliminarBtn);

        cargarAutos.run();
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelModificarEliminarVendedor() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Teléfono"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(model);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable cargarVendedores = () -> {
            model.setRowCount(0);
            for (Vendedor v : concesionaria.getVendedores()) {
                model.addRow(new Object[]{v.getId(), v.getNombre(), v.getTelefono()});
            }
        };

        JButton eliminarBtn = new JButton("Eliminar Seleccionado");
        eliminarBtn.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String id = (String) model.getValueAt(fila, 0);
                concesionaria.getVendedores().removeIf(v -> v.getId().equals(id));
                concesionaria.guardarDatos();
                cargarVendedores.run();
            }
        });

        JButton modificarBtn = new JButton("Modificar Seleccionado");
        modificarBtn.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String id = (String) model.getValueAt(fila, 0);
                for (Vendedor v : concesionaria.getVendedores()) {
                    if (v.getId().equals(id)) {
                        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", v.getNombre());
                        String nuevoTelefono = JOptionPane.showInputDialog(this, "Nuevo Teléfono:", v.getTelefono());

                        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) v.setNombre(nuevoNombre);
                        if (nuevoTelefono != null && !nuevoTelefono.trim().isEmpty()) v.setTelefono(nuevoTelefono);

                        concesionaria.guardarDatos();
                        cargarVendedores.run();
                        break;
                    }
                }
            }
        });

        JPanel botones = new JPanel();
        botones.add(modificarBtn);
        botones.add(eliminarBtn);

        cargarVendedores.run();
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelConsultaGeneralVendedores() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Teléfono"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(model);

        JButton mostrarBtn = new JButton("Mostrar Vendedores");
        mostrarBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Vendedor v : concesionaria.getVendedores()) {
                model.addRow(new Object[]{v.getId(), v.getNombre(), v.getTelefono()});
            }
        });

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(mostrarBtn, BorderLayout.SOUTH);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConcesionariaGUI::new);
    }
} 