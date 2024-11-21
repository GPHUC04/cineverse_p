package controller;

import DatabaseConnection.Schedule_TicketDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.html.parser.DTDConstants;
import view.Ticketpanel;

/**
 * Controller class for TicketPanel to handle actions for Confirm, Cancel, Pay, and Cancel (for pay).
 */
public class TicketController implements ActionListener {
    private Ticketpanel ticketPanel;
    private Schedule_TicketDAO scheduleDAO;

    public TicketController(Ticketpanel ticketPanel) {
        this.ticketPanel = ticketPanel;
        ticketPanel.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = e.getActionCommand();

        switch (btn) {
            case "Confirm":
                ticketPanel.btnConfirmActionPerformed();
                break;
                
            case "Cancel":
                if (e.getSource().equals(ticketPanel.getBtn_cancel_confirm())) {
                ticketPanel.btnCancelActionPerformed();
                } else if (e.getSource().equals(ticketPanel.getBtn_cancel_pay())) {
                ticketPanel.btnCancelPayActionPerformed();
                }
                break;
            case "Pay":
                ticketPanel.btnPayActionPerformed();
                break;

            default:
                break;
        }
    }
}
