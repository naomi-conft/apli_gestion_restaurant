package com.restaurant.views;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

public class DetailsCommandeDialog extends JDialog {

    private JTextArea textArea;
    private JButton btnImprimer;
    private JButton btnFermer;

    public DetailsCommandeDialog(Frame owner, String title, String content) {
        super(owner, title, true); // Modal dialog
        
        initComponents(content);
        
        // Settings basic properties
        setSize(500, 600);
        setLocationRelativeTo(owner);
    }

    private void initComponents(String content) {
        setLayout(new BorderLayout(10, 10));

        // Content Area
        textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnImprimer = new JButton("🖨️ Imprimer");
        btnImprimer.setBackground(new Color(33, 150, 243));
        btnImprimer.setForeground(Color.WHITE);
        btnImprimer.addActionListener(e -> imprimerFacture());
        
        btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());

        buttonPanel.add(btnImprimer);
        buttonPanel.add(btnFermer);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void imprimerFacture() {
        try {
            boolean complete = textArea.print(
                new MessageFormat("Facture"), // Header
                new MessageFormat("Page - {0}"), // Footer
                true, // Show print dialog
                null, // Service
                null, // Attributes
                true // Interactive
            );

            if (complete) {
                JOptionPane.showMessageDialog(this,
                    "Impression terminée",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Printing cancelled by user
            }

        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'impression : " + pe.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
