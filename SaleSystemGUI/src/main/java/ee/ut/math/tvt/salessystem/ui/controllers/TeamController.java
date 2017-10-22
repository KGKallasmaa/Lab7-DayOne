package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import javax.swing.text.html.ImageView;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class TeamController implements Initializable {

    @FXML
    private Text team_name;
    @FXML
    private Text team_leader;
    @FXML
    private Text team_leader_email;
    @FXML
    private Text team_members;

    public TeamController() {
    }

    public Text getTeam_name() {
        return team_name;
    }

    public Text getTeam_leader() {
        return team_leader;
    }

    public Text getTeam_leader_email() {
        return team_leader_email;
    }

    public Text getTeam_members() {
        return team_members;
    }

    protected void setTeam_name(String text) {
        Text new_text = new Text(text);
        this.team_name = new_text;
    }

    protected void setTeam_leader(String text) {
        Text new_text = new Text(text);
        this.team_leader = new_text;
    }

    protected void setTeam_leader_email(String text) {
        Text new_text = new Text(text);
        this.team_leader_email = new_text;
    }

    protected void setTeam_members(String text) {
        Text new_text = new Text(text);
        this.team_members = new_text;
    }


    public Properties load_prop () {
        Properties prop = new Properties();
        return prop;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties prop = load_prop();
        //BROKEN~!!!
        this.team_name.setText(prop.getProperty("team_name"));
        this.team_leader.setText(prop.getProperty("team_leader"));
        this.team_leader_email.setText(prop.getProperty("team_leader_email"));
        this.team_members.setText(prop.getProperty("team_members"));
    }
}