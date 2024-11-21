/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DatabaseConnection.Schedule_TicketDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.Schedulepanel;

/**
 *
 * @author kenlee
 */
public class Schedulecontroller implements ActionListener{
    private Schedulepanel schedulepanel;
    private Schedule_TicketDAO scheduleDAO;
    
    public Schedulecontroller( Schedulepanel shSchedulepanel){
        this.schedulepanel = shSchedulepanel;
        schedulepanel.setActionListener(this);
                
    } 

    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = e.getActionCommand();
        
        
        switch (btn) {
            case "Insert":
                schedulepanel.showInsertdialog();
                
                break;
            case "Edit":
               schedulepanel.showEditDialog();
                break;
            case "Delete":
                schedulepanel.deleteSchedule();
                break;
            case "OK": 
                if(e.getSource().equals(schedulepanel.getOk_insert())){
                  schedulepanel.insertSchedule();
                } else if (e.getSource().equals(schedulepanel.getOk_edit())){
                  schedulepanel.editSchedule();
                }
                
                break;
            case "Cancel":
                if(e.getSource().equals(schedulepanel.getCancel_insert())){
                    schedulepanel.cancelInsert();
                } else if (e.getSource().equals(schedulepanel.getCancel_edit())){
                    schedulepanel.cancelEdit();
                }
                break;
            case "btn_confirm":
                //
            default:
                break;
        }
        
         }
    
}
