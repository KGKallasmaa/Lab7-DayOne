package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamController implements Initializable  {
    @FXML private Text team_name;
    @FXML private Text team_leader;
    @FXML private Text team_leader_email;
    @FXML private Text team_members;

    protected void setTeam_name(){
        this.team_name = new Text("Lab-7-dayOne");
    }
    protected void setTeam_leader(){
        this.team_leader = new Text("Larry Ellison");
    }
    protected void setTeam_leader_email(){
        this.team_leader_email = new Text("info@oracle.com");
    }
    protected void setTeam_members(){
        this.team_members = new Text("Fred Kasemaa"+"\n"+"Villem Laimre"+"\n"+"Karl-Gustav Kallasmaa");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
