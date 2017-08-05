package cn.edu.sdut.softlab.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.Size;

/**
 * The persistent class for the information database table.
 *
 */
@Entity
@Table(name = "news")
@NamedQueries({@NamedQuery(name = "News.findAll", query = "SELECT n FROM News n"),
                @NamedQuery(name = "News.findById",query = "SELECT n FROM News n WHERE n.id = :id"),
                @NamedQuery(name = "News.findByTeamId",query = "SELECT n FROM News n WHERE n.team.id = :team_id"),
                @NamedQuery(name = "News.findByStatus",query = "SELECT n FROM News n WHERE n.status = :status")})
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INFORMATION_ID_GENERATOR")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFORMATION_ID_GENERATOR")
    private int id;

    @Lob
    private String content;

    private String level;
    
    @Size(max = 20)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Lob
    private String title;

    //bi-directional many-to-one association to Student
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @ManyToOne
    private Team team;

    public News() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
