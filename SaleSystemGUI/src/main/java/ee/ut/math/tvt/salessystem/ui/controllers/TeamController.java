package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.ImageView;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class TeamController implements Initializable {
    private static final Logger log = LogManager.getLogger(TeamController.class);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Team tab initialized");
        //Loading application properties
        Properties properties = null;
        try {
            properties = new Properties();
            InputStream resourceAsStream =  TeamController.class.getClassLoader().getResourceAsStream("application.properties");
            if (resourceAsStream != null) {
                properties.load(resourceAsStream);
            }
        } catch (IOException e) {
            log.error("Properties file was not found");
        }

        this.team_name.setText(properties.getProperty("team_name"));
        this.team_leader.setText(properties.getProperty("team_leader"));
        this.team_leader_email.setText(properties.getProperty("team_leader_email"));
        this.team_members.setText(properties.getProperty("team_members"));

        if(team_name.getText() != null && team_leader.getText() != null&& team_leader_email.getText() != null&& team_members.getText() != null){
            log.debug("Team info properly set");
        }
    }
}