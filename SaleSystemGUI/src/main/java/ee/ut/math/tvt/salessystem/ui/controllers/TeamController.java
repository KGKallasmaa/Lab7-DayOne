package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamController implements Initializable  {

    private javafx.scene.control.TextField team_name;
    private javafx.scene.control.TextField team_leader;
    private javafx.scene.control.TextField team_leader_email;
    private javafx.scene.control.TextField team_members;

    protected void setTeam_name(){
        this.team_name = new TextField("Lab-7-dayOne");
    }
    protected void setTeam_leader(){
        this.team_leader = new TextField("Larry Ellison");
    }
    protected void setTeam_leader_email(){
        this.team_leader_email = new TextField("info@oracle.com");
    }
    protected void setTeam_members(){
        this.team_members = new TextField("Fred Kasemaa"+"\n"+"Villem Laimre"+"\n"+"Karl-Gustav Kallasmaa");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
