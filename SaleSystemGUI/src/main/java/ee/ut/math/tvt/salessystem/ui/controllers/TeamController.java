package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamController implements Initializable  {

    @FXML private Text team_name = this.getTeam_name();
    @FXML private Text team_leader = this.getTeam_leader();
    @FXML private Text team_leader_email = this.getTeam_leader_email();
    @FXML private Text team_members = this.getTeam_members();

    public TeamController(){
    }

    public Text getTeam_name(){
        return team_name;
    }
    public Text getTeam_leader(){
        return team_leader;
    }
    public Text getTeam_leader_email(){
        return team_leader_email;
    }
    public Text getTeam_members(){
        return team_members;
    }
    protected void setTeam_name(Text new_name){
        this.team_name = new_name;
    }
    protected void setTeam_leader(Text new_leader){
        this.team_leader = new_leader;
    }
    protected void setTeam_leader_email(Text new_email){
        this.team_leader_email = new_email;
    }
    protected void setTeam_members(Text new_members){
        this.team_members = new_members;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        team_name.setText("Lab-7-dayOne");
        team_leader.setText("Larry Ellison");
        team_leader_email.setText("info@oracle.com");
       team_members.setText("Fred Kasemaa"+"\n"+"Villem Laimre"+"\n"+"Karl-Gustav Kallasmaa");
    }
}
